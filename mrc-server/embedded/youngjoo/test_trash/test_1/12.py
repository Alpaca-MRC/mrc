import io
import socket
import time
from picamera2 import Picamera2
import numpy as np
from PIL import Image

host, port = "70.12.247.147", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))

try:
    picam2 = Picamera2()
    preview_config = picam2.create_preview_configuration()
    picam2.configure(preview_config)
    picam2.start()

    while True:
        time.sleep(0.5)  # Wait for 0.5 seconds

        request = picam2.capture_request()
        request.wait()  # Wait for image capture
        frame = request.get_array()  # Get the image data as a numpy array

        if frame is not None:
            img = Image.fromarray(frame)  # Convert numpy array to PIL Image
            with io.BytesIO() as img_buffer:
                img.save(img_buffer, format='JPEG')  # Save image as JPEG to in-memory buffer
                img_data = img_buffer.getvalue()  # Retrieve byte data from buffer

                # Here, you could include the length of the data before sending it,
                # so the receiver knows how much data to expect.
                # Example: sock.sendall(len(img_data).to_bytes(4, 'big'))
                sock.sendall(img_data)  # Send the encoded image data

                receivedData = sock.recv(1024).decode("UTF-8")  # Receive the response from the server
                print(receivedData)
finally:
    sock.close()
