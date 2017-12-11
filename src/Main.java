import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    // parameter
    private static final String PARAM_HELP = "--help";
    private static final String PARAM_HELP_SHORT = "-h";
    private static final String PARAM_DEBUG = "--debug";
    private static final String PARAM_DEBUG_SHORT = "-d";
    private static final String PARAM_IMAGES = "--image";
    private static final String PARAM_IMAGES_SHORT = "-i";
    private static final String PARAM_LABELS = "--labels";
    private static final String PARAM_LABELS_SHORT = "-l";

    private static String imagesFilepath = null;
    private static String labelsFilepath = null;
    static boolean debug = false;

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // call save method here
        }));

        // arg handling
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {

                switch (args[i]) {
                    // help
                    case PARAM_HELP_SHORT:
                    case PARAM_HELP:
                        // TODO: implement showhelp()
                        break;

                    // debug
                    case PARAM_DEBUG_SHORT:
                    case PARAM_DEBUG:
                        debug = true;
                        break;

                    // images input
                    case PARAM_IMAGES_SHORT:
                    case PARAM_IMAGES:
                        i++;
                        imagesFilepath = args[i];
                        break;

                    // labels input
                    case PARAM_LABELS_SHORT:
                    case PARAM_LABELS:
                        i++;
                        labelsFilepath = args[i];
                        break;

                    default:
                        System.out.println("unknown parameter: \"" + args[i] + "\"\n");
                        // showHelp();
                        System.exit(-1);
                }
            }

        }


        Network net = new Network(new int[]{28 * 28, 30, 30, 10});
        int setSize = 60000;
        int batchSize = 5000;

        for (int i = 0; i < Math.abs(setSize/batchSize); i++) {
    
            log("----------", true);
            log("== BATCH " + (i + 1) + "/" + (Math.abs(setSize/batchSize)) + "           " +  (i*batchSize) + " to " + (i*batchSize+batchSize) + " ==", true);
            log("----------", true);
            
            double[][] imageSet = null;
            if (imagesFilepath != null) {
                imageSet = net.convertImages(new Parser().parseImage(new File(imagesFilepath), i*batchSize, i*batchSize+batchSize));
            }
            else {
                log("no filepath for images given");
                System.exit(-1);
            }

            double[][] labelSet = null;
            if (labelsFilepath != null) {
                labelSet = net.convertLables(new Parser().parseLabel(new File(labelsFilepath), i*batchSize, i*batchSize+batchSize));
            }
            else {
                log("no filepath for labels given");
                System.exit(-1);
            }


            int bla = 10;

            for (int j = 0; j < bla; j++) {
                if (j != 0 && j % (bla / 10) == 0) {
                    log("learning: " + ((double)j / (double)bla* 100) + "% done", true);
                }
                net.trainAll(imageSet, labelSet, 0.3);
            }

        }
    
        double[][] imageSet = null;
        if (imagesFilepath != null) {
            imageSet = net.convertImages(new Parser().parseImage(new File(imagesFilepath), 0, 1000));
        }
        else {
            log("no filepath for images given");
            System.exit(-1);
        }
    
        double[][] labelSet = null;
        if (labelsFilepath != null) {
            labelSet = net.convertLables(new Parser().parseLabel(new File(labelsFilepath), 0, 1000));
        }
        else {
            log("no filepath for labels given");
            System.exit(-1);
        }

        net.testing(imageSet, labelSet);



    }
    static void printLabel(double[] label) {
        for (int i = 0; i < label.length; i++) {
            if (label[i] == 1.0){
                System.out.println(i);
            }

        }
    }

    static void printImage(double[] image) {
        for (int i = 0; i < 28*28; i++) {
            if (image[i] == 0.0){
                System.out.print(".");
            }
            else if (image[i] > .5) {
                System.out.print("#");
            }
            else {
                System.out.print("O");
            }
            if (i != 0 && i % 28 == 0){
                System.out.println("");
            }
        }
    }

    static void log(String msg, boolean debug) {
        if (debug) {
            Date date = new Date(System.currentTimeMillis());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
            System.out.println(formatter.format(date) + " " + msg);
        }
    }

    static void log(String msg) {
        if (debug) {
            Date date = new Date(System.currentTimeMillis());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
            System.out.println(formatter.format(date) + " " + msg);
        }
    }

}
