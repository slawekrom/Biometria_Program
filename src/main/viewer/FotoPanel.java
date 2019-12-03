package main.viewer;


import main.shared.Filters;
import main.shared.ImageSharedOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class FotoPanel extends JPanel {
    private  BufferedImage bufferedImage;
    private final JLabel fotoLabel = new JLabel();
    private final JScrollPane jScrollPane = new JScrollPane();
    private JLabel RLabel = new JLabel("R");
    private JLabel GLabel = new JLabel("G");
    private JLabel BLabel = new JLabel("B");
    private JLabel enterThreshold = new JLabel("Enter threshold");
    private JLabel enterContrastThreshold = new JLabel("Enter contrast threshold");
    private JLabel enter_k = new JLabel("Enter k parameter for niblack method ");
    private JLabel window_size = new JLabel("enter mask size for niblack method ");
    private JLabel maskLabel = new JLabel("Mask");
    private JTextField mask_0_0 = new JTextField();
    private JTextField mask_0_1 = new JTextField();
    private JTextField mask_0_2 = new JTextField();
    private JTextField mask_1_0 = new JTextField();
    private JTextField mask_1_1 = new JTextField();
    private JTextField mask_1_2 = new JTextField();
    private JTextField mask_2_0 = new JTextField();
    private JTextField mask_2_1 = new JTextField();
    private JTextField mask_2_2 = new JTextField();
    private JTextField win_size = new JTextField();
    private JTextField k_parameter = new JTextField();
    private JTextField j_threshold = new JTextField();
    private JTextField j_contr_threshold = new JTextField();
    private JTextField RField = new JTextField();
    private JTextField GField = new JTextField();
    private JTextField BField = new JTextField();
    private JButton manual = new JButton("manual");
    private JButton changeRGB = new JButton("Change RGB");
    private JButton filter  = new JButton("Filter");
    private int x,y;
    private int pom = 0;
    private int threshold;
    private BufferedImage originalImage;
    int[][] localContrast,max,min;
    private int mask [][] = new int[3][3];
    private Filters filters = new Filters();


    public FotoPanel(){
        setLayout(null);
        setBounds(0,0,720,480);
        fotoLabel.setBounds(0,0,720,480);
        add(RLabel);
        RLabel.setBounds(30,510,30,30);
        add(RField);
        RField.setBounds(70,510,40,30);
        add(GLabel);
        GLabel.setBounds(140,510,30,30);
        add(GField);
        GField.setBounds(175,510,40,30);
        add(BLabel);
        BLabel.setBounds(220,510,30,30);
        add(BField);
        BField.setBounds(255,510,40,30);
        add(changeRGB);
        changeRGB.setBounds(300,510,200,50);

        add(enterThreshold);
        enterThreshold.setBounds(800,50,100,50);
        add(enter_k);
        enter_k.setBounds(800,270,150,30);
        add(k_parameter);
        k_parameter.setBounds(800,320,40,40);
        add(window_size);
        window_size.setBounds(1000,270,150,30);
        add(win_size);
        win_size.setBounds(1000,320,40,40);
        add(enterContrastThreshold);
        enterContrastThreshold.setBounds(1000,50,150,50);
        add(j_threshold);
        j_threshold.setBounds(800,100,50,50);
        add(j_contr_threshold);
        j_contr_threshold.setBounds(1000,100,50,50);
        addMask();
        add(manual);
        manual.setBounds(800,200,100,50);
        setVisible(true);

        fotoLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int colorValue = bufferedImage.getRGB(e.getX(),e.getY());
                Color color = new Color(colorValue);
                RField.setText(String.valueOf((color.getRed())));
                GField.setText(String.valueOf((color.getGreen())));
                BField.setText(String.valueOf((color.getBlue())));
            }
        });
        fotoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                 x=e.getX();
                 y = e.getY();
            }
        });
        changeRGB.addActionListener(e -> {
            BufferedImage img = ImageSharedOperations.convertIconToImage(getImageIcon());
            int r=Integer.parseInt(RField.getText());
            int g=Integer.parseInt(GField.getText());
            int b=Integer.parseInt(BField.getText());

            if(r>=0 && r<=255 && g>=0 && g<=255 && b>=0 && b<=255 ) {
                Color color = new Color(r, g, b);
                img.setRGB(x, y, color.getRGB());
                originalImage = img;
                fotoLabel.setIcon(new ImageIcon(img));
            }
        });
        manual.addActionListener(e -> {
            threshold = Integer.parseInt(j_threshold.getText());
            BufferedImage image = ImageSharedOperations.convertIconToImage(getImageIcon());
            for(int w = 0; w < image.getWidth(); w++) {
                for(int h = 0; h < image.getHeight(); h++) {
                    Color c = new Color(image.getRGB(w, h));
                    image.setRGB(w, h,
                            c.getRed() >= threshold ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
                }
            }
            //fotoLabel.setIcon(new ImageIcon(image));
            JFrame jFrame = new JFrame();
            JLabel jLabel = new JLabel();
            jLabel.setSize(image.getWidth(),image.getHeight());
            jLabel.setIcon(new ImageIcon(image));
            jFrame.add(jLabel);
            jFrame.setSize(image.getWidth(),image.getHeight());
            jFrame.setTitle("Manual method");
            jFrame.show();
        });
        fotoLabel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                float temp = 1;
                if(rotation<0){
                    temp+=0.1;
                    pom++;
                }
                else{
                    temp-=0.1;
                    pom--;
                }

                //przy powiekszeniu/pomnijszeniu do oryginalnego rozmianru
                //podstawiane jest oryginalne zdjecie aby nie tracic na jakosci zdjecia
                if(pom ==0){
                    fotoLabel.setIcon(new ImageIcon(originalImage));
                }
                else {
                    BufferedImage img = ImageSharedOperations.convertIconToImage(getImageIcon());
                    int imgHeight = (int) (img.getHeight() * temp);
                    int imgWidth = (int) (img.getWidth() * temp);
                    BufferedImage newImage = new BufferedImage(imgWidth, imgHeight, img.getType());
                    Graphics2D graphics = newImage.createGraphics();
                    graphics.drawImage(img, 0, 0, imgWidth, imgHeight, null);
                    graphics.dispose();
                    fotoLabel.setIcon(new ImageIcon(newImage));
                }
            }
        });
    }


    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        originalImage = bufferedImage;
    }

    public void loadImage(){
        fotoLabel.setIcon(new ImageIcon(bufferedImage));
        jScrollPane.setViewportView(fotoLabel);
        add(jScrollPane);
        jScrollPane.setBounds(0,0,720,480);
    }

    public ImageIcon getImageIcon(){
        return (ImageIcon) fotoLabel.getIcon();
    }
    public void bernsen(){
        BufferedImage image = ImageSharedOperations.convertIconToImage(getImageIcon());
        BufferedImage newImage = deepCopy(image);
        int t,local_contrast;
        Color color;
        int contrast_threshold = Integer.parseInt(j_contr_threshold.getText());
        int set_threshold = Integer.parseInt(j_threshold.getText());
        calculateLocalContrast(image);
        for (int w = 0;w<image.getWidth();w++) {
            for (int h = 0; h < image.getHeight(); h++) {
                color = new Color(image.getRGB(w, h));
                t = (max[w][h] + min[w][h]) / 2;
                local_contrast = (max[w][h] - min[w][h]) ;
                if(local_contrast < contrast_threshold){
                    if (t >=set_threshold){
                        newImage.setRGB(w,h,new Color(255,255,255).getRGB());
                    }
                    else newImage.setRGB(w, h, new Color(0, 0, 0).getRGB());
                }
                else if (color.getRed() >=t){
                    newImage.setRGB(w,h,new Color(255,255,255).getRGB());
                }
                else {
                    newImage.setRGB(w,h,new Color(0,0,0).getRGB());
                }



            }
        }
        fotoLabel.setIcon(new ImageIcon(newImage));
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(newImage.getWidth(),newImage.getHeight());
        jLabel.setIcon(new ImageIcon(newImage));
        jFrame.add(jLabel);
        jFrame.setSize(newImage.getWidth(),newImage.getHeight());
        jFrame.setTitle("Bernsen method");
        jFrame.show();

    }
    public void calculateLocalContrast(BufferedImage image){
        localContrast = new int[image.getHeight()][image.getWidth()];
        max =  new int[image.getHeight()][image.getWidth()];
        min =  new int[image.getHeight()][image.getWidth()];
        int colorValue,max_v,min_v;
        Color color;

        for (int i = 0; i<image.getHeight();i++){
            for (int j = 0;j<image.getWidth();j++){
                min_v = 255;
                max_v = 0;
                for (int k = i-1;k<=i+1;k++){
                    for (int m = j-1;m<=j+1;m++) {
                        if (k >= 0 && k < image.getHeight() && m >= 0 && m < image.getWidth()){
                            if (k != i || m != j) {
                                color = new Color(image.getRGB(k, m));
                                colorValue = color.getRed();
                                if (colorValue > max_v) max_v = colorValue;
                                if (colorValue < min_v) min_v = colorValue;
                            }
                    }
                    }
                }
                //System.out.println("mim="+min_v+" max="+max_v);
                max[i][j] = max_v;
                min[i][j] = min_v;


            }
        }
        System.out.println("Done");
    }
    public void niblack(){

        Color color;
        BufferedImage image = ImageSharedOperations.convertIconToImage(getImageIcon());
        BufferedImage newImage = deepCopy(image);
        int t;
        double colorValue;
        int w_size;
        int a =Integer.parseInt(win_size.getText())/2;
        double k_par = Double.parseDouble(k_parameter.getText());
        for (int w= 0;w<image.getWidth();w++){
            for (int h = 0;h<image.getHeight();h++){
                double srednia = 0;
                double sigma = 0;
                w_size = 0;
                for (int k = w-a;k<=w+a;k++) {
                    for (int m = h - a; m <= h + a; m++) {
                        if (k >= 0 && k < image.getHeight() && m >= 0 && m < image.getWidth()) {
                            if (k != w || m != h) {
                                color = new Color(image.getRGB(k, m));
                                colorValue = (color.getRed()+color.getGreen()+color.getBlue())/3.0;
                                srednia += colorValue;
                                w_size++;
                            }
                        }
                    }
                }
                srednia = srednia/w_size;
                    for (int x = w-a;x<=w+a;x++){
                        for (int y = h-a;y<=h+a;y++){
                            if (x >= 0 && x < image.getHeight() && y >= 0 && y < image.getWidth()){
                                if (x != w || y != h) {
                                    color = new Color(image.getRGB(x, y));
                                    colorValue = (color.getRed()+color.getGreen()+color.getBlue())/3.0;
                                    sigma+=Math.pow((colorValue-srednia),2);
                                }
                            }
                        }
                    }

                    sigma  = sigma/w_size;
                t= (int) (srednia + (k_par*Math.sqrt(sigma)));
                color = new Color(image.getRGB(w,h));
                colorValue = (color.getRed()+color.getGreen()+color.getBlue())/3.0;
                if(colorValue>t){
                    newImage.setRGB(w,h,new Color(255,255,255).getRGB());
                }
                else {
                    newImage.setRGB(w,h,new Color(0,0,0).getRGB());
                }

            }
        }
        //fotoLabel.setIcon(new ImageIcon(newImage));
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(newImage.getWidth(),newImage.getHeight());
        jLabel.setIcon(new ImageIcon(newImage));
        jFrame.add(jLabel);
        jFrame.setSize(newImage.getWidth(),newImage.getHeight());
        jFrame.setTitle("Niblack method");
        jFrame.show();

    }
    private void addMask(){
        add(maskLabel);
        maskLabel.setBounds(800,400,150,30);
        add(mask_0_0);
        mask_0_0.setBounds(800,450,30,30);
        add(mask_0_1);
        mask_0_1.setBounds(850,450,30,30);
        add(mask_0_2);
        mask_0_2.setBounds(900,450,30,30);

        add(mask_1_0);
        mask_1_0.setBounds(800,500,30,30);
        add(mask_1_1);
        mask_1_1.setBounds(850,500,30,30);
        add(mask_1_2);
        mask_1_2.setBounds(900,500,30,30);

        add(mask_2_0);
        mask_2_0.setBounds(800,550,30,30);
        add(mask_2_1);
        mask_2_1.setBounds(850,550,30,30);
        add(mask_2_2);
        mask_2_2.setBounds(900,550,30,30);

        add(filter);
        filter.setBounds(900,600,100,40);
        filter.addActionListener(e -> {
            doFilter();
            /*fotoLabel.setIcon(new ImageIcon(img));*/

        });
    }
    public void medianFilter(int w_size){
        BufferedImage image = filters.medianFilter(ImageSharedOperations.convertIconToImage(getImageIcon()),w_size);
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(image.getWidth(),image.getHeight());
        jLabel.setIcon(new ImageIcon(image));
        jFrame.add(jLabel);
        jFrame.setSize(image.getWidth(),image.getHeight());
        jFrame.show();
    }
    public void kuwaharFilter(){
        BufferedImage image = filters.kuwaharFilter(ImageSharedOperations.convertIconToImage(getImageIcon()));
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(image.getWidth(),image.getHeight());
        jLabel.setIcon(new ImageIcon(image));
        jFrame.add(jLabel);
        jFrame.setSize(image.getWidth(),image.getHeight());
        jFrame.show();
    }
    private void setMask(){
        mask[0][0] = Integer.parseInt(mask_0_0.getText());
        mask[0][1] = Integer.parseInt(mask_0_1.getText());
        mask[0][2] = Integer.parseInt(mask_0_2.getText());

        mask[1][0] = Integer.parseInt(mask_1_0.getText());
        mask[1][1] = Integer.parseInt(mask_1_1.getText());
        mask[1][2] = Integer.parseInt(mask_1_2.getText());

        mask[2][0] = Integer.parseInt(mask_2_0.getText());
        mask[2][1] = Integer.parseInt(mask_2_1.getText());
        mask[2][2] = Integer.parseInt(mask_2_2.getText());
    }
    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void dilationFilter(){
        BufferedImage image = filters.dilationFilter(ImageSharedOperations.convertIconToImage(getImageIcon()));
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(image.getWidth(),image.getHeight());
        jLabel.setIcon(new ImageIcon(image));
        jFrame.add(jLabel);
        jFrame.setSize(image.getWidth(),image.getHeight());
        jFrame.show();
    }

    public void setLaplaceMask(){
        mask_0_0.setText("-1");
        mask_0_1.setText("-1");
        mask_0_2.setText("-1");
        mask_1_0.setText("-1");
        mask_1_1.setText("8");
        mask_1_2.setText("-1");
        mask_2_0.setText("-1");
        mask_2_1.setText("-1");
        mask_2_2.setText("-1");
        doFilter();
    }
    public void setSobelMask0(){
        mask_0_0.setText("-1");
        mask_0_1.setText("0");
        mask_0_2.setText("1");
        mask_1_0.setText("-2");
        mask_1_1.setText("0");
        mask_1_2.setText("2");
        mask_2_0.setText("-1");
        mask_2_1.setText("0");
        mask_2_2.setText("1");
        doFilter();
    }
    public void setSobelMask90(){
        mask_0_0.setText("1");
        mask_0_1.setText("2");
        mask_0_2.setText("1");
        mask_1_0.setText("0");
        mask_1_1.setText("0");
        mask_1_2.setText("0");
        mask_2_0.setText("-1");
        mask_2_1.setText("-2");
        mask_2_2.setText("-1");
        doFilter();
    }
    public void setSobelMask45(){
        mask_0_0.setText("0");
        mask_0_1.setText("1");
        mask_0_2.setText("2");
        mask_1_0.setText("-1");
        mask_1_1.setText("0");
        mask_1_2.setText("1");
        mask_2_0.setText("-2");
        mask_2_1.setText("-1");
        mask_2_2.setText("0");
        doFilter();
    }
    public void setSobelMask135(){
        mask_0_0.setText("2");
        mask_0_1.setText("1");
        mask_0_2.setText("0");
        mask_1_0.setText("1");
        mask_1_1.setText("0");
        mask_1_2.setText("-1");
        mask_2_0.setText("0");
        mask_2_1.setText("-1");
        mask_2_2.setText("-2");
        doFilter();
    }
    public void setSharpeningFilter(){
        mask_0_0.setText("-1");
        mask_0_1.setText("-1");
        mask_0_2.setText("-1");
        mask_1_0.setText("-1");
        mask_1_1.setText("9");
        mask_1_2.setText("-1");
        mask_2_0.setText("-1");
        mask_2_1.setText("-1");
        mask_2_2.setText("-1");
        doFilter();
    }
    public void setHP1Filter(){
        mask_0_0.setText("0");
        mask_0_1.setText("-1");
        mask_0_2.setText("0");
        mask_1_0.setText("-1");
        mask_1_1.setText("5");
        mask_1_2.setText("-1");
        mask_2_0.setText("0");
        mask_2_1.setText("-1");
        mask_2_2.setText("0");
        doFilter();
    }
    public void setGaussianBlurFilter(){
        mask_0_0.setText("1");
        mask_0_1.setText("2");
        mask_0_2.setText("1");
        mask_1_0.setText("2");
        mask_1_1.setText("4");
        mask_1_2.setText("2");
        mask_2_0.setText("1");
        mask_2_1.setText("2");
        mask_2_2.setText("1");
        doFilter();
    }
    public void setPrewittFilter(){
        mask_0_0.setText("1");
        mask_0_1.setText("1");
        mask_0_2.setText("1");
        mask_1_0.setText("0");
        mask_1_1.setText("0");
        mask_1_2.setText("0");
        mask_2_0.setText("-1");
        mask_2_1.setText("-1");
        mask_2_2.setText("-1");
        doFilter();
    }
    private void doFilter(){
        setMask();
        filters.setMask(mask);
        BufferedImage img = filters.filterImage(ImageSharedOperations.convertIconToImage(getImageIcon()));
        JFrame jFrame = new JFrame();
        JLabel jLabel = new JLabel();
        jLabel.setSize(img.getWidth(),img.getHeight());
        jLabel.setIcon(new ImageIcon(img));
        jFrame.add(jLabel);
        jFrame.setSize(img.getWidth(),img.getHeight());
        jFrame.show();
    }

}
