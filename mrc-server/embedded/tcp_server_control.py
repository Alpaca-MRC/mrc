import serial
import threading
import time
import RPi.GPIO as GPIO
import queue
import socket

data_queue = queue.Queue()  # Create a queue to store received data
direction_queue = queue.Queue()
main_queue = queue.Queue()

serverSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serverSock.bind(('', 25001))
serverSock.listen(1)
print("waiting connect...")

running_thread = True

# # set timeout 2 min
# serverSock.settimeout(120)

def angle_to_percent(angle):
    if angle > 180 or angle < 0:
        return False

    start = 2.5
    end = 12.5
    ratio = (end - start) / 180

    angle_as_percent = angle * ratio

    return start + angle_as_percent


def serial_thread():
    global running_thread
    global serverSock

    # set clientSocket
    connectionSock, addr = serverSock.accept()
    print("Connect accept.")
    connectionSock.send('hi, i\'m RCcar'.encode('utf-8'))

    try:
        while running_thread is True:
            # a/b/c = -4 <= a <= 4, a = left, right, b= go, c = back
            receiveddata = connectionSock.recv(1024).decode('utf-8').strip()

            print(receiveddata)

            receivedlis = list(receiveddata.split("_"))

            for i in range(len(receivedlis) - 1):
                if receivedlis[i] == "close":
                    print("client close socket")
                    # close this socket
                    time.sleep(1)
                    print("waiting connect...")
                    connectionSock, addr = serverSock.accept()
                    print("Connect accept.")
                    connectionSock.send('hi, i\'m RCcar'.encode('utf-8'))
                    break

                else:
                    print(receivedlis[i])
                    commands = list(map(int, receivedlis[i].split("/")))
                    direction_queue.put(commands[0])
                    data_queue.put((commands[1], commands[2]))
                    print('Received Data:', commands)
                    print(commands[0], commands[1], commands[2])

    except Exception as e:
        print(e)
        running_thread = False

    finally:
        # connectionSock.send('close'.encode('utf-8'))
        connectionSock.close()
        print("socket thread over")


def control_speed(pwm, in1, in2, speed, state):
    if state == 1:
        GPIO.output(in1, HIGH)
        GPIO.output(in2, LOW)
        pwm.ChangeDutyCycle(speed)

    elif state == -1:
        GPIO.output(in1, LOW)
        GPIO.output(in2, HIGH)
        pwm.ChangeDutyCycle(speed)


# PIN input output set
OUTPUT = 1
INPUT = 0

# PIN set
HIGH = 1
LOW = 0

# set DC driver motor pin
DCin1 = 19
DCin2 = 26
ENA = 13

# servo motor's pin set
servo_pin = 18

GPIO.setmode(GPIO.BCM)
GPIO.setup(DCin1, GPIO.OUT)
GPIO.setup(DCin2, GPIO.OUT)
GPIO.setup(ENA, GPIO.OUT)
GPIO.output(DCin1, GPIO.LOW)
GPIO.output(DCin2, GPIO.LOW)
DCpwm = GPIO.PWM(ENA, 300)
DCpwm.start(0)

GPIO.setup(servo_pin, GPIO.OUT)
servoPwm = GPIO.PWM(servo_pin, 50)
servoPwm.start(angle_to_percent(90))


def main_speed():
    now_speed = 0
    now_dc = (0, 0)
    try:
        while running_thread is True:
            if not data_queue.empty():  # Check if data is present in the queue
                data = data_queue.get()  # Get data from the queue
                if now_dc != data:
                    now_dc = data

            # if now_dc is (1, 0), go straight and speed up
            if now_dc == (1, 0):
                # if motor stopped, run Rccar
                # for skip audible frequency, set speed 45
                if now_speed == 0:
                    now_speed = 45
                # limit max speed 80
                elif 45 <= now_speed < 80:
                    now_speed += 2
                    if now_speed > 80:
                        now_speed = 80
                # if Rccar go backward, speed down
                # if reach audible frequency, stop rc car
                elif now_speed < 0:
                    now_speed += 5
                    if now_speed > -45:
                        now_speed = 0

            # if now_dc is back, go reverse and speed up
            elif now_dc == (0, 1):
                # if motor stopped, run Rccar
                # for skip audible frequency, set speed 45
                if 0 >= now_speed > -45:
                    now_speed = -45

                # if rc car go straight, speed down
                # if reach audible frequency, stop rc car
                elif 45 <= now_speed <= 80:
                    now_speed -= 15
                    if now_speed < 45:
                        now_speed = 0

                # limit max speed 80
                elif -80 < now_speed <= -45:
                    now_speed -= 5
                    if now_speed < -80:
                        now_speed = -80

            # if not give any move command, go to stop
            elif now_dc == (0, 0):
                now_speed = 0
                # if now_speed > 0:
                #     now_speed -= 10
                #     if now_speed < 45:
                #         now_speed = 0
                #
                # elif now_speed < 0:
                #     now_speed += 10
                #     if now_speed > -45:
                #         now_speed = 0

            # according to speed, call control speed func
            if now_speed >= 0:
                control_speed(DCpwm, DCin1, DCin2, abs(now_speed), 1)
            else:
                control_speed(DCpwm, DCin1, DCin2, abs(now_speed), -1)

    # except KeyboardInterrupt:
    #     print("maintainer off server")

    finally:
        print("DC thread over")


def main_direction():
    called = False
    now_servo = 0
    try:
        while running_thread is True:
            if not direction_queue.empty():  # Check if data is present in the queue
                data = direction_queue.get()  # Get data from the queue
                if now_servo != data:
                    called = False
                    now_servo = data

            if now_servo != 0 and called is False:
                servoPwm.ChangeDutyCycle(angle_to_percent(90 + (7.5 * now_servo)))
                time.sleep(0.01)
                called = True

            elif now_servo == 0 and called is False:
                servoPwm.ChangeDutyCycle(angle_to_percent(90))
                time.sleep(0.01)
                called = True

    finally:
        print("servo thread over")


if __name__ == '__main__':
    task1 = threading.Thread(target=serial_thread)
    task2 = threading.Thread(target=main_direction)
    task3 = threading.Thread(target=main_speed)
    task1.daemon = True  # task1 set daemon
    task2.daemon = True  # task2 set daemon
    task3.daemon = True  # task3 set daemon
    task1.start()
    task2.start()
    task3.start()

    try:
        while True:
            if not main_queue.empty():
                time.sleep(0.1)  # wait for main_thread doesn't over
            else:
                message = main_queue.get()
                if message == "close":
                    print("use close")
                    break

    except KeyboardInterrupt:
        print("KeyboardInterrupt: Stopping threads...")

    finally:
        running_thread = False
        task1.join()
        task2.join()
        task3.join()
        servoPwm.stop()
        DCpwm.stop()
        GPIO.cleanup()  # thread over, gpio cleanup
        print("everything is over")
