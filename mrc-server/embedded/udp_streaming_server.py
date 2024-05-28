# UDP SERVER

from picamera2 import Picamera2
import io
import socket
import struct
import time
import threading

# camera setting
res=(1280, 720)
framerate = 60
picam2 = Picamera2()
config = picam2.create_video_configuration({"size": res},lores={"size": res},controls={"FrameRate": framerate})
picam2.align_configuration(config)
picam2.configure(config)


# socket setting
server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ('', 8080)
server_socket.bind(server_address)
print('connecting server')

def send_image_over_udp(image_data, client_address):
    # MAX_PACKET_SIZE = 65507
    MAX_PACKET_SIZE = 50000
    for i in range(0, len(image_data), MAX_PACKET_SIZE):
        part_data = image_data[i:i+MAX_PACKET_SIZE]
        server_socket.sendto(part_data, client_address)

def handle_client(client_address):
    print(f'client connected: {client_address}')
    picam2.start()
    time.sleep(1)
    try:
        while True:
            stream = io.BytesIO()
            picam2.capture_file(stream, format='jpeg')
            
            stream.seek(0)
            image_data = stream.read()
            image_len = len(stream.getbuffer().tobytes())

            if image_len == 0:
                break

            stream.seek(0)
            server_socket.sendto(struct.pack('<L', image_len), client_address)
            send_image_over_udp(image_data, client_address)
            stream.truncate()
    finally:
        picam2.stop()

while True:
    try:
        print('waiting client')
        data, client_address = server_socket.recvfrom(1024)
        message = data.decode('utf-8')

        if message == 'disconnect':
            print(f'Client disconnected : {client_address}')
            continue
        else:
            client_thread = threading.Thread(target=handle_client, args=(client_address,))
            client_thread.start()
    except KeyboardInterrupt:
        picam2.close()
        server_socket.close()
