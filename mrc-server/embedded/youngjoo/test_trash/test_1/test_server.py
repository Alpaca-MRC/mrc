import asyncio
import logging
import os
import websockets
from protobuf import OculusControllerState_pb2  # Replace 'protobuf' with the actual protobuf package

# Servo configuration. Change these values based on your servo's verified safe
# minimum and maximum values.
PULSE_MIN_US = 500
PULSE_MAX_US = 1000

async def main():
    port = int(os.getenv("PORT", 9080))
    logging.basicConfig(level=logging.INFO)

    async def client_connection(websocket, path):
        logging.info(f"establishing client connection... {websocket}")
        async for message in websocket:
            try:
                oculus = OculusControllerState_pb2.OculusControllerState()
                oculus.ParseFromString(message)
                stick = oculus.secondary_thumbstick.x
                servo = (PULSE_MIN_US +
                         ((stick + 1) * 50 * (PULSE_MAX_US - PULSE_MIN_US) / 100))

                # Do something with servo value...
                logging.info(f"Servo: {servo}")
            except Exception as e:
                logging.error(f"Error parsing message: {e}")

    start_server = websockets.serve(client_connection, "192.168.137.197", port)

    async with start_server:
        logging.info(f"Starting server on port {port}")
        await start_server.serve_forever()

asyncio.run(main())
