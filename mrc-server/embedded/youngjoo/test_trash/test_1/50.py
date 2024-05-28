import numpy as np
import base64
import socket
import sys
import time
from datetime import datetime
from picamera2 import Picamera2
from picamera2.encoders import H264Encoder, MJPEGEncoder

class ClientSocket:
    def __init__(self, ip, port):
        self.TCP_SERVER_IP = ip
        self.TCP_SERVER_PORT = port
        self.connectCount = 0
        self.connectServer()

    def closeConnections(self):
        # 카메라 정지
        if hasattr(self, 'picam2') and self.picam2:
            self.picam2.stop()
            print('카메라 종료')
        
        # 소켓 연결 종료
        if hasattr(self, 'sock') and self.sock:
            self.sock.close()
            print('소켓 종료')


    def connectServer(self):
        try:
            self.sock = socket.socket()
            self.sock.connect((self.TCP_SERVER_IP, self.TCP_SERVER_PORT))
            print('서버 연결 성공 [ TCP_SERVER_IP: ' + self.TCP_SERVER_IP + ', TCP_SERVER_PORT: ' + str(self.TCP_SERVER_PORT) + ' ]')
            self.connectCount = 0
            self.sendImages()
        except Exception as e:
            print(e)
            self.connectCount += 1
            if self.connectCount == 10:
                print('마니 잘못됐따' )
                self.closeConnections()
                sys.exit()
            print(f'{self.connectCount} 번째 연결 안되는중')
            self.connectServer()

    def sendImages(self):
        cnt = 0

        picam2 = Picamera2()

        config = picam2.create_video_configuration(main={"size": (480, 320)})
        picam2.configure(config)
        picam2.start()
        time.sleep(2)
        print('카메라 연결 성공!!!')

        try:
            while True:
         
                encoder = MJPEGEncoder(bitrate=20000)
                buffer = picam2.capture_buffer(encoder)
                image_data = np.frombuffer(buffer, dtype=np.uint8)

               

                stringData = base64.b64encode(image_data)
                length = str(len(stringData))
                self.sock.sendall(length.encode('utf-8').ljust(64))
                self.sock.send(stringData)
               
                print('이ㅁ미지 전송 완료 %d' % (cnt))
                cnt += 1
                time.sleep(0.095)
        # except Exception as e:
        #     print("전송에서 에러가 생긴듯"+e)
        #     self.sock.close()
        #     time.sleep(1)
        #     self.connectServer()'
        finally:
            self.closeConnections()

def main():
    TCP_IP = '70.12.247.147'
    TCP_PORT = 25001
    client = ClientSocket(TCP_IP, TCP_PORT)

if __name__ == "__main__":
    main()
