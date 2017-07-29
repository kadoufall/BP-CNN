package kadoufall.bp_cnn.bp.lcdd;

/**
 * 在testbp/lcdd.txt中读取测试集，在dtestbp/Lcdd[14302010049].txt中输出识别后的结果
 */

import kadoufall.bp_cnn.bp.BPNN;

import java.io.*;
import java.util.Scanner;

public class LCDD_Test {
    private static BPNN bpnn;

    public static void main(String[] args) {
        bpnn = new BPNN(7, 18, 10);
        bpnn.initializeFile("src/lcdd_weight/input_hidden.txt", "src/lcdd_weight/hidden_output.txt", "src/lcdd_weight/hidden_bias.txt", "src/lcdd_weight/output_bias.txt");

        // 读取输入
        System.out.println("------------------------\n请输入测试的行数");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        int num = Integer.parseInt(in);
        double[][] trainSet = new double[num][7];

        File file = new File("src/testbp/lcdd.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
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

        // 识别
        String content = "";
        for (int i = 0; i < num; i++) {
            content += getOutput(bpnn.predict(trainSet[i])) + "\r\n";
        }

        // 输出
        file = new File("src/testbp/Lcdd[14302010049].txt");
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

}
