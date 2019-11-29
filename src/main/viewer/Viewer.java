package main.viewer;


import main.shared.HistogramOperations;
import main.shared.ImageSharedOperations;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.SwingWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Viewer extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JMenu files = new JMenu("File");
    private JMenu histogramItem = new JMenu("Histograms");
    private JMenu operations = new JMenu("Operations");
    private JMenu convetToGreyScale = new JMenu("Convert To Grey Scale");
    private JMenu binarization = new JMenu("Binarization");
    private  JMenu filters  = new JMenu("Filters");
    private JMenuItem mediana = new JMenuItem("Mediana filter");
    private JMenuItem kuwahar = new JMenuItem("Kuwahar filter");
    private JMenuItem laplaceFilter = new JMenuItem("Set Laplace mask");
    private JMenuItem sobelFilter0 = new JMenuItem("Sobel filter 0 ");
    private JMenuItem sobelFilter45 = new JMenuItem("Sobel filter 45 ");
    private JMenuItem sobelFilter90 = new JMenuItem("Sobel filter 90 ");
    private JMenuItem sobelFilter135 = new JMenuItem("Sobel filter 135 ");
    private JMenuItem sharpFilter = new JMenuItem("Filtr Wyostrzający");
    private JMenuItem HP1Filter = new JMenuItem("HP 1 Filter");
    private JMenuItem prewittFilter = new JMenuItem("Prewitt 90");
    private JMenuItem gaussianBlur = new JMenuItem("Gaussian blur Filter");
    private JMenuItem bernsen = new JMenuItem("Bernsen method");
    private JMenuItem byR = new JMenuItem("Convert by R");
    private JMenuItem byG = new JMenuItem("Convert by G");
    private JMenuItem byB = new JMenuItem("Convert by B");
    private JMenuItem byRGB = new JMenuItem("Convert by RGB");
    private JMenuItem stretchHistogram = new JMenuItem("Stretch histogram");
    private JMenuItem equalizeHistogram = new JMenuItem("equalize histogram");
    private JMenuItem lightenImage = new JMenuItem("Lighten image");
    private JMenuItem darkenImage = new JMenuItem("Darken image");
    private JMenuItem niblack = new JMenuItem("Niblack method");
    private JMenuItem calculateHistograms = new JMenuItem("Calculate histograms");
    private JMenuItem drawRedHistogram = new JMenuItem("Draw RED histogram");
    private JMenuItem drawGreenHistogram = new JMenuItem("Draw GREEN histogram");
    private JMenuItem drawBlueHistogram = new JMenuItem("Draw BLUE histogram");
    private JMenuItem drawRGBHistogram = new JMenuItem("Draw RGB/3 histogram");
    private JMenuItem loadImage = new JMenuItem("Load image");
    private JMenuItem loadLenaImage = new JMenuItem("Load Lena Image");
    private JMenuItem saveImage = new JMenuItem("Save image");
    private FotoPanel fotoPanel;
    private HistogramOperations histogramOperations;
    List<int[]> histograms = new ArrayList<>();
    List<double[]> distrs = new ArrayList<>();
    int[] LUT_R = new int[256];
    int[] LUT_G = new int[256];
    int[] LUT_B = new int[256];
    double[] distr_LUT_R = new double[256];
    double[] distr_LUT_G = new double[256];
    double[] distr_LUT_B = new double[256];
    private int minR,minG,minB,maxR,maxG,maxB;


    public Viewer() {
        this.setTitle("BIOMETRIA - CW 1");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        fotoPanel = new FotoPanel();

        histogramOperations = new HistogramOperations();
        menuBar.add(files);
        files.add(loadImage);
        files.add(saveImage);
        files.add(loadLenaImage);
        add(menuBar, BorderLayout.NORTH);
        menuBar.add(histogramItem);
        histogramItem.add(calculateHistograms);
        histogramItem.add(drawRedHistogram);
        histogramItem.add(drawGreenHistogram);
        histogramItem.add(drawBlueHistogram);
        histogramItem.add(drawRGBHistogram);
        menuBar.add(operations);
        menuBar.add(convetToGreyScale);
        menuBar.add(binarization);
        menuBar.add(filters);
        filters.add(mediana);
        filters.add(kuwahar);
        filters.add(laplaceFilter);
        filters.add(sobelFilter0);
        filters.add(sobelFilter45);
        filters.add(sobelFilter90);
        filters.add(sobelFilter135);
        filters.add(sharpFilter);
        filters.add(HP1Filter);
        filters.add(gaussianBlur);
        filters.add(prewittFilter);
        binarization.add(bernsen);
        binarization.add(niblack);
        convetToGreyScale.add(byR);
        convetToGreyScale.add(byG);
        convetToGreyScale.add(byB);
        convetToGreyScale.add(byRGB);
        operations.add(stretchHistogram);
        operations.add(equalizeHistogram);
        operations.add(lightenImage);
        operations.add(darkenImage);

        setVisible(true);
        loadImage.addActionListener((ActionEvent e) -> {
            Load loadWindow = new Load();

            int returnValue = loadWindow.showDialog(null, "Select image");
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                BufferedImage img = ImageSharedOperations.loadImage(loadWindow.getSelectedFile().getPath());
                fotoPanel.setBufferedImage(img);
                fotoPanel.loadImage();
                add(fotoPanel);
                pack();
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        loadLenaImage.addActionListener(e -> {

            BufferedImage img = ImageSharedOperations.loadImage("C:\\Users\\Sławek\\Pictures\\lena.jpg");
            fotoPanel.setBufferedImage(img);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);

        });

        saveImage.addActionListener((ActionEvent e) -> {

            Save  saveWindow = new Save();
            String path = saveWindow.getFileFilter().getDescription();
            BufferedImage img = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            String pathToSave = saveWindow.getSelectedFile().getPath()+path;
            ImageSharedOperations.saveImage(img, pathToSave);
        });

        calculateHistograms.addActionListener((ActionEvent e) -> {
            calculateHistogram();
        });

        drawRedHistogram.addActionListener(e -> {
            ChartDraw exampleChart = new ChartDraw();
            exampleChart.setArray(histograms.get(0));
            exampleChart.setName("Red histogram");
            CategoryChart chart = exampleChart.getChart();
            chart.getStyler().setSeriesColors(new Color[]{Color.RED});

            Thread t = new Thread(() -> {
                JFrame red_his_frame = new SwingWrapper(chart).displayChart();
                red_his_frame.setTitle("RED HISTOGRAM");
                red_his_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            });
            t.start();
        });
        drawGreenHistogram.addActionListener(e -> {
            ChartDraw exampleChart = new ChartDraw();
            exampleChart.setArray(histograms.get(1));
            exampleChart.setName("Green histogram");
            CategoryChart chart = exampleChart.getChart();
            chart.getStyler().setSeriesColors(new Color[]{Color.GREEN});

            Thread t = new Thread(() -> {
                JFrame green_his_frame = new SwingWrapper(chart).displayChart();
                green_his_frame.setTitle("GREEN HISTOGRAM");
                green_his_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            });
            t.start();
        });
        drawBlueHistogram.addActionListener(e -> {
            ChartDraw exampleChart = new ChartDraw();
            exampleChart.setArray(histograms.get(2));
            exampleChart.setName("Blue histogram");
            CategoryChart chart = exampleChart.getChart();
            chart.getStyler().setSeriesColors(new Color[]{Color.BLUE});

            Thread t = new Thread(() -> {
                JFrame blue_his_frame = new SwingWrapper(chart).displayChart();
                blue_his_frame.setTitle("BLUE HISTOGRAM");
                blue_his_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            });
            t.start();
        });
        drawRGBHistogram.addActionListener(e -> {
            ChartDraw exampleChart = new ChartDraw();
            exampleChart.setArray(histograms.get(3));
            exampleChart.setName("RGB/3 Histogram");
            CategoryChart chart = exampleChart.getChart();
            chart.getStyler().setSeriesColors(new Color[]{Color.BLACK});

            Thread t = new Thread(() -> {
                JFrame rgb_his_frame = new SwingWrapper(chart).displayChart();
                rgb_his_frame.setTitle("GRAY HISTOGRAM");
                rgb_his_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            });
            t.start();
        });
        stretchHistogram.addActionListener(e -> {
            calculateLUT();
            BufferedImage img = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            BufferedImage newImg = img;
            int colorValue;
            Color color;
            Color newColor;
            int R_value,G_value,B_value;
            for(int w = 0; w < img.getWidth(); w++) {
                for(int h = 0; h < img.getHeight(); h++) {
                    colorValue = img.getRGB(w,h);
                    color = new Color(colorValue);
                    R_value = color.getRed();
                    G_value = color.getGreen();
                    B_value = color.getBlue();
                    newColor = new Color(LUT_R[R_value],LUT_G[G_value],LUT_B[B_value],255);
                    newImg.setRGB(w,h,newColor.getRGB());


                }
            }
            fotoPanel.setBufferedImage(newImg);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            calculateHistogram();

        });
        equalizeHistogram.addActionListener(e -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            distrs = histogramOperations.calculateDistr(img,histograms);
            calculateDistrLUT();
            BufferedImage newImage = img;
            int colorValue;
            Color color;
            Color newColor;
            int R_value,G_value,B_value;
            for(int w = 0; w < img.getWidth(); w++) {
                for(int h = 0; h < img.getHeight(); h++) {
                    colorValue = img.getRGB(w,h);
                    color = new Color(colorValue);
                    R_value = color.getRed();
                    G_value = color.getGreen();
                    B_value = color.getBlue();
                    newColor = new Color((int)distr_LUT_R[R_value],(int)distr_LUT_G[G_value],(int)distr_LUT_B[B_value],255);
                    newImage.setRGB(w,h,newColor.getRGB());


                }
            }
            fotoPanel.setBufferedImage(newImage);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            calculateHistogram();

        });

        lightenImage.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            BufferedImage newImage = image;
            int colorValue;
            Color color;
            Color newColor;
            int R_value,G_value,B_value;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    colorValue = image.getRGB(i,j);
                    color = new Color(colorValue);
                    R_value = color.getRed();
                    G_value = color.getGreen();
                    B_value = color.getBlue();
                    R_value = (int) (30*Math.log(R_value+1));
                    G_value = (int) (30*Math.log(G_value+1));
                    B_value = (int) (30*Math.log(B_value+1));

                    if (R_value >255){
                        R_value = 255;
                    }
                    if (G_value >255){
                        G_value = 255;
                    }
                    if (B_value >255){
                        B_value = 255;
                    }

                    newColor = new Color(R_value,G_value,B_value);
                    newImage.setRGB(i,j,newColor.getRGB());


                }
            }
            fotoPanel.setBufferedImage(newImage);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            calculateHistogram();
        });
        darkenImage.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            BufferedImage newImage = image;
            int colorValue;
            Color color;
            Color newColor;
            int R_value,G_value,B_value;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    colorValue = image.getRGB(i,j);
                    color = new Color(colorValue);
                    R_value = color.getRed();
                    G_value = color.getGreen();
                    B_value = color.getBlue();
                    R_value = (int) (0.001 * Math.pow(R_value,2));
                    G_value = (int) (0.001 * Math.pow(G_value,2));
                    B_value = (int) (0.001 * Math.pow(B_value,2));

                    if (R_value >255){
                        R_value = 255;
                    }
                    if (G_value >255){
                        G_value = 255;
                    }
                    if (B_value >255){
                        B_value = 255;
                    }

                    newColor = new Color(R_value,G_value,B_value);
                    newImage.setRGB(i,j,newColor.getRGB());


                }
            }
            fotoPanel.setBufferedImage(newImage);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            calculateHistogram();
        });
        byR.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            int colorValue;
            Color color;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    color = new Color(image.getRGB(i,j));
                    colorValue = color.getRed();
                    image.setRGB(i,j,new Color(colorValue,colorValue,colorValue).getRGB());
                }
            }
            fotoPanel.setBufferedImage(image);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        byG.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            int colorValue;
            Color color;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    color = new Color(image.getRGB(i,j));
                    colorValue = color.getGreen();
                    image.setRGB(i,j,new Color(colorValue,colorValue,colorValue).getRGB());
                }
            }
            fotoPanel.setBufferedImage(image);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        byB.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            int colorValue;
            Color color;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    color = new Color(image.getRGB(i,j));
                    colorValue = color.getBlue();
                    image.setRGB(i,j,new Color(colorValue,colorValue,colorValue).getRGB());
                }
            }
            fotoPanel.setBufferedImage(image);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        byRGB.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
            int colorValue;
            Color color;
            for (int i = 0;i<image.getWidth();i++){
                for (int j = 0;j<image.getHeight();j++){
                    color = new Color(image.getRGB(i,j));
                    colorValue = (color.getRed()+color.getBlue()+color.getGreen())/3;
                    image.setRGB(i,j,new Color(colorValue,colorValue,colorValue).getRGB());
                }
            }
            fotoPanel.setBufferedImage(image);
            fotoPanel.loadImage();
            add(fotoPanel);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        bernsen.addActionListener(e -> fotoPanel.bernsen());
        niblack.addActionListener(e -> {
            fotoPanel.niblack();
        });
        mediana.addActionListener(e -> {
            String window_size = JOptionPane.showInputDialog("Enter winodw size ");
            int w_size = Integer.parseInt(window_size);
            System.out.println(window_size);
            fotoPanel.medianFilter(w_size);
        });
        kuwahar.addActionListener(e -> {
            fotoPanel.kuwaharFilter();
        });
        laplaceFilter.addActionListener(e ->
                fotoPanel.setLaplaceMask());
        sobelFilter0.addActionListener(e ->
                fotoPanel.setSobelMask0());
        sobelFilter45.addActionListener(e ->
                fotoPanel.setSobelMask45());
        sobelFilter90.addActionListener(e ->
                fotoPanel.setSobelMask90());
        sobelFilter135.addActionListener(e ->
                fotoPanel.setSobelMask135());
        sharpFilter.addActionListener(e ->
                fotoPanel.setSharpeningFilter());
        HP1Filter.addActionListener(e ->
                fotoPanel.setHP1Filter());
        gaussianBlur.addActionListener(e ->
                fotoPanel.setGaussianBlurFilter());
        prewittFilter.addActionListener(e ->
                fotoPanel.setPrewittFilter());
    }

    private void calculateLUT(){
        double r,g,b;
        int rr,gg,bb;
        for(int i = 0;i<256;i++){
            //dla R
            r =  256.0*(i-minR)/(maxR-minR);
            if(r>255){
                LUT_R[i] = 255;}
                else if(r<0){
                    LUT_R[i] = 0;
                }
                else {
                    rr = (int) r;
                    LUT_R[i] = rr;
            }

                //dla G
            g = 256.0*(i-minG)/(maxG-minG);
            if(g>255){
                LUT_G[i] = 255;}
            else if(g<0){
                LUT_G[i] = 0;
            }
            else {
                gg = (int) g;
                LUT_G[i] = gg;
            }

            // dla B
            b = 256.0*(i-minB)/(maxB-minB);
            if(b>255){
                LUT_B[i] = 255;}
            else if(b<0){
                LUT_B[i] = 0;
            }
            else {
                bb= (int) b;
                LUT_B[i] = bb;
            }
            System.out.println("r "+LUT_R[i]+"g "+LUT_G[i]+"b "+LUT_B[i]);

        }
    }

    private void calculateLocalContrast(BufferedImage image){

    }

    private void calculateDistrLUT(){
        double D0_R,D0_G,D0_B;

        for (int i=0;i<256;i++){
            distr_LUT_R[i] = 0;
            distr_LUT_G[i] = 0;
            distr_LUT_B[i] = 0;

        }


        //szukanie pierwszej niezerowej dystrubuanty
        int i;
        i=0;
        while (distrs.get(0)[i] ==0) {i++;}
        D0_R = distrs.get(0)[i];

        i=0;
        while (distrs.get(1)[i] ==0) {i++;}
        D0_G = distrs.get(1)[i];

        i=0;
        while (distrs.get(2)[i] ==0) {i++;}
        D0_B = distrs.get(2)[i];

        // obliczanie LUT
        for(int j=0;j<256;j++){
            distr_LUT_R[j] = ((distrs.get(0)[j]-D0_R)/(1-D0_R))*255;
            distr_LUT_G[j] = ((distrs.get(1)[j]-D0_G)/(1-D0_G))*255;
            distr_LUT_B[j] = ((distrs.get(2)[j]-D0_B)/(1-D0_B))*255;

        }
    }
    private void calculateHistogram(){
        BufferedImage img = ImageSharedOperations.convertIconToImage(fotoPanel.getImageIcon());
        histograms =histogramOperations.calculateHistograms(img);
        histogramOperations.calculateValue(histograms);
        minR = histogramOperations.getMinR();
        minG = histogramOperations.getMinG();
        minB = histogramOperations.getMinB();
        maxR = histogramOperations.getMaxR();
        maxG = histogramOperations.getMaxG();
        maxB = histogramOperations.getMaxB();
    }

}
