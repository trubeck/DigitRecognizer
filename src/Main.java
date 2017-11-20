import java.io.File;

public class Main {
    
    // parameter
    private static final String PARAM_HELP = "--help";
    private static final String PARAM_HELP_SHORT = "-h";
    private static final String PARAM_DEBUG = "--debug";
    private static final String PARAM_DEBUG_SHORT = "-d";
    private static final String PARAM_INPUT = "--input";
    private static final String PARAM_INPUT_SHORT = "-i";
    
    private static String inputFilepath = null;
    static boolean debug = false;
    
    public static void main(String[] args) {
    
        // arg handling
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
            
                switch (args[i]) {
                    // help
                    case PARAM_HELP_SHORT:
                    case PARAM_HELP:
                        //showhelp()
                        break;
    
                    case PARAM_DEBUG_SHORT:
                    case PARAM_DEBUG:
                        debug = true;
                        break;
        
                        // input filepath
                    case PARAM_INPUT_SHORT:
                    case PARAM_INPUT:
                        i++;
                        inputFilepath = args[i];
                        break;
                        
                        
                
                    default:
                        System.out.println("unknown parameter: \"" + args[i] + "\"\n");
                        System.exit(-1);
                }
            }
        
        }

        if (inputFilepath != null) {
            int[][][] numberSet = new Parser().parseImage(new File(inputFilepath));
        }
        
    }
    
}
