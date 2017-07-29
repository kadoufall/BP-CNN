package kadoufall.bp_cnn.cnn.letter;

/**
 * 在testLetter中读取测试集，在dataset_image/LetterBP_Validate.txt中输出识别后的结果
 */

import kadoufall.bp_cnn.bp.BPNN;

import java.io.*;
import java.util.ArrayList;

public class Letter_Validate {
    private static BPNN bpnn;
    private static ArrayList<double[]> input = new ArrayList<>();
    private static ArrayList<String> destination = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //初始化BP网络
        bpnn = new BPNN(28 * 28, 50, 8);
        bpnn.initializeFile("src/letter_weight/input_hidden.txt", "src/letter_weight/hidden_output.txt", "src/letter_weight/hidden_bias.txt", "src/letter_weight/output_bias.txt");

        addInput();

        int temNum = 0;
        // 识别
        String content = "";
        for (int i = 0; i < input.size(); i++) {
            String tem = getOutput(bpnn.predict(input.get(i)));
            content += tem + "\r\n";
            if (tem.equals(destination.get(i))) {
                temNum++;
            }
        }

        System.out.println("All" + input.size());
        System.out.println("Right" + temNum);

        // 输出
        File file = new File("src/dataset_image/LetterBP_Validate.txt");
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
     * @throws IOException
     */
    private static void addInput() throws IOException {
        for (int i = 0; i < 8; i++) {
            File directory = new File("src/dataset_image/validation/C");
            if (i == 0) {
                directory = new File("src/dataset_image/validation/A");
            } else if (i == 1) {
                directory = new File("src/dataset_image/validation/B");
            } else if (i == 2) {
                directory = new File("src/dataset_image/validation/C");
            } else if (i == 3) {
                directory = new File("src/dataset_image/validation/D");
            } else if (i == 4) {
                directory = new File("src/dataset_image/validation/E");
            } else if (i == 5) {
                directory = new File("src/dataset_image/validation/F");
            } else if (i == 6) {
                directory = new File("src/dataset_image/validation/G");
            } else if (i == 7) {
                directory = new File("src/dataset_image/validation/H");
            }
            File[] imageFiles = directory.listFiles();
            for (File imageFile : imageFiles) {
                if (imageFile.getName().endsWith("txt")) {
                    continue;
                }
                double[] re = OtsuBinarize.getInput(imageFile.getPath());
                input.add(re);
                destination.add(numToLetter(i));
            }
        }

    }

    /**
     * 输入一个数字，返回识别的字母
     *
     * @param num
     * @return
     */
    private static String numToLetter(int num) {
        String re = "";
        switch (num) {
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
