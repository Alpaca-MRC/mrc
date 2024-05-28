import io
import time
import socket
from picamera2 import Picamera2
import numpy as np
from PIL import Image
from picamera2.encoders import JpegEncoder

host, port = "192.168.190.166", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))
print('connect')
try:
    picam2 = Picamera2()
    picam2.start_preview()
    video_config = picam2.create_video_configuration({"size": (640,480)})
    picam2.configure(video_config)
    encoder = JpegEncoder()
    print('camera start')
    picam2.start_recording(encoder, 'ahah.jpeg')

    # Configure camera settings if necessary
    # picam2.configure(picam2.create_preview_configuration())

    try:
        while True:

            time.sleep(0.5)
            print('start capture')
            buffer = picam2.capture_buffer()

            # 전송할 데이터 크기를 먼저 보냅니다.
            size_info = str(len(buffer)).encode("UTF-8")
            print(size_info)
            sock.sendall(size_info)

            # 데이터를 전송합니다.
            sock.sendall(buffer)
            receivedData = sock.recv(1024).decode("UTF-8")  
            # print(receivedData)
    finally:
        picam2.stop_recording()

finally:
    sock.close()
