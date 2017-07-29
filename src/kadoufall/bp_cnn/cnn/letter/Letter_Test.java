package kadoufall.bp_cnn.cnn.letter;

/**
 * 在testLetter中读取测试集，在dataset_image/LetterBP[14302010049].txt中输出识别后的结果
 */

import kadoufall.bp_cnn.bp.BPNN;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Letter_Test {
    private static BPNN bpnn;
    private static ArrayList<double[]> input = new ArrayList<double[]>();

    public static void main(String[] args) throws IOException {
        //初始化BP网络
        bpnn = new BPNN(28 * 28, 50, 8);
        bpnn.initializeFile("src/letter_weight/input_hidden.txt", "src/letter_weight/hidden_output.txt", "src/letter_weight/hidden_bias.txt", "src/letter_weight/output_bias.txt");

        // 获取输入
        System.out.println("------------------------\n请输入");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        int num = Integer.parseInt(in);
        addInput(num);

        // 识别
        String content = "";
        for (int i = 0; i < input.size(); i++) {
            String tem = getOutput(bpnn.predict(input.get(i)));
            content += tem + "\r\n";
        }

        // 输出
        File file = new File("src/dataset_image/LetterBP[14302010049].txt");
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
     * 从testletter中读取测试集，并添加到输入数组中
     *
     * @param num
     * @throws IOException
     */
    private static void addInput(int num) throws IOException {
        File directory = new File("src/testletterBP");
        File[] imageFiles = directory.listFiles();

        Arrays.sort(imageFiles, new Comparator<File>() {
            public int compare(File f1, File f2) {
                String s1 = f1.getName();
                String s2 = f2.getName();
                s1 = s1.substring(0, s1.indexOf('.'));//去除后缀
                s2 = s2.substring(0, s2.indexOf('.'));
                return Integer.parseInt(s1) - Integer.parseInt(s2);
            }
        });

        int count = 0;
        for (File imageFile : imageFiles) {
            if (imageFile.getName().endsWith(".txt")) {
                continue;
            }
            double[] re = OtsuBinarize.getInput(imageFile.getPath());
            input.add(re);
            count++;
            if (count == num) {
                break;
            }
        }

    }

    /**
     * 输入一个数组，返回识别的字母
     *
     * @param input
     * @return A--H
     */
    private static String getOutput(double[] input) {
        String re = "";
        int where = 0;
        double tem = input[0];
        for (int i = 1; i < input.length; i++) {
            if (input[i] > tem) {
                tem = input[i];
                where = i;
            }
        }

        switch (where) {
            case 0:
                re = "A";
                break;
            case 1:
                re = "B";
                break;
            case 2:
                re = "C";
                break;
            case 3:
                re = "D";
                break;
            case 4:
                re = "E";
                break;
            case 5:
                re = "F";
                break;
            case 6:
                re = "G";
                break;
            case 7:
                re = "H";
                break;
        }

        return re;
    }


}
