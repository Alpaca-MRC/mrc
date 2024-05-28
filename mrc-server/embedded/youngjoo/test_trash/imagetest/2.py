import socket
import cv2

# 이미지 파일을 불러와서 크기 조정
image = cv2.imread('test.jpeg')
resized_image = cv2.resize(image, (640, 480))

# 이미지를 JPEG 형식으로 인코딩
_, encoded_image = cv2.imencode('.jpg', resized_image)

# TCP 소켓 생성 및 서버에 연결
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('70.12.247.147', 8080))

# 인코딩된 이미지를 바이트 배열로 변환하고 전송
image_bytes = encoded_image.tobytes()
client_socket.sendall(image_bytes)

# 소켓 닫기
client_socket.close()
