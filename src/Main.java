import sun.nio.ch.Net;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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


        Network net = new Network(new int[]{28 * 28, 16, 16, 10});

        double[][] imageSet = null;
        if (imagesFilepath != null) {
            imageSet = net.convertImages(new Parser().parseImage(new File(imagesFilepath)));
        }
        else {
            log("Alles kacke");
            System.exit(-1);
        }

        double[][] labelSet = null;
        if (labelsFilepath != null) {
            labelSet = net.convertLables(new Parser().parseLabel(new File(labelsFilepath)));
        }
        else {
            log("Alles kacke");
            System.exit(-1);
        }

//        double[] input = new double[] {0.1, 0.8, 0.4, 0.7};
//        double[] target = new double[] {0, 1};


//        net.convertImages(numberSet);

//        for (int i = 0; i < 100; i++) {
//            net.train(input, target, 0.3);
//        }

        int bla = 10;
        for (int i = 0; i < bla; i++) {
//            if (i != 0 && i % (bla / 10) == 0) {
//                log("gelernt: " + ((double)i / (double)bla* 100) + "% done", true);
//            }
            net.trainAll(imageSet, labelSet, 0.3);
        }

        System.out.println("net = " + Arrays.toString(net.LAYER_SIZE));
        System.out.println(Arrays.toString(labelSet[0]) + " so soll sein");
        System.out.println("net.feedForward() = " + Arrays.toString(net.feedForward(imageSet[0])));

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
