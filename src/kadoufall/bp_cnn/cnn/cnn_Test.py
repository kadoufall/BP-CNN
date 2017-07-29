# coding=utf-8
import os
import os.path
import numpy as np
from keras.models import Sequential
from keras.models import model_from_json
from PIL import Image

num = int(raw_input("Input the num:\n"))

data = np.empty((num, 1, 28, 28), dtype="float32")
files = os.listdir("./testletterCNN")
files.sort(key=lambda x: int(x[:-4]))

# num = len(files)
print 'Test samples num:', num

for i in range(num):
    img = Image.open("./testletterCNN" + '\\' + files[i])
    arr = np.asarray(img, dtype="float32")
    data[i, :, :, :] = arr

json_file = open('model.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
loaded_model = model_from_json(loaded_model_json)
# load weights into new model
loaded_model.load_weights("model.h5")
print("Loaded model from disk")

# evaluate loaded model on test data
# sgd = SGD(lr=0.05, decay=1e-6, momentum=0.9, nesterov=True)
# loaded_model.compile(loss='categorical_crossentropy', optimizer=sgd,metrics=['accuracy'])

tem = loaded_model.predict_classes(data, batch_size=32, verbose=0)
print 'Predict finish!'

outFile = open('./dataset_image/LetterCNN[14302010049].txt', 'w')

for i in range(num):
    if tem[i] == 0:
        outFile.write('A' + '\n')
    elif tem[i] == 1:
        outFile.write('B' + '\n')
    elif tem[i] == 2:
        outFile.write('C' + '\n')
    elif tem[i] == 3:
        outFile.write('D' + '\n')
    elif tem[i] == 4:
        outFile.write('E' + '\n')
    elif tem[i] == 5:
        outFile.write('F' + '\n')
    elif tem[i] == 6:
        outFile.write('G' + '\n')
    elif tem[i] == 7:
        outFile.write('H' + '\n')
outFile.close()
