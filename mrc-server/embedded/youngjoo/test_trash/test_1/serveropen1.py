import asyncio
import asyncws
import json

@asyncio.coroutine
def echo():
    websocket = yield from asyncws.connect('ws://192.168.137.197:8000/')
    yield from websocket.send(json.dumps(        {'id': '1', 'type': 'subscribe_events', 'event_type': 'state_changed'}))
    while True:
            message = yield from websocket.recv()
            if message is None:
                break
            print(message)
    asyncio.get_event_loop().run_until_complete(echo())
