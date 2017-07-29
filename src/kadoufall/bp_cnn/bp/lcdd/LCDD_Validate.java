package kadoufall.bp_cnn.bp.lcdd;

/**
 * 验证
 * 在testbp/lcdd_ValidateSet.txt中读取测试集，在testbp/Lcdd_Validate.txt中输出识别后的结果
 */

import kadoufall.bp_cnn.bp.BPNN;

import java.io.*;

public class LCDD_Validate {
    private static BPNN bpnn;

    public static void main(String[] args) {
        bpnn = new BPNN(7, 18, 10);
        bpnn.initializeFile("src/lcdd_weight/input_hidden.txt", "src/lcdd_weight/hidden_output.txt", "src/lcdd_weight/hidden_bias.txt", "src/lcdd_weight/output_bias.txt");

        // 读取输入
        double[][] trainSet = new double[20][7];
        File file = new File("src/testbp/lcdd_ValidateSet.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String[] tem = tempString.split(" ");
                for (int i = 0; i < 7; i++) {
                    trainSet[line][i] = Integer.parseInt(tem[i]);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        // 设置目标
        double[][] destination = new double[20][10];
        for (int i = 0; i < destination.length; i++) {
            destination[i] = getDestination(i);
        }

        bpnn.setTrainSetAndDestination(trainSet, destination);
        // 计算总误差
        double totaError = bpnn.predictAll();

        // 识别
        String content = "";
        for (int i = 0; i < 20; i++) {
            content += getOutput(bpnn.predict(trainSet[i])) + "\r\n";
        }
        content += "均方误差" + totaError;

        // 输出
        file = new File("src/testbp/Lcdd_Validate.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 输入一个数组，返回识别的数字
     *
     * @param input
     * @return A--H
     */
    private static int getOutput(double[] input) {
        int re = 0;
        double tem = input[0];
        for (int i = 1; i < input.length; i++) {
            if (input[i] > tem) {
                tem = input[i];
                re = i;
            }
        }

        return re;
    }

    /**
     * 验证集，1-10行为0-9，11-20行：1 2 2 2 4 5 6 6 8 9 的残缺部分
     *
     * @param num
     * @return
     */
    private static double[] getDestination(int num) {
        double[] re = new double[10];
        if (num == 0) {
            double[] tem = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 1) {
            double[] tem = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 2) {
            double[] tem = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 3) {
            double[] tem = {0, 0, 0, 1, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 4) {
            double[] tem = {0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 5) {
            double[] tem = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
            re = tem;
        } else if (num == 6) {
            double[] tem = {0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
            re = tem;
        } else if (num == 7) {
            double[] tem = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
            re = tem;
        } else if (num == 8) {
            double[] tem = {0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
            re = tem;
        } else if (num == 9) {
            double[] tem = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
            re = tem;
        } else if (num == 10) {
            double[] tem = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 11) {
            double[] tem = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 12) {
            double[] tem = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 13) {
            double[] tem = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 14) {
            double[] tem = {0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
            re = tem;
        } else if (num == 15) {
            double[] tem = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
            re = tem;
        } else if (num == 16) {
            double[] tem = {0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
            re = tem;
        } else if (num == 17) {
            double[] tem = {0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
            re = tem;
        } else if (num == 18) {
            double[] tem = {0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
            re = tem;
        } else if (num == 19) {
            double[] tem = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
            re = tem;
        }

        return re;
    }

}

