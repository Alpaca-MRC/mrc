import cv2
import struct
import time
import socket

client_socket = socket.socket()
client_socket.connect(('70.12.247.147', 8000))

connection = client_socket.makefile('wb')

camera = cv2.VideoCapture(0)  # 카메라 열기 (카메라 번호: 0)

try:
    # 카메라 해상도 설정
    camera.set(cv2.CAP_PROP_FRAME_WIDTH, 500)
    camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

    time.sleep(2)  # 카메라 시작 시간 대기

    start = time.time()
    while True:
        ret, frame = camera.read()  # 프레임 읽기
        if not ret:
            break

        # 이미지 데이터를 바이트로 인코딩
        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 90]  # JPEG 품질 설정 (0-100)
        _, img_encode = cv2.imencode('.jpg', frame, encode_param)
        img_bytes = img_encode.tobytes()

        # 이미지 길이를 서버에 전송
        connection.write(struct.pack('<L', len(img_bytes)))
        connection.flush()

        # 이미지 데이터를 서버에 전송
        connection.write(img_bytes)
        
        # 60초가 지나면 종료
        if time.time() - start > 60:
            break
finally:
    # 연결 종료
    connection.close()
    client_socket.close()
