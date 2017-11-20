import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");

        new Parser().parseImage(new File("./data/train-images.idx3-ubyte"));
    }
}
