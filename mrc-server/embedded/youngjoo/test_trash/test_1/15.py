import io
import time
import socket
from picamera2 import Picamera2
import numpy as np
from PIL import Image

host, port = "70.12.247.147", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))
print('connect')
try:
    picam2 = Picamera2()
    picam2.start_preview()
    video_config = picam2.create_video_configuration({"size": (1280, 720)})
    picam2.configure(video_config)
    print('camera start')
    picam2.start()

    # Configure camera settings if necessary
    # picam2.configure(picam2.create_preview_configuration())

    try:
        while True:

            time.sleep(0.5)
            print('start capture')
            buffer = picam2.capture_buffer()
            print('get frame')
            # request.wait()  # 기본적으로 동기 방식으로 동작합니다.
            # buffer = request.get_buffer()  # memoryview 객체를 반환합니다.

            # 전송할 데이터 크기를 먼저 보냅니다.
            size_info = str(len(buffer)).encode("UTF-8")
            sock.sendall(size_info)

            # 데이터를 전송합니다.
            sock.sendall(buffer)

            receivedData = sock.recv(1024).decode("UTF-8")  
            print(receivedData)
    finally:
        picam2.close()

finally:
    sock.close()
