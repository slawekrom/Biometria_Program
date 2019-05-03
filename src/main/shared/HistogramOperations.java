package main.shared;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HistogramOperations {

    int minR = 255;
    int minG = 255;
    int minB = 255;
    int minRGB = 255;
    int maxR = 1;
    int maxG = 1;
    int maxB = 1;
    int maxRGB = 1;

    public int getMinR() {
        return minR;
    }

    public int getMinG() {
        return minG;
    }

    public int getMinB() {
        return minB;
    }

    public int getMinRGB() {
        return minRGB;
    }

    public int getMaxR() {
        return maxR;
    }

    public int getMaxG() {
        return maxG;
    }

    public int getMaxB() {
        return maxB;
    }

    public int getMaxRGB() {
        return maxRGB;
    }

    public List<int[]> calculateHistograms(BufferedImage image) {
        int[] histogramRed = new int[256];
        int[] histogramGreen = new int[256];
        int[] histogramBlue = new int[256];
        int[] RGB = new int[256];

        for(int w = 0; w < image.getWidth(); w++) {
            for(int h = 0; h < image.getHeight(); h++) {
                Color color = new Color(image.getRGB(w, h));
                histogramRed[color.getRed()]++;
                histogramGreen[color.getGreen()]++;
                histogramBlue[color.getBlue()]++;
            }
        }
        for (int i = 0;i<256;i++){
            RGB[i] =(int) (histogramBlue[i]+histogramRed[i] + histogramGreen[i])/3;
        }

        List<int[]> histograms = new ArrayList<>();
        histograms.add(histogramRed);
        histograms.add(histogramGreen);
        histograms.add(histogramBlue);
        histograms.add(RGB);

        return histograms;
    }

    public void calculateValue(List<int[]> histograms){
        int[] histogramRed = histograms.get(0);
        int[] histogramGreen =  histograms.get(1);
        int[] histogramBlue =  histograms.get(2);
        int[] histogramRGB =  histograms.get(3);
         minR = 255;
         minG = 255;
         minB = 255;
         minRGB = 255;
         maxR = 1;
         maxG = 1;
         maxB = 1;
         minRGB = 255;
         maxRGB = 1;
         maxRGB = 1;

        for (int i=0;i<256;i++){
            if (histogramRed[i]>0 && i<minR){
                minR = i;
            }
            if (histogramGreen[i]>0 && i<minG){
                minG = i;
            }
            if (histogramBlue[i]>0 && i<minB){
                minB = i;
            }
            if (histogramRGB[i]>0 && i<minRGB){
                minRGB = i;
            }
            if (histogramRed[i]>0 && i>maxR){
                maxR = i;
            }
            if (histogramGreen[i]>0 && i>maxG){
                maxG = i;
            }
            if (histogramBlue[i]>0 && i>maxB){
                maxB = i;
            }
            if (histogramRGB[i]> 0 && i>maxRGB){
                maxRGB = i;
            }
        }
        System.out.println("min R "+minR+"max R "+maxR);
        System.out.println("min  G "+minG+"max G "+maxG);
        System.out.println("min  b "+minB+"max B "+maxB);

        System.out.println("max r "+maxR+"g "+maxG+"b "+maxB);

    }
    public List<double[]> calculateDistr(BufferedImage img,List<int[]> histograms){
        double DistrR[] = new double[256];
        double DistrG[] = new double[256];
        double DistrB[] = new double[256];
       /* for (int i =0;i<256;i++){
            DistrR[i] = 0;
            DistrG[i] = 0;
            DistrB[i] = 0;
        }*/
        int[] histogramRed = histograms.get(0);
        int[] histogramGreen =  histograms.get(1);
        int[] histogramBlue =  histograms.get(2);
        double numberOfPixels = img.getHeight()*img.getWidth();
        DistrR[0]  = histogramRed[0]/numberOfPixels;
        DistrG[0]  = histogramGreen[0]/numberOfPixels;
        DistrB[0]  = histogramBlue[0]/numberOfPixels;
        for(int i=1;i<256;i++){
            DistrR[i] = DistrR[i-1]+(histogramRed[i]/numberOfPixels);
            DistrG[i] = DistrG[i-1]+(histogramGreen[i]/numberOfPixels);
            DistrB[i] = DistrB[i-1]+(histogramBlue[i]/numberOfPixels);
        }

        List<double[]> distr = new ArrayList<>();
        distr.add(DistrR);
        distr.add(DistrG);
        distr.add(DistrB);
        return distr;
    }


}

