import io
import time
import socket
from picamera2 import Picamera2
import numpy as np
from PIL import Image

host, port = "70.12.247.147", 25001
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((host, port))
print('connect')
try:
    picam2 = Picamera2()
    picam2.start_preview()
    print('camera start')

    # Configure camera settings if necessary
    picam2.configure(picam2.create_preview_configuration())

    try:
        while True:
            time.sleep(0.5)  # Give time for the image to be captured

            # Capture an image to a numpy array
            frame = picam2.capture_array()

            if frame is not None:
                # Convert numpy array to PIL Image for easier handling
                img = Image.fromarray(frame)

                # Encode the image as JPEG into a BytesIO buffer
                with io.BytesIO() as buffer:
                    img.save(buffer, format="JPEG")
                    img_data = buffer.getvalue()

                    # Send the size of the image data first
                    sock.sendall(len(img_data).to_bytes(4, 'big'))
                    # Then send the actual image data
                    sock.sendall(img_data)

                    # Receive response from the server
                    receivedData = sock.recv(1024).decode("UTF-8")
                    print(receivedData)
    finally:
        picam2.close()

finally:
    sock.close()
