import socket
import os

def send_image(image_path, server_ip, server_port):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((server_ip, server_port))
        with open(image_path, 'rb') as img_file:
            image_data = img_file.read()
        
        # 이미지 데이터의 크기를 먼저 전송
        s.sendall(len(image_data).to_bytes(4, byteorder='big'))
        # 이미지 데이터 전송
        s.sendall(image_data)

if __name__ == "__main__":
    image_path = 'test.jpeg'
    server_ip = '70.12.247.147'  # 유니티 서버 IP
    server_port = 8080  # 유니티 서버 포트
    send_image(image_path, server_ip, server_port)
