package kadoufall.bp_cnn.bp.sine;

/**
 * 验证
 * 在testbp/Sine_ValidateSet.txt中读取测试集，在dtestbp/Sine_Validate.txt中输出识别后的结果
 */

import java.io.*;

public class Sine_Validate {
    private static BPNN_Sine bpnn;

    public static void main(String[] args) {
        bpnn = new BPNN_Sine(1, 5, 1);
        bpnn.initializeFile("src/sine_weight/input_hidden.txt", "src/sine_weight/hidden_output.txt", "src/sine_weight/hidden_bias.txt", "src/sine_weight/output_bias.txt");

        // 读取输入
        double[][] trainSet = new double[127][1];
        File file = new File("src/testbp/Sine_ValidateSet.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                trainSet[line][0] = Double.parseDouble(tempString);
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
        double[][] destination = new double[127][1];
        for (int i = 0; i < destination.length; i++) {
            destination[i][0] = Math.sin(trainSet[i][0]);
        }

        bpnn.setTrainSetAndDestination(trainSet, destination);
        // 计算总误差
        double totaError = bpnn.predictAll();

        // 识别
        String content = "";
        for (int i = 0; i < 127; i++) {
            content += "预测值:" + bpnn.predict(trainSet[i])[0] + "    实际值:" + destination[i][0] + "\r\n";
        }
        content += "均方误差" + totaError;

        // 输出
        file = new File("src/testbp/Sine_Validate.txt");
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

}
