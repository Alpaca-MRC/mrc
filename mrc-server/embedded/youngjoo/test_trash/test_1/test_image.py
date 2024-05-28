import socket
from picamera2.encoders import JpegEncoder

# 서버 주소와 포트
SERVER_IP = '192.168.137.1'  # 서버의 IP 주소
SERVER_PORT = 25001       # 사용할 포트 번호

# 이미지 파일 읽기
with open('test.jpeg', 'rb') as file:
    image_data = file.read()

# 소켓 생성 및 서버에 연결
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((SERVER_IP, SERVER_PORT))
print('connect')

encoder = JpegEncoder()
buffer = image_data.capture_buffer()
# 이미지 데이터 전송
size_info = str(len(buffer)).encode("UTF-8")
print(size_info)
client_socket.sendall(size_info)

client_socket.sendall(image_data)

# 소켓 연결 종료
client_socket.close()
print('disconnected')
