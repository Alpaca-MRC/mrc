import io
import time
import socket
from picamera2 import Picamera2
import numpy as np
from PIL import Image
from picamera2.encoders import JpegEncoder
import base64

host, port = "70.12.247.147", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))
print('connect')
try:
    picam2 = Picamera2()
    picam2.start_preview()
    video_config = picam2.create_video_configuration({"size": (1280, 720)})
    picam2.configure(video_config)
    encoder = JpegEncoder()  # JPEG 인코더 생성
    print('camera start')
    picam2.start_recording(encoder, 'ahah.jpeg')  # JPEG로 녹화 시작

    try:
        while True:
            time.sleep(0.5)
            print('start capture')
            stream = io.BytesIO()  # 바이트 스트림 생성
            picam2.capture_image(stream, 'jpeg')  # JPEG 형식으로 이미지 캡처
            stream.seek(0)  # 스트림 위치를 처음으로 이동

            # 이미지 데이터 읽기
            image_data = stream.read()
            stream.close()  # 스트림 닫기

            print('get frame')

            # 이미지 데이터를 Base64로 인코딩
            base64_image = base64.b64encode(image_data)

            # 전송할 데이터 크기를 먼저 보냅니다.
            size_info = str(len(base64_image)).encode("UTF-8")
            sock.sendall(size_info)

            # 데이터를 전송합니다.
            sock.sendall(base64_image)

            receivedData = sock.recv(1024).decode("UTF-8")  
            print(receivedData)
    finally:
        picam2.stop_recording()  # 녹화 중지

finally:
    sock.close()
