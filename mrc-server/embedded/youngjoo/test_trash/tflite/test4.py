from tflite_runtime.interpreter import Interpreter
import numpy as np
import cv2
from picamera2 import Picamera2, Preview
from libcamera import Transform

def load_labels(path): 
    with open(path, 'r') as f:
        return [line.strip() for i, line in enumerate(f.readlines())]

def set_input_tensor(interpreter, image):
    tensor_index = interpreter.get_input_details()[0]['index']
    input_tensor = interpreter.tensor(tensor_index)()[0]
    # print(f'input_tensor : {input_tensor}')
    # input_tensor[:, :] = image
    input_tensor[:, :] = np.expand_dims(image, axis=-1)

def classify_image(interpreter, image, top_k=1):
    set_input_tensor(interpreter, image)
    interpreter.invoke()
    output_details = interpreter.get_output_details()[0]
    output = np.squeeze(interpreter.get_tensor(output_details['index']))
    scale, zero_point = output_details['quantization']
    output = scale * (output - zero_point)
    ordered = np.argpartition(-output, top_k)
    # print(f'output_details : {output_details}, ordered: {ordered}')
    print(f'output: {output}')
    return [(i, output[i]) for i in ordered[:top_k]][0]

data_folder = "/home/ssafy/tflite/"
model_path = data_folder + "model_2.tflite"
label_path = data_folder + "labels_2.txt"

interpreter = Interpreter(model_path)
interpreter.allocate_tensors()
_, height, width, _ = interpreter.get_input_details()[0]['shape']

# Picamera2 설정
picam2 = Picamera2()
print('start')
picam2.start_preview(Preview.QT)
config = picam2.create_preview_configuration(main={"size": (640, 480)},
                                             lores={"size": (320, 240), "format": "YUV420"})# config.transform = Transform(hflip=True, vflip=False)  # 필요한 경우 변환 적용
picam2.configure(config)
picam2.start()

try:
    while True:
        # 이미지 캡처
        buffer = picam2.capture_buffer("lores")
        image = cv2.resize(buffer, (224, 224))  

        label_id, prob = classify_image(interpreter, image)
        labels = load_labels(label_path)

        classification_label = labels[label_id]

        print(f"Result: {label_id}, Accuracy: {np.round(prob*100, 2)}%.")

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
finally:
    picam2.stop()
    cv2.destroyAllWindows()
