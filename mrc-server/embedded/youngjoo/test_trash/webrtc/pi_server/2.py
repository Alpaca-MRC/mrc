import picamera2
from picamera2 import Picamera2
import threading
import subprocess
import io
import socket

# 클라이언트가 접속하여 영상을 받아 볼 서버 소켓을 만들어줍니다.
socket_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket_server.bind(('192.168.137.197', 1234))
socket_server.listen(10)

while True:
    socket_client = None
    try:
        print("Waiting client...")
        socket_client = socket_server.accept()[0].makefile('rb')
        print("====[ Client connected ]========================================")
        print("|                                                              |")
        print("| Waiting camera connection...                                |")
        # 카메라 설정
        picam2 = Picamera2()
        config = picam2.create_video_configuration(main={"size": (640, 480)})
        picam2.configure(config)

        # 스트리밍 스레드
        def stream_video():
            # FFmpeg을 사용하여 실시간 스트리밍
            subprocess.run(["ffmpeg", "-f", "video4linux2", "-i", "/dev/video0", "-c", "copy", "-f", "rtsp", "rtsp://192.168.137.197:8554/stream"])

        # 스트리밍 실행
        stream_thread = threading.Thread(target=stream_video)
        stream_thread.start()
        stream_thread.join()
    except KeyboardInterrupt:
        # 만약 터미널에서 Ctrl+C 가 눌린다면, 서버 소켓을 닫고, 루프를 벗어납니다.
        print("Closing server...")
        socket_server.close()
        print("Server closed")
        print("Exit process.\n")
        break
    except socket.error:
        # 만약 클라이언트에서 연결이 끊긴다면, 연결을 종료해줍니다.
        print("|                                                              |")
        print("====[ Client disconnected ]=====================================")
        print("\n")
