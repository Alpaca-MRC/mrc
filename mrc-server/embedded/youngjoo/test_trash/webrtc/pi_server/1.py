# coding=utf-8
print("Start program")

import time
import struct
import io
import socket
from picamera2 import Picamera2

# 클라이언트가 접속하여 영상을 받아 볼 서버 소켓을 만들어줍니다.
socket_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket_server.bind(('192.168.137.197', 1234))
socket_server.listen(10)

while True:
    socket_client = None
    try:
        print("Waiting client...")
        socket_client = socket_server.accept()[0].makefile('rb')
        print("====[ Client connected ]========================================")
        print("|                                                              |")
        print("| Waiting camera connection...                                |")
        picam2 = Picamera2()
        # 카메라 설정을 위한 프리뷰 설정
        preview_config = picam2.create_preview_configuration(main={"size": (320, 240)})
        picam2.configure(preview_config)
        # 카메라 워밍업
        picam2.start()
        print("| [OK] Camera connected                                        |")
        print("| start streaming                                              |")
        
        while True:
            # 스트림 준비
            stream = io.BytesIO()
            # 이미지 캡처
            picam2.capture_file(stream, format='jpeg')
            # 스트림의 크기 전송
            stream_length = stream.tell()
            socket_client.write(struct.pack(">I", stream_length))
            # 스트림 포지션 초기화 및 읽기
            stream.seek(0)
            socket_client.write(stream.read())
            socket_client.flush()
            # 스트림 초기화
            stream.seek(0)
            stream.truncate()

    except KeyboardInterrupt:
        # 만약 터미널에서 Ctrl+C 가 눌린다면, 서버 소켓을 닫고, 루프를 벗어납니다.
        print("Closing server...")
        socket_server.close()
        print("Server closed")
        print("Exit process.\n")
        break
    except socket.error:
        # 만약 클라이언트에서 연결이 끊긴다면, 연결을 종료해줍니다.
        print("|                                                              |")
        print("====[ Client disconnected ]=====================================")
        print("\n")
