package kadoufall.bp_cnn.cnn.letter;

/**
 * 利用Otsu算法对图像进行二值化
 * 对外开放getInput接口，读取一个图片，返回二值化后的数组
 * 借鉴了Bostjan Cigan方法(http://zerocool.is-a-geek.net)
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OtsuBinarize {
    private static BufferedImage original, grayscale, binarized;

    /* //测试
    public static void main(String[] args) throws IOException {
        File original_f = new File("src/dataset_image/train/B/Train_B193.png");
        String output_f = "src/dataset_image/train/B/Train_B193"+"_bin";
        original = ImageIO.read(original_f);
        grayscale = toGray(original);
        binarized = binarize(grayscale);
        double [] tem = getInput("src/dataset_image/train/B/Train_B193.png");
    }
    */

    /**
     * 开放接口，返回二值化的数组
     *
     * @param src
     * @return
     * @throws IOException
     */
    public static double[] getInput(String src) throws IOException {
        File original_f = new File(src);
        original = ImageIO.read(original_f);
        grayscale = toGray(original);
        binarized = binarize(grayscale);

        int width = binarized.getWidth();
        int height = binarized.getHeight();

        double[] re = new double[width * height];

        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = binarized.getRGB(i, j);
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                int grey = (red * 299 + green * 587 + blue * 114) / 1000;
                re[index] = grey;
                if (grey == 255) {
                    re[index] = 1;
                }
                index++;
            }
        }

        return re;
    }

    /**
     * 返回灰度图的直方图
     *
     * @param input
     * @return
     */
    public static int[] imageHistogram(BufferedImage input) {
        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) histogram[i] = 0;

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }

        return histogram;
    }

    /**
     * 将rgb转为灰度值
     *
     * @param original
     * @return
     */
    private static BufferedImage toGray(BufferedImage original) {
        int alpha, red, green, blue;
        int newPixel;
        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
                // Write pixels into image
                lum.setRGB(i, j, newPixel);
            }
        }

        return lum;

    }

    /**
     * 用Otsu法获得二分的阈值
     *
     * @param original
     * @return
     */
    private static int otsuTreshold(BufferedImage original) {
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    /**
     * 二分
     *
     * @param original
     * @return
     */
    private static BufferedImage binarize(BufferedImage original) {
        int red;
        int newPixel;
        int threshold = otsuTreshold(original);
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);
            }
        }

        return binarized;
    }

    /**
     * 将R,G,B.Alpha转为标准的8 bit
     *
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }

}