package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class FixedThresholdFilter implements PixelFilter {

    public FixedThresholdFilter() {

    }

    @Override
    public DImage processImage(DImage img) {
        short[][] gridOld = img.getBWPixelGrid();
        short[][] grid = crop(gridOld, 0, 0, 600, 600);
        ArrayList<String> answerList = new ArrayList<>();
        int ansCol = 0;
        for (int r = 108; r < 226; r+=50) {
            double minAvg = 255;
            for (int c = 105; c < 231; c+=25){
                //find the avg
                double sum = 0;
                for (int i = 0; i < 20; i++){
                    for (int j = 0; j < 20; j++) {
                        sum += grid[r + i][c + j];
                    }
                }
                double avgBW = sum/400;
                if(avgBW<minAvg){
                    minAvg = avgBW;
                    ansCol = c;
                }
            }

            if(ansCol==105){
                answerList.add("A");
            }else if(ansCol == 130){
                answerList.add("B");
            }else if(ansCol == 155){
                answerList.add("C");
            }else if(ansCol == 180){
                answerList.add("D");
            }else{
                answerList.add("E");
            }
        }
        print(answerList);
        //LATER WE CAN PRINT THIS TO A CSV FILE INSTEAD OF PRINTING IT
        img.setPixels(grid);
        return img;
    }
    private static void print(ArrayList<String> arr){
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
        }
    }
    private static short[][] crop(short[][] img, int startR, int startC, int endR, int endC){
        short[][] newImg = new short[endR-startR+1][endC-startC+1];
        for (int r = 0; r <= endR-startR; r++) {
            for (int c = 0; c <= endC-startC; c++) {
                newImg[r][c] = img[r+startR][c+startC];
            }
        }
        return newImg;
    }
}

