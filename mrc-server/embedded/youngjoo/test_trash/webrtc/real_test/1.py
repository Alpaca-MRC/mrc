from picamera2 import Picamera2
import io
import socket
import struct
import time

# Camera setup
picam2 = Picamera2()
picam2.resolution = (1920, 1080)
picam2.framerate = 30
preview_config = picam2.create_video_configuration({"size": (1920, 1080)})
picam2.configure(preview_config)
picam2.start()
time.sleep(1)

# Socket setup
server_socket = socket.socket()
server_socket.bind(('192.168.137.197', 9090)) 
server_socket.listen(10)
print('서버 시작, 연결을 기다립니다...')

connection = server_socket.accept()[0].makefile('wb')
print('클라이언트가 연결되었습니다.')

try:
    while True:
        stream = io.BytesIO()
        picam2.capture_file(stream, format='jpeg')
        
        stream.seek(0)
        image_len = len(stream.getbuffer().tobytes())

        if image_len == 0:
            break
        stream.seek(0)
        connection.write(struct.pack('<L', image_len))
        connection.write(stream.read())
        time.sleep(0.05)
finally:
    # 전송이 완료되었음을 알리기 위해 길이 0의 메시지를 전송
    connection.write(struct.pack('<L', 0))
    connection.close()
    server_socket.close()
    picam2.stop()
    print('서버가 종료되었습니다.')
