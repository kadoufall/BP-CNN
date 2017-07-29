# coding=utf-8

# 导入各种用到的模块组件
import os
import numpy as np
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation, Flatten
from keras.layers.advanced_activations import PReLU
from keras.layers.convolutional import Convolution2D, MaxPooling2D, ZeroPadding2D
from keras.optimizers import SGD, Adadelta, Adagrad
from keras.utils import np_utils, generic_utils
import numpy as np
from PIL import Image
from keras import backend as k

k.set_image_dim_ordering('th')
from data import load_data


# 建立CNN模型，生成一个model
def __CNN__(testdata, testlabel, traindata, trainlabel):
    model = Sequential()

    # 第一个卷积层，6个卷积核，每个卷积核大小5*5。1表示输入的图片的通道,灰度图为1通道。
    # 采用maxpooling，poolsize为(2,2)
    model.add(Convolution2D(6, 5, 5, border_mode='valid', input_shape=(1, 28, 28)))
    model.add(Activation('tanh'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    # 第二个卷积层，12个卷积核，每个卷积核大小5*5。
    # 采用maxpooling，poolsize为(2,2)
    model.add(Convolution2D(12, 5, 5))
    model.add(Activation('tanh'))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.25))

    # 全连接层
    model.add(Flatten())
    model.add(Dense(128))
    model.add(Activation('tanh'))
    model.add(Dropout(0.5))

    # Softmax分类，输出是8类别
    model.add(Dense(8))
    model.add(Activation('softmax'))

    # 开训练模型
    # 使用SGD + momentum冲量
    sgd = SGD(lr=0.02, decay=1e-6, momentum=0.9, nesterov=True)
    # model.compile里的参数loss就是损失函数(目标函数)
    model.compile(loss='categorical_crossentropy', optimizer=sgd, metrics=['accuracy'])
    # 开始训练，batch_size是每次带入训练的样本数目 ， nb_epoch 是迭代次数，  shuffle 是打乱样本随机。
    model.fit(traindata, trainlabel, batch_size=128, nb_epoch=120, shuffle=True, verbose=1,
              validation_data=(testdata, testlabel))
    # 设置测试评估参数，用测试集样本
    # model.evaluate(testdata, testlabel, batch_size=16,verbose=1)

    # serialize model to JSON
    model_json = model.to_json()
    with open("model.json", "w") as json_file:
        json_file.write(model_json)
    # serialize weights to HDF5
    model.save_weights("model.h5")
    print("Saved model to disk")


# 主模块
traindata, trainlabel = load_data("./dataset_image/train", 7780)
testdata, testlabel = load_data("./dataset_image/validation", 3200)

# label为A~H共8个类别，keras要求格式为binary class matrices,转化一下，直接调用keras提供的这个函数
testlabel = np_utils.to_categorical(testlabel, 8)
trainlabel = np_utils.to_categorical(trainlabel, 8)

__CNN__(testdata, testlabel, traindata, trainlabel)
