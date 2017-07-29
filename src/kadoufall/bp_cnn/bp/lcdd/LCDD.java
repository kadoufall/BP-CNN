package kadoufall.bp_cnn.bp.lcdd;

/**
 * 从lcdd.txt中读取训练集，训练后将参数输出到lcdd_weight下各个文件中
 */
import kadoufall.bp_cnn.bp.BPNN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LCDD {
    private static BPNN bpnn;

    public static void main(String[] args) {
        double[][] trainSet = new double[10][7];

        // 读取训练集
        File file = new File("src/lcdd.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] tem = tempString.split(" ");
                for (int i=0;i<7;i++){
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

        bpnn = new BPNN(7,18,10);

        // 训练
        for(int i=0;i<20000000;i++){
            int choice = (int)(Math.random()*10);
            double re = bpnn.train(trainSet[choice],getDestination(choice));
            //System.out.println(choice+":  "+re);
        }

        // 输出权重
        bpnn.print("src/lcdd_weight/input_hidden.txt","src/lcdd_weight/hidden_output.txt","src/lcdd_weight/hidden_bias.txt","src/lcdd_weight/output_bias.txt");

    }

    /**
     * 给定数字，返回数组表达
     * @param num
     * @return
     */
    private static double[] getDestination(int num){
        double[] re = new double[10];
        if(num==0){
            double[] tem = {1,0,0,0,0,0,0,0,0,0};
            re = tem;
        }else if(num==1){
            double[] tem = {0,1,0,0,0,0,0,0,0,0};
            re = tem;
        }else if(num==2){
            double[] tem = {0,0,1,0,0,0,0,0,0,0};
            re = tem;
        }else if(num==3){
            double[] tem = {0,0,0,1,0,0,0,0,0,0};
            re = tem;
        }else if(num==4){
            double[] tem = {0,0,0,0,1,0,0,0,0,0};
            re = tem;
        }else if(num==5){
            double[] tem = {0,0,0,0,0,1,0,0,0,0};
            re = tem;
        }else if(num==6){
            double[] tem = {0,0,0,0,0,0,1,0,0,0};
            re = tem;
        }else if(num==7){
            double[] tem = {0,0,0,0,0,0,0,1,0,0};
            re = tem;
        }else if(num==8){
            double[] tem = {0,0,0,0,0,0,0,0,1,0};
            re = tem;
        }else if(num==9){
            double[] tem = {0,0,0,0,0,0,0,0,0,1};
            re = tem;
        }

        return re;
    }

}
