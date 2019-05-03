package main.viewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Save extends JFileChooser
{

    public Save(){
        setVisible(true);

        setDialogTitle("SAVE");



        addChoosableFileFilter(new  FileNameExtensionFilter(".jpg","jpg"));
        addChoosableFileFilter(new FileNameExtensionFilter(".bmp","bmp"));
        addChoosableFileFilter(new FileNameExtensionFilter(".png","png"));
        addChoosableFileFilter(new FileNameExtensionFilter(".tiff","tiff"));
        setAcceptAllFileFilterUsed(true);

        showDialog(null,"Save");


    }
}
