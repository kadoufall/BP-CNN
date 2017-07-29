package kadoufall.bp_cnn.bp.sine;

/**
 * 用于测试，读取src/testbp/sine.txt，输出src/testbp/Sine[14302010049].txt
 */

import java.io.*;
import java.util.Scanner;

public class Sine_Test {
    private static BPNN_Sine bpnn;

    public static void main(String[] args) {
        bpnn = new BPNN_Sine(1, 5, 1);
        bpnn.initializeFile("src/sine_weight/input_hidden.txt", "src/sine_weight/hidden_output.txt", "src/sine_weight/hidden_bias.txt", "src/sine_weight/output_bias.txt");

        // 读取输入
        System.out.println("------------------------\n请输入测试的行数");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        int num = Integer.parseInt(in);
        double[] trainSet = new double[num];

        File file = new File("src/testbp/sine.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                trainSet[line] = Double.parseDouble(tempString);
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

        // 识别
        String content = "";
        double[] tem = new double[1];
        for (int i = 0; i < num; i++) {
            tem[0] = trainSet[i];
            content += bpnn.predict(tem)[0] + "\r\n";
            //System.out.println(Math.sin(tem[0]));
        }

        // 输出
        file = new File("src/testbp/Sine[14302010049].txt");
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
