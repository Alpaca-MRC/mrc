import struct
import time
import picamera
import io
import socket

client_socket = socket.socket()

client_socket.connect(('70.12.247.147', 8000))

connection = client_socket.makefile('wb')

try:
    camera = picamera.Picamera()
    camera.vflip= True
    camera.resolution = (500, 480)
    camera.start_preview()
    time.sleep(2)

    start = time.time()
    stream = io.BytesIO()
    for foo in camera.capture_continuous(stream, 'jpeg'):
        stream.seek(0)
        connection.write(struct.pack('<L', stream.tell()))
        connection.flush()
        connection.write(stream.read())
        if time.time() - start > 60:
            break
        stream.seek(0)
        stream.truncate()
    connection.write(struct.pack('<L', 0))
finally:
    connection.close()
    client_socket.close()