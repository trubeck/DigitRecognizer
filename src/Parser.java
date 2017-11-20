
import java.io.*;

public class Parser {

    int[][][] parseImage(File f) {
        try {
            FileInputStream images = new FileInputStream(f);

            int magicNumberImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfRows  = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfColumns = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            System.out.println(magicNumberImages + ", "+ numberOfImages + ", "+ numberOfRows +", "+ numberOfColumns);

            int[][][] numberSet = new int[numberOfImages][numberOfRows][numberOfColumns];
    
            for (int numImage = 0; numImage < numberOfImages; numImage++) {
                for (int numCol = 0; numCol < numberOfColumns; numCol++) {
                    for (int numRow = 0; numRow < numberOfRows; numRow++) {
                        numberSet[numImage][numRow][numCol] = images.read();
                        if (Main.debug) {
                            if (numberSet[numImage][numRow][numCol] <= 0xF) {
                                System.out.print("0" + Integer.toHexString(numberSet[numImage][numRow][numCol]));
                            }
                            else {
                                System.out.print(Integer.toHexString(numberSet[numImage][numRow][numCol]));
                            }
                        }
                    }
                    if (Main.debug) {
                        System.out.println();
                    }
                }
            }
            return numberSet;
    
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[] parseLabel(File f) {
        try {
            FileInputStream labels = new FileInputStream(f);
    
            int magicNumberLabels = (labels.read() << 24) | (labels.read() << 16) | (labels.read() << 8) | (labels.read());
            int numberOfLabels = (labels.read() << 24) | (labels.read() << 16) | (labels.read() << 8) | (labels.read());
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
}
