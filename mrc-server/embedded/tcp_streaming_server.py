# TCP SERVER

from picamera2 import Picamera2
import io
import socket
import struct
import time

# camera setting
res=(1280, 720)
framerate=60
picam2 = Picamera2()
config = picam2.create_video_configuration({"size": res},lores={"size": res},controls={"FrameRate": framerate})
picam2.align_configuration(config)
picam2.configure(config)


# Socket setup
server_socket = socket.socket()
server_socket.bind(('', 8080)) 
server_socket.listen(10)
# print('server start waiting client')

while True:
    try:
        connection, addr = server_socket.accept()
        # print('client connected')
        picam2.start()
        time.sleep(1)
        connection_file = connection.makefile('wb')
        try:
            # print('start to send data')
            while True:
       
                stream = io.BytesIO()
                picam2.capture_file(stream, format='jpeg')
                
                stream.seek(0)
                image_len = len(stream.getbuffer().tobytes())

                if image_len == 0:
                    break
                stream.seek(0)
                connection_file.write(struct.pack('<L', image_len))
                try:
                    connection_file.write(stream.read())
                except:
                    # print('file not sended')
                    break
                stream.truncate()    
                time.sleep(0.05)
        finally:
            # print('client disconnected')
            # connection_file.close()
            connection.close()
            picam2.stop()
    except KeyboardInterrupt:
        # print('server_closed')
        server_socket.close()
        picam2.stop()
        break
