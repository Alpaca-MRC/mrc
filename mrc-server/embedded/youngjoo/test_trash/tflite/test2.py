from tflite_runtime.interpreter import Interpreter
import numpy as np
import time
from picamera2 import Picamera2
import cv2
from PIL import Image, ImageDraw, ImageFont
import datetime

def load_labels(path): # Read the labels from the text file as a Python list.
    with open(path, 'r') as f:
        return [line.strip() for i, line in enumerate(f.readlines())]

def set_input_tensor(interpreter, image):
    tensor_index = interpreter.get_input_details()[0]['index']
    input_tensor = interpreter.tensor(tensor_index)()[0]
    input_tensor[:, :] = image

def classify_image(interpreter, image, top_k=1):
    set_input_tensor(interpreter, image)
    interpreter.invoke()
    output_details = interpreter.get_output_details()[0]
    output = np.squeeze(interpreter.get_tensor(output_details['index']))
    scale, zero_point = output_details['quantization']
    output = scale * (output - zero_point)
    ordered = np.argpartition(-output, 1)
    return [(i, output[i]) for i in ordered[:top_k]][0]

data_folder = "/home/ssafy/tflite/"
model_path = data_folder + "model.tflite"
label_path = data_folder + "labels.txt"

interpreter = Interpreter(model_path)
interpreter.allocate_tensors()

camera = Picamera2()
camera.resolution = (224, 224)
camera.framerate = 32

for frame in camera.capture_continuous(rawCapture, format="rgb", use_video_port=True):
    image = np.empty((224, 224, 3), dtype=np.uint8)
    frame.array.copy_to(image)

    cv2.imshow("Frame", image)
  
    label_id, prob = classify_image(interpreter, image)
    labels = load_labels(label_path)
    classification_label = labels[label_id]
    print("Result: ", label_id, ", Accuracy: ", np.round(prob*100, 2), "%. At: ", datetime.datetime.now())
       
    if label_id==1:
        print("Yannis where are you?")
        
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
        
camera.close()
cv2.destroyAllWindows()
