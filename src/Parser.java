
import java.io.*;

public class Parser {

    public int[][][] parseImage(File f){
        try {
            FileInputStream images = new FileInputStream(f);

            int magicNumberImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfImages = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfRows  = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            int numberOfColumns = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());
            System.out.println(magicNumberImages + ", "+ numberOfImages + ", "+ numberOfRows +", "+ numberOfColumns);




        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int[] parseLabel(File f){

        return null;
    }
}
