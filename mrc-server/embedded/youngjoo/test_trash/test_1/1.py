import asyncio
import websockets
import numpy as np
import cv2
import threading
from datetime import datetime, timezone

THRESHOLD_MILLIS = 100

async def client_connection(websocket, path):
    print("establishing client connection...", websocket)
    global counter
    async for message in websocket:
        print("Received message from client:", message)

async def camera_stream(websocket, path):
    global counter
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("Error: Couldn't open camera")
        return

    while True:
        ret, frame = cap.read()
        if not ret:
            print("Error: Couldn't read frame")
            break

        current_time = int((datetime.now(timezone.utc) - datetime(1970, 1, 1, tzinfo=timezone.utc)).total_seconds() * 1000)
        await websocket.send(frame.tobytes())

async def serve(websocket, path):
    await asyncio.gather(client_connection(websocket, path), camera_stream(websocket, path))

async def main():
    port = 8080
    async with websockets.serve(serve, "localhost", port):
        print(f"Server started at ws://localhost:{port}")
        await asyncio.Future()  # run forever

if __name__ == "__main__":
    asyncio.run(main())
