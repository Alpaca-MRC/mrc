import asyncio
import websockets
import numpy as np
from picamera2 import Picamera2, Preview
import threading
from datetime import datetime, timezone

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
        request.wait()  # 기본적으로 동기 방식으로 동작합니다. 비동기 처리가 필요하다면 적절하게 수정해야 합니다.
        frame = request.get_array()  # 이제 frame은 numpy 배열입니다.

        if frame is None:
            print("Error: Couldn't read frame")
            break

        # 여기서는 frame을 그대로 bytes로 변환하여 전송합니다. 실제 사용 시에는 압축 또는 포맷 변환을 고려해야 할 수 있습니다.
        await websocket.send(frame.tobytes())

async def server_control(websocket, path):
    await asyncio.gather(client_connection(websocket, path), camera_stream(websocket, path))

async def main():
    port = 8080
    # async with websockets.serve(serve, "localhost", port):
    #     print(f"Server started at ws://localhost:{port}")
        # await asyncio.Future()  # run forever
    server = await websockets.serve(server_control, "192.168.137.197", port)
    print(f"Server started at ws://localhost:{port}")
    await server.wait_closed()
       

if __name__ == "__main__":
    asyncio.run(main())
