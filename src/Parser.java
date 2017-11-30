
import java.io.*;

class Parser {
    
    int[][][] parseImage(File f) {
        try {
            FileInputStream images = new FileInputStream(f);
            
            int magicNumberImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfRows = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfColumns = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());

            log("magicNumberImages = " + magicNumberImages);
            log("numberOfImages = " + numberOfImages);
            log("numberOfRows = " + numberOfRows);
            log("numberOfColumns = " + numberOfColumns);

            int[][][] numberSet = new int[numberOfImages][numberOfRows][numberOfColumns];
            
            for (int numImage = 0; numImage < numberOfImages; numImage++) {
                for (int numCol = 0; numCol < numberOfColumns; numCol++) {
                    for (int numRow = 0; numRow < numberOfRows; numRow++) {
                        numberSet[numImage][numRow][numCol] = images.read();

                    }
                }
                // output progress
                if (numImage != 0 && numImage % (numberOfImages / 10) == 0) {
                    log("Parsing images: " + ((double)numImage / (double)numberOfImages * 100) + "% done", true);
                }
            }
            
            log("Image parsing done", true);
            
            
            return numberSet;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    int[] parseLabel(File f) {
        try {
            FileInputStream labels = new FileInputStream(f);
            
            int magicNumberLabels = (labels.read() << 24) | (labels.read() << 16) | (labels.read() << 8) | (labels.read());
            int numberOfLabels = (labels.read() << 24) | (labels.read() << 16) | (labels.read() << 8) | (labels.read());
            
            int[] labelSet = new int[numberOfLabels];
            
            for (int numLabels = 0; numLabels < numberOfLabels; numLabels++) {
                labelSet[numLabels] = labels.read();

                log("" + labelSet[numLabels]);

            }
            
            log("Label parsing done", true);
            
            return labelSet;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private void log(String msg, boolean debug) {
        Main.log(msg, debug);
    }

    private void log(String msg) {
        Main.log(msg);
    }
    
}
