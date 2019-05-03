package main.viewer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Load extends JFileChooser {

    public Load(){
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String fileName = f.getName().toLowerCase();
                if(fileName.endsWith(".jpg") || fileName.endsWith(".png")
                        || fileName.endsWith(".tiff") || fileName.endsWith(".bmp")) {
                    return true;
                } else return false;
            }

            @Override
            public String getDescription() {
                return "Image files (.jpg, .png, .tiff, .bmp)";
            }
        });
    }
}
