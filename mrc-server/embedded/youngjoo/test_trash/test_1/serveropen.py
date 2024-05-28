# import asyncio
# import websockets
# from io import BytesIO
# from PIL import Image

# async def receive_video():
#     async with websockets.connect("ws://192.168.137.197:8000/") as websocket:
#         try:
#             while True:
#                 frame = await websocket.recv()
#                 process_frame(frame)
#                 print('connected!!!')
#         except websockets.exceptions.ConnectionClosedError:
#             print("Connection closed")

import asyncio
import websockets

async def server_handler(websocket, path):
    print("Client connected")
    try:
        while True:
            # 여기에 웹소켓 클라이언트로부터 메시지를 처리하는 코드를 작성합니다.
            # 이 예제에서는 간단히 받은 메시지를 다시 클라이언트에게 전송합니다.
            message = await websocket.recv()
            await websocket.send(message)
    except websockets.exceptions.ConnectionClosedError:
        print("Connection closed")

async def main():
    print('before start')
    server = await websockets.serve(server_handler, "192.168.137.197", 8000)
    await server.wait_closed()

asyncio.run(main())

