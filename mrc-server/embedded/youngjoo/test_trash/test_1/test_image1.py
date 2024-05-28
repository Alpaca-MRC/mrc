import socket
from picamera2 import Picamera2
from io import BytesIO

# 서버 주소와 포트
SERVER_IP = '192.168.137.1'  # 서버의 IP 주소
SERVER_PORT = 25001       # 사용할 포트 번호

# 카메라 설정
picam2 = Picamera2()
config = picam2.create_preview_configuration()
picam2.configure(config)

# 이미지 캡처 및 인코딩
stream = BytesIO()
picam2.start()
picam2.capture_file(stream, format='jpeg')
image_data = stream.getvalue()
picam2.stop()

# 소켓 생성 및 서버에 연결
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((SERVER_IP, SERVER_PORT))
print('Connected')

# 이미지 데이터 전송
size_info = str(len(image_data)).encode("utf-8")
client_socket.sendall(size_info)
client_socket.sendall(image_data)
print('Image sent')

# 소켓 연결 종료
client_socket.close()
print('Disconnected')
