import socket
import struct
import io
import threading
import sys
import tkinter as tk
from PIL import Image, ImageTk

class ImageClient:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.root = tk.Tk()
        self.root.title("Image Viewer")

        self.label = tk.Label(self.root)
        self.label.pack()

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((self.host, self.port))
        print('start receiving')
        threading.Thread(target=self.receive_image).start()

    def receive_image(self):
        while True:
            try:
                # 프레임 크기를 수신
                image_size_bytes = self.socket.recv(4)
                if not image_size_bytes:
                    break
                image_size = struct.unpack('>I', image_size_bytes)[0]

                # 이미지 데이터를 수신하고 PIL Image로 변환
                image_data = b''
                while len(image_data) < image_size:
                    chunk = self.socket.recv(min(image_size - len(image_data), 1024))
                    if not chunk:
                        break
                    image_data += chunk
                image_stream = io.BytesIO(image_data)
                image = Image.open(image_stream)

                # 이미지를 표시
                photo = ImageTk.PhotoImage(image)
                self.label.config(image=photo)
                self.label.image = photo
                self.root.update()

            except Exception as e:
                print("Error receiving image:", e)
                break

    def run(self):
        self.root.mainloop()

if __name__ == "__main__":
    host = "192.168.137.197"
    port = 1234
    client = ImageClient(host, port)
    client.run()
