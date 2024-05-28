from picamera2 import Picamera2
import io
import socket
import struct
import time
import cv2

# Camera setup
picam2 = Picamera2()
preview_config = picam2.create_video_configuration({"size": (640,480)})
picam2.configure(preview_config)
picam2.start()
time.sleep(2)

# Socket setup
server_socket = socket.socket()
server_socket.bind(('192.168.137.197', 9090))  # Raspberry Pi의 IP 주소와 포트 번호
server_socket.listen(10)
print('서버 시작, 연결을 기다립니다...')

connection = server_socket.accept()[0].makefile('wb')
print('클라이언트가 연결되었습니다.')

try:
    while True:
        # 이미지 파일로 캡처
        picam2.capture_file("temp_image.jpeg")

        # 파일을 열어서 이미지 데이터 읽기
        image = cv2.imread('temp_image.jpeg')

        # 이미지를 JPEG 형식으로 인코딩
        _, encoded_image = cv2.imencode('.jpg', image)
        image_bytes = encoded_image.tobytes()

        # 이미지 데이터의 길이를 구하고 전송
        image_len = len(image_bytes)
        print(image_len)
        if image_len == 0:
            print('이미지 캡처 실패, 종료합니다.')
            break
        # 클라이언트에게 이미지 길이를 먼저 전송
        connection.write(struct.pack('<L', image_len))
        # 이미지 데이터 전송
        connection.write(image_bytes)
        connection.flush()
        print('이미지 전송 완료!')

        # 여기에 딜레이를 추가하여 캡처 빈도를 조절할 수 있습니다.
        time.sleep(1)  # 필요에 따라 슬립 시간 조정
finally:
    # 전송이 완료되었음을 알리기 위해 길이 0의 메시지를 전송
    connection.write(struct.pack('<L', 0))
    connection.close()
    server_socket.close()
    picam2.stop()
    print('서버가 종료되었습니다.')
