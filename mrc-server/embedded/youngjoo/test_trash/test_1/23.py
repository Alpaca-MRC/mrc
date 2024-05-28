import io
import time
import socket
from picamera2 import Picamera2
from picamera2.encoders import JpegEncoder

host, port = "70.12.247.147", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))
print('connect')

try:
    picam2 = Picamera2()
    video_config = picam2.create_video_configuration({"size": (1280, 720)})
    picam2.configure(video_config)
    encoder = JpegEncoder()
    picam2.start()
    print('camera start')

    try:
        while True:
            time.sleep(0.5)
            
            # JPEG 이미지를 캡처합니다.
            buffer = picam2.capture_buffer('main')  # Adjust according to your buffer format
            
            # 전송할 데이터 크기를 먼저 보냅니다.
            size_info = len(buffer).to_bytes(4, byteorder='big')
            sock.sendall(size_info)

            # 데이터를 전송합니다.
            sock.sendall(buffer)

            # 응답을 받습니다.
            receivedData = sock.recv(1024).decode("UTF-8")
            print(receivedData)

    finally:
        picam2.stop()
        print('camera stop')

finally:
    sock.close()
    print('socket closed')