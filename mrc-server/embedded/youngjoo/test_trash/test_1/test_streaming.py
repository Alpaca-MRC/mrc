import asyncio
import logging
import websockets
from PIL import Image
from io import BytesIO
from asyncio import Queue
from time import time
from picamera2 import Picamera2

# Constants
THRESHOLD_MILLIS = 100

async def client_connection(websocket, queue, counter):
    try:
        logging.info(f"establishing client connection... {websocket}")
        async for message in websocket:
            image_bytes = BytesIO(message)
            frame = Image.open(image_bytes)
            await queue.put((frame, int(time() * 1000)))  # Putting frame and timestamp into the queue
    except websockets.exceptions.ConnectionClosedError:
        logging.info("Connection closed")
    finally:
        async with counter_lock:
            counter -= 1
            logging.info(f"Removing connection, connection counter: {counter}")

async def main():
    logging.basicConfig(level=logging.INFO)
    width = int(os.getenv("VIDEO_WIDTH", 1920))
    height = int(os.getenv("VIDEO_HEIGHT", 1080))
    video_device_index = int(os.getenv("VIDEO_DEVICE_INDEX", 0))
    framerate = int(os.getenv("FRAMERATE", 10))
    port = int(os.getenv("PORT", 8080))

    logging.warn(f"Framerate {framerate}")

    counter = 0
    counter_lock = asyncio.Lock()
    queue = Queue()
    server = await websockets.serve(
        lambda ws, path: client_connection(ws, queue, counter),
        "192.168.137.197", port
    )

    # Camera capturing loop
    while True:
        async with counter_lock:
            if counter <= 0:
                continue

        camera = Picamera2(video_device_index, width, height, framerate)
        camera.open()
        try:
            while True:
                async with counter_lock:
                    if counter <= 0:
                        break

                frame = camera.capture()
                await queue.put((frame, int(time() * 1000)))  # Putting frame and timestamp into the queue
        finally:
            camera.close()

asyncio.run(main())
