package kadoufall.bp_cnn.cnn.letter;

/**
 * 给定文件夹名，将文件夹下图片灰度化、二值化，最后写入到同文件夹下all.txt中
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class ProduceTextFiles_test {
    public static void main(String[] args) throws Exception {
        ProduceTextFiles_test produceTextFiles = new ProduceTextFiles_test();
        String inputDirectoryPath = "src/dataset_image/train/B";
        produceTextFiles.writeGrey(inputDirectoryPath);

        //System.out.println(getLineCountOfFile("src/dataset_image/train/B/all.txt"));
    }

    /**
     * 原方法：该方法读入一个文件夹，文件夹名为字母，对文件夹中的每个图片文件转成灰度值写入文本文件
     * 修改后：读入一个文件夹，将每个图片文件转为灰度值后进行二值化，然后全部写入一个all.txt中
     *
     * @param directoryPath
     * @throws Exception
     */
    public void writeGrey(String directoryPath) throws Exception {
        //String outputFilePath = filePath.replace(".png", ".txt");
        String outputFilePath = directoryPath + "/all.txt";
        File outputFile = new File(outputFilePath);
        FileOutputStream fos = new FileOutputStream(outputFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

        File directory = new File(directoryPath);
        File[] imageFiles = directory.listFiles();
        for (File imageFile : imageFiles) {
            writeGrey(imageFile, directoryPath, osw);
        }
        osw.close();
    }

    /**
     * 原方法：文件名后缀为.png，该方法读入图片文件，将灰度值写入文本文件
     * 修改后：读入图片，进行灰度化、二值化，将值写到文本文件中
     *
     * @param file
     * @param directoryPath
     * @param osw
     * @throws Exception
     */
    public void writeGrey(File file, String directoryPath, OutputStreamWriter osw) throws Exception {
        // String letter = directoryPath.charAt(directoryPath.length() - 1) + "";
        String filePath = file.getPath();
        File imageFile = new File(filePath);

        if (imageFile.getName().endsWith(".txt")) {
            return;
        }

        double[] re = OtsuBinarize.getInput(filePath);
        for (int i = 0; i < re.length; i++) {
            osw.write(re[i] + " ");
        }

        osw.write("\r\n");
        // osw.write(letter);
        // osw.close();
    }


    /**
     * 给定文件，获取当前文件的行数
     *
     * @param filePath
     * @return
     */
    private static int getLineCountOfFile(String filePath) {
        int lineCount = 0;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                ++lineCount;
            }
        } catch (Exception e) {
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return lineCount;
    }
}