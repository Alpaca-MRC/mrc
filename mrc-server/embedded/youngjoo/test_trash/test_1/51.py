import socket
import sys
import base64
import numpy
from picamera2 import Picamera2
from datetime import datetime
import time

class ClientSocket:
    def __init__(self, ip, port):
        self.TCP_SERVER_IP = ip
        self.TCP_SERVER_PORT = port
        self.connectCount = 0
        self.connectServer()

    def connectServer(self):
        try:
            self.sock = socket.socket()
            self.sock.connect((self.TCP_SERVER_IP, self.TCP_SERVER_PORT))
            print('Client socket is connected with Server socket [ TCP_SERVER_IP: ' + self.TCP_SERVER_IP + ', TCP_SERVER_PORT: ' + str(self.TCP_SERVER_PORT) + ' ]')
            self.connectCount = 0
            self.sendImages()
        except Exception as e:
            print(e)
            self.connectCount += 1
            if self.connectCount == 10:
                print('Connect fail %d times. exit program'%(self.connectCount))
                sys.exit()
            print('%d times try to connect with server'%(self.connectCount))
            self.connectServer()

    def sendImages(self):
        cnt = 0
        picam2 = Picamera2()
        # Configure for low latency
        config = picam2.create_preview_configuration(main={"size": (480, 315)})
        picam2.configure(config)
        picam2.start()  # 카메라 시작

        try:
            while True:
                # 이미지를 메모리로 바로 캡처
                buffer = picam2.capture_array()
                encoded_string = base64.b64encode(buffer)

                stime = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S.%f')
                length = str(len(encoded_string))
                self.sock.sendall(length.encode('utf-8').ljust(64))
                self.sock.send(encoded_string)
                self.sock.send(stime.encode('utf-8').ljust(64))
                print('send images %d' % cnt)
                cnt += 1
                time.sleep(0.095)
        except Exception as e:
            print(e)
        finally:
            self.sock.close()
            picam2.stop()  # 카메라 종료
            time.sleep(1)
            self.connectServer()
            self.sendImages()

def main():
    TCP_IP = '192.168.137.1'
    TCP_PORT = 25001
    client = ClientSocket(TCP_IP, TCP_PORT)

if __name__ == "__main__":
    main()
