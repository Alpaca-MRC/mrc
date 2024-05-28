import io
import logging
import asyncio
import websockets
from http import server
from threading import Condition

from picamera2 import Picamera2
from picamera2.encoders import MJPEGEncoder
from picamera2.outputs import FileOutput

PAGE = """\
<html>
<head>
<title>picamera2 MJPEG streaming demo</title>
<script>
  var ws = new WebSocket("ws://" + window.location.hostname + ":8000");
  var image = document.getElementById('image');

  ws.onmessage = function(event) {
    image.src = URL.createObjectURL(event.data);
  };
</script>
</head>
<body>
<h1>Picamera2 MJPEG Streaming Demo</h1>
<img id="image" width="640" height="480" />
</body>
</html>
"""


class StreamingOutput(io.BufferedIOBase):
    def __init__(self):
        self.frame = None
        self.condition = Condition()

    def write(self, buf):
        with self.condition:
            self.frame = buf
            self.condition.notify_all()


async def send_video(websocket, path):
    try:
        while True:
            with output.condition:
                output.condition.wait()
                frame = output.frame
            await websocket.send(frame)
    except Exception as e:
        logging.warning('Removed streaming client: %s', str(e))


picam2 = Picamera2()
picam2.configure(picam2.create_video_configuration(main={"size": (640, 480)}))
output = StreamingOutput()
picam2.start_recording(MJPEGEncoder(), FileOutput(output))

try:
    start_server = websockets.serve(send_video, "localhost", 8000)

    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()
finally:
    picam2.stop_recording()
