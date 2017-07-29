package kadoufall.bp_cnn.bp.sine;

/**
 * BP神经网络结构，for 正弦函数拟合
 */

import java.io.*;

public class BPNN_Sine {
    // 默认的输入层、隐藏层、输出层的个数，默认学习率，上一次学习率和误差和
    private int inputNum = 1;
    private int hiddenNum = 18;
    private int outputNum = 10;
    private double rate = 0.1;
    // 权重和偏置数组
    private double[][] inputHiddenWeight;
    private double[][] hiddenOutputWeight;
    private double[] hiddenBias;
    private double[] outputBias;
    // 训练集和目标
    private double[][] trainSet = null;
    private double[][] destination = null;

    /**
     * 默认随机初始化权重和偏置数组
     *
     * @param inputNum
     * @param hiddenNum
     * @param outputNum
     */
    public BPNN_Sine(int inputNum, int hiddenNum, int outputNum) {
        this.inputNum = inputNum;
        this.hiddenNum = hiddenNum;
        this.outputNum = outputNum;
        inputHiddenWeight = new double[inputNum][hiddenNum];
        hiddenOutputWeight = new double[hiddenNum][outputNum];
        hiddenBias = new double[hiddenNum];
        outputBias = new double[outputNum];
        this.initializeRandom();
    }

    /**
     * 随机初始化权重和偏置数组
     */
    private void initializeRandom() {
        for (int i = 0; i < inputNum; i++) {
            for (int j = 0; j < hiddenNum; j++) {
                inputHiddenWeight[i][j] = (Math.random() * 2 - 1) / inputNum;
            }
        }

        for (int i = 0; i < hiddenNum; i++) {
            for (int j = 0; j < outputNum; j++) {
                hiddenOutputWeight[i][j] = (Math.random() * 2 - 1) / hiddenNum;
            }
        }

        for (int i = 0; i < hiddenNum; i++) {
            hiddenBias[i] = Math.random() - 0.2;
        }

        for (int i = 0; i < outputNum; i++) {
            outputBias[i] = Math.random() * 0.2;
        }
    }

    /**
     * 从训练后的数据中初始化权重和偏置数组
     *
     * @param input_hidden
     * @param hidden_output
     * @param hidden_bias
     * @param output_bias
     */
    public void initializeFile(String input_hidden, String hidden_output, String hidden_bias, String output_bias) {
        try {
            File file = new File(input_hidden);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            for (int i = 0; i < inputNum; i++) {
                for (int j = 0; j < hiddenNum; j++) {
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        inputHiddenWeight[i][j] = Double.parseDouble(tempString);
                        break;
                    }
                }
            }
            reader.close();

            tempString = null;
            file = new File(hidden_output);
            BufferedReader reader1 = new BufferedReader(new FileReader(file));
            for (int i = 0; i < hiddenNum; i++) {
                for (int j = 0; j < outputNum; j++) {
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader1.readLine()) != null) {
                        hiddenOutputWeight[i][j] = Double.parseDouble(tempString);
                        break;
                    }
                }
            }
            reader1.close();

            tempString = null;
            file = new File(hidden_bias);
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            for (int i = 0; i < hiddenNum; i++) {
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader2.readLine()) != null) {
                    hiddenBias[i] = Double.parseDouble(tempString);
                    break;
                }
            }
            reader2.close();

            tempString = null;
            file = new File(output_bias);
            BufferedReader reader3 = new BufferedReader(new FileReader(file));
            for (int i = 0; i < outputNum; i++) {
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader3.readLine()) != null) {
                    outputBias[i] = Double.parseDouble(tempString);
                    break;
                }
            }
            reader3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行预测,返回预测后的数组
     *
     * @param inputValue
     * @return
     */
    public double[] predict(double[] inputValue) {
        double[] hiddenValue = new double[hiddenNum];
        double[] outputValue = new double[outputNum];

        for (int i = 0; i < hiddenNum; i++) {
            double tempSum = 0;
            for (int j = 0; j < inputNum; j++) {
                tempSum += inputValue[j] * inputHiddenWeight[j][i];
            }
            tempSum += hiddenBias[i];
            hiddenValue[i] = sigmoid(tempSum);
        }

        for (int i = 0; i < outputNum; i++) {
            double tempSum = 0;
            for (int j = 0; j < hiddenNum; j++) {
                tempSum += hiddenValue[j] * hiddenOutputWeight[j][i];
            }
            tempSum += outputBias[i];
            outputValue[i] = sigmoid(tempSum);
        }

        return outputValue;
    }

    /**
     * 对于训练集的所有样本，计算并返回当前参数下的误差和，这里误差为均方误差
     *
     * @return
     */
    public double predictAll() {
        double totalError = 0;
        for (int i = 0; i < trainSet.length; i++) {
            double[] predict = predict(trainSet[i]);        // 对于每一个训练样本，根据当前参数得到一个输出
            for (int j = 0; j < predict.length; j++) {
                totalError += (destination[i][j] - predict[j]) * (destination[i][j] - predict[j]);    //计算当前训练样本在当前训练参数下的误差
            }
        }
        return totalError / trainSet.length;
    }

    /**
     * 进行训练，返回训练前的误差
     */
    public double train(double[] inputValue, double[] destination) {
        double totalError = 0;

        double[] hiddenValue = new double[hiddenNum];
        double[] outputValue = new double[outputNum];

        // 预测
        for (int i = 0; i < hiddenNum; i++) {
            double tempSum = 0;
            for (int j = 0; j < inputNum; j++) {
                tempSum += inputValue[j] * inputHiddenWeight[j][i];
            }
            tempSum += hiddenBias[i];
            hiddenValue[i] = sigmoid(tempSum);
        }

        for (int i = 0; i < outputNum; i++) {
            double tempSum = 0;
            for (int j = 0; j < hiddenNum; j++) {
                tempSum += hiddenValue[j] * hiddenOutputWeight[j][i];
            }
            tempSum += outputBias[i];
            outputValue[i] = sigmoid(tempSum);
        }

        // 调整权重与偏置
        for (int i = 0; i < hiddenNum; i++) {
            for (int j = 0; j < outputNum; j++) {
                double delta = rate * (destination[j] - outputValue[j]) * (1 - outputValue[j] * outputValue[j]) * hiddenValue[i];
                hiddenOutputWeight[i][j] += delta;
            }
        }

        for (int i = 0; i < inputNum; i++) {
            for (int j = 0; j < hiddenNum; j++) {
                double temp = 0;
                for (int k = 0; k < outputNum; k++) {
                    temp += hiddenOutputWeight[j][k] * (1 - outputValue[k] * outputValue[k]) * (destination[k] - outputValue[k]);
                }
                double delta = rate * (1 - hiddenValue[j] * hiddenValue[j]) * inputValue[i] * temp;
                inputHiddenWeight[i][j] += delta;
            }
        }

        for (int i = 0; i < outputNum; i++) {
            double delta = rate * (destination[i] - outputValue[i]) * (1 - outputValue[i] * outputValue[i]);
            outputBias[i] += delta;
        }

        for (int i = 0; i < hiddenNum; i++) {
            double temp = 0;
            for (int k = 0; k < outputNum; k++) {
                temp += hiddenOutputWeight[i][k] * (1 - outputValue[k] * outputValue[k]) * (destination[k] - outputValue[k]);
            }
            double delta = rate * (1 - hiddenValue[i] * hiddenValue[i]) * temp;
            hiddenBias[i] += delta;
        }

        for (int i = 0; i < outputNum; i++) {
            totalError += (destination[i] - outputValue[i]) * (destination[i] - outputValue[i]);
        }
        totalError = totalError / 2;

/*
        //调整学习速率
        double errorAfter = predictAll();   // 调整后的总误差
        if (errorAfter<errorBefore){
            rate = 1.05 * rate;
        }else if (errorAfter>1.04*errorBefore){
            rate = 0.7*rate;
        }
*/
        return totalError;
    }

    /**
     * 设置总的训练集和目标
     *
     * @param trainSet
     * @param destination
     */
    public void setTrainSetAndDestination(double[][] trainSet, double[][] destination) {
        this.trainSet = trainSet;
        this.destination = destination;
    }

    /**
     * sigmoid函数
     *
     * @param x
     * @return sigmoid(x)
     */
    private double sigmoid(double x) {
        return (Math.pow(Math.E, x) - Math.pow(Math.E, -x)) / (Math.pow(Math.E, x) + Math.pow(Math.E, -x));
    }

    /**
     * 将参数输出到指定文件中
     *
     * @param input_hidden
     * @param hidden_output
     * @param hidden_bias
     * @param output_bias
     * @return
     */
    public boolean print(String input_hidden, String hidden_output, String hidden_bias, String output_bias) {
        boolean re = true;

        // 输出
        try {
            File outFile1 = new File(input_hidden);
            FileWriter fileWriter = new FileWriter(outFile1);
            for (int i = 0; i < inputNum; i++) {
                for (int j = 0; j < hiddenNum; j++) {
                    fileWriter.write(String.valueOf(inputHiddenWeight[i][j]));
                    fileWriter.write("\r\n");
                }
            }
            fileWriter.close();

            outFile1 = new File(hidden_output);
            FileWriter fileWriter1 = new FileWriter(outFile1);
            for (int i = 0; i < hiddenNum; i++) {
                for (int j = 0; j < outputNum; j++) {
                    fileWriter1.write(String.valueOf(hiddenOutputWeight[i][j]));
                    fileWriter1.write("\r\n");
                }
            }
            fileWriter1.close();

            outFile1 = new File(hidden_bias);
            FileWriter fileWriter2 = new FileWriter(outFile1);
            for (int i = 0; i < hiddenNum; i++) {
                fileWriter2.write(String.valueOf(hiddenBias[i]));
                fileWriter2.write("\r\n");
            }
            fileWriter2.close();

            outFile1 = new File(output_bias);
            FileWriter fileWriter3 = new FileWriter(outFile1);
            for (int i = 0; i < outputNum; i++) {
                fileWriter3.write(String.valueOf(outputBias[i]));
                fileWriter3.write("\r\n");
            }
            fileWriter3.close();
        } catch (IOException e) {
            re = false;
            e.printStackTrace();
        }

        return re;
    }


    public void changeRate(double rate) {
        this.rate = rate;
    }

}

