# coding=utf-8
import os
import os.path
from PIL import Image
import numpy as np


# 读取目录下的所有文件，包括嵌套的文件夹
def GetFileList(dir, fileList):
    newDir = dir
    if os.path.isfile(dir):
        fileList.append(dir)
    elif os.path.isdir(dir):
        for s in os.listdir(dir):
            # 忽略all.txt
            if s == "all.txt":
                continue
            newDir = os.path.join(dir, s)
            GetFileList(newDir, fileList)
    return fileList


# 根据文件路径分割出文件名
def getName(dir):
    tem = os.path.basename(dir)
    index = tem.find("_", 0)
    return tem[index + 1:index + 2]


# 根据文件夹路径和文件数，生成并返回numpy数组
def load_data(dir, count):
    data = np.empty((count, 1, 28, 28), dtype="float32")
    label = np.empty((count,), dtype="int")
    fileDir = dir
    list = GetFileList(fileDir, [])
    num = len(list)
    print num
    for i in range(num):
        img = Image.open(list[i])
        arr = np.asarray(img, dtype="float32")
        data[i, :, :, :] = arr
        if getName(list[i]) == 'A':
            label[i] = 0
        elif getName(list[i]) == 'B':
            label[i] = 1
        elif getName(list[i]) == 'C':
            label[i] = 2
        elif getName(list[i]) == 'D':
            label[i] = 3
        elif getName(list[i]) == 'E':
            label[i] = 4
        elif getName(list[i]) == 'F':
            label[i] = 5
        elif getName(list[i]) == 'G':
            label[i] = 6
        elif getName(list[i]) == 'H':
            label[i] = 7

            # print label[i]
    return data, label
