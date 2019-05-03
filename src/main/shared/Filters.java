package main.shared;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Filters {

    private int[][] mask;

    public void setMask(int[][] mask) {
        this.mask = mask;
    }

    public BufferedImage filterImage(BufferedImage img) {
        BufferedImage copy = deepCopy(img);
        for(int w = 0; w < img.getWidth() ; w++) {
            for(int h = 0; h < img.getHeight() ; h++) {
                copy.setRGB(w, h, calculateNewPixelValue(img, w, h));
            }
        }
        return copy;
    }
    public BufferedImage medianFilter(BufferedImage img,int w_size){
        BufferedImage copy = deepCopy(img);
        for(int w = 0; w < img.getWidth() ; w++) {
            for(int h =0; h < img.getHeight() ; h++) {
                copy.setRGB(w,h,calculateMediana(img,w,h,w_size));
            }
        }
        return copy;
    }
    public BufferedImage kuwaharFilter(BufferedImage image){
        BufferedImage copy = deepCopy(image);
        for(int w = 0; w < image.getWidth() ; w++) {
            for(int h = 0; h < image.getHeight(); h++) {
                copy.setRGB(w,h,calculate_kuwahar(image,w,h));
            }
        }
        return copy;
    }
    private int calculate_kuwahar(BufferedImage img,int w,int h){
        double sigmaR [] = new double[4];
        double sigmaG [] = new double[4];
        double sigmaB [] = new double[4];
        double sredniaR[] = new double[4];
        double sredniaG[] = new double[4];
        double sredniaB[] = new double[4];

        //pierwszy
        //srednia
        for (int i = -2;i<=0;i++){
            for (int j=-2;j<= 0;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sredniaR[0] += c.getRed() / 9.0;
                    sredniaG[0] += c.getGreen() / 9.0;
                    sredniaB[0] += c.getBlue() / 9.0;
                }
            }
        }
        //wariancja
        for (int i = -2;i<=0;i++){
            for (int j=-2;j<= 0;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sigmaR[0] += (Math.pow(c.getRed() - sredniaR[0], 2)) / 9.0;
                    sigmaG[0] += (Math.pow(c.getGreen() - sredniaG[0], 2)) / 9.0;
                    sigmaB[0] += (Math.pow(c.getBlue() - sredniaB[0], 2)) / 9.0;
                }
            }
        }
        //drugi
        for (int i = 0;i<=2;i++){
            for (int j=-2;j<= 0;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sredniaR[1] += c.getRed() / 9.0;
                    sredniaG[1] += c.getGreen() / 9.0;
                    sredniaB[1] += c.getBlue() / 9.0;
                }
            }
        }
        //wariancja
        for (int i = 0;i<=2;i++){
            for (int j=-2;j<= 0;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sigmaR[1] += (Math.pow(c.getRed() - sredniaR[1], 2)) / 9.0;
                    sigmaG[1] += (Math.pow(c.getGreen() - sredniaG[1], 2)) / 9.0;
                    sigmaB[1] += (Math.pow(c.getBlue() - sredniaB[1], 2)) / 9.0;
                }
            }
        }
        //trzeci
        for (int i = -2;i<=0;i++){
            for (int j=0;j<= 2;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sredniaR[2] += c.getRed() / 9.0;
                    sredniaG[2] += c.getGreen() / 9.0;
                    sredniaB[2] += c.getBlue() / 9.0;
                }
            }
        }
        //wariancja
        for (int i = -2;i<=0;i++){
            for (int j=0;j<= 2;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sigmaR[2] += (Math.pow(c.getRed() - sredniaR[2], 2)) / 9.0;
                    sigmaG[2] += (Math.pow(c.getGreen() - sredniaG[2], 2)) / 9.0;
                    sigmaB[2] += (Math.pow(c.getBlue() - sredniaB[2], 2)) / 9.0;
                }
            }
        }
        //czwarty
        for (int i = 0;i<=2;i++){
            for (int j=0;j<= 2;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sredniaR[3] += c.getRed() / 9.0;
                    sredniaG[3] += c.getGreen() / 9.0;
                    sredniaB[3] += c.getBlue() / 9.0;
                }
            }
        }
        //wariancja
        for (int i = 0;i<=2;i++){
            for (int j=0;j<= 2;j++){
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    sigmaR[3] += (Math.pow(c.getRed() - sredniaR[3], 2)) / 9.0;
                    sigmaG[3] += (Math.pow(c.getGreen() - sredniaG[3], 2)) / 9.0;
                    sigmaB[3] += (Math.pow(c.getBlue() - sredniaB[3], 2)) / 9.0;
                }
            }
        }
        //znalezienie najmnieszej wariancji
        int minR,minG,minB;
        minB = minG = minR =0;
        double mR = sigmaR[0];
        double mG = sigmaG[0];
        double mB = sigmaB[0];
        for (int i=1;i<4;i++){
            if (sigmaR[i]<mR) {
                mR = sigmaR[i];
                minR = i;
            }
            if (sigmaG[i]<mG) {
                mG = sigmaG[i];
                minG = i;
            }
            if (sigmaB[i]<mB){
                mB = sigmaB[i];
                minB = i;
            }
        }

        Color c = new Color((int)sredniaR[minR],(int)sredniaG[minG],(int)sredniaB[minB]);
        return c.getRGB();

    }

    private int calculateMediana(BufferedImage img,int w,int h,int w_size){
        int[] tableR= new int[w_size*w_size];
        int[] tableG= new int[w_size*w_size];
        int[] tableB= new int[w_size*w_size];
        int licznik = 0;
        for(int i =-(w_size/2); i <= w_size/2; i++) {
            for(int j = -(w_size/2); j <= w_size/2; j++) {
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {
                    Color c = new Color(img.getRGB(w + i, h + j));
                    tableR[licznik] = c.getRed();
                    tableG[licznik] = c.getGreen();
                    tableB[licznik] = c.getBlue();
                    licznik++;
                }
            }
        }
        Arrays.sort(tableR);
        Arrays.sort(tableG);
        Arrays.sort(tableB);
        int index = (w_size*w_size)/2;
        Color c = new Color(tableR[index], tableG[index], tableB[index]);
        return c.getRGB();
    }
    public int calculateNewPixelValue(BufferedImage img, int w, int h) {
        double sumRed = 0, sumGreen = 0, sumBlue = 0;
        double mask_sum = 0;
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(i+w>0 && i+w<img.getWidth() && j+h>0 &&j+h<img.getHeight()) {

                    Color c = new Color(img.getRGB(w + i, h + j));
                    sumRed += c.getRed() * mask[i + 1][j + 1];
                    sumBlue += c.getBlue() * mask[i + 1][j + 1];
                    sumGreen += c.getGreen() * mask[i + 1][j + 1];
                }
                    mask_sum += mask[i + 1][j + 1];

            }
        }
       /* sumRed = sumRed / 9;
        sumBlue = sumBlue / 9;
        sumGreen = sumGreen / 9;*/
       if (mask_sum!=0) {
           sumRed = sumRed / mask_sum;
           sumBlue = sumBlue / mask_sum;
           sumGreen = sumGreen / mask_sum;
       }

        if (sumRed >255) sumRed = 255;
        if (sumGreen >255) sumGreen = 255;
        if (sumBlue >255) sumBlue = 255;

        if (sumRed <0) sumRed = 0;
        if (sumGreen <0) sumGreen = 0;
        if (sumBlue <0) sumBlue = 0;

        Color c = new Color((int) sumRed, (int) sumGreen, (int) sumBlue);
        return c.getRGB();
    }


    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
