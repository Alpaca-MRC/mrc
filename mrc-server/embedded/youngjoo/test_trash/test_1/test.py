import asyncio
import websockets
import numpy as np
from picamera2 import Picamera2, Preview
import threading
from datetime import datetime, timezone

PAGE = """\
<html>
<head>
    <title>Camera Stream</title>
    
</head>
<body>
    <video id="video" autoplay></video>

    <script>
        const video = document.getElementById('video');
        const ws = new WebSocket('ws://localhost:8080');

        ws.onmessage = function(event) {
            // 받은 이진 데이터를 Blob으로 변환합니다.
            const blob = new Blob([event.data], { type: 'image/jpeg' });

            // Blob을 URL로 변환하여 비디오 요소에 표시합니다.
            video.src = URL.createObjectURL(blob);
        };
    </script>
</body>
</html>
"""

async def client_connection(websocket, path):
    print("establishing client connection...", websocket)
    async for message in websocket:
        print("Received message from client:", message)

async def camera_stream(websocket, path):
    picam2 = Picamera2()
    preview_config = picam2.create_preview_configuration()
    picam2.configure(preview_config)
    picam2.start()

    while True:
        request = picam2.capture_request()
        request.wait()
        frame = request.get_array() 

        if frame is None:
            print("Error: Couldn't read frame")
            break

        await websocket.send(frame.tobytes())

# async def serve(websocket, path):
#     print('connecting')
#     await asyncio.gather(client_connection(websocket, path), camera_stream(websocket, path))
        
async def serve(websocket, path):
    print('connecting')
    task1 = asyncio.create_task(client_connection(websocket, path))
    task2 = asyncio.create_task(camera_stream(websocket, path))
    await task1
    await task2


async def main():
    port = 8080
    # async with websockets.serve(serve, "192.168.137.197", port):
    async with websockets.serve(serve, "localhost", port):
        print(f"Server started at ws://localhost:{port}")
        await asyncio.Future()  # run forever

if __name__ == "__main__":
    asyncio.run(main())

