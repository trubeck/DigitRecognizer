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
//        if (imagesFilepath != null) {
//            int[][][] numberSet = new Parser().parseImage(new File(imagesFilepath));
//        }

        Network net = new Network(new int[]{4, 5, 5, 2});
        System.out.println("net = " + Arrays.toString(net.LAYER_SIZE));
        System.out.println("net.feedForward() = " + Arrays.toString(net.feedForward(new double[] {1d, 2d, 3d, 4d})));

    }

    static void log(String msg) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        System.out.println(formatter.format(date) + " " + msg);
    }

}
