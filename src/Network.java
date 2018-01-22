import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Network {

    // number of layers
    private final int NUM_LAYERS;
    
    // number of neurons in input layer
    private final int INPUT_SIZE;
    
    // number of neurons in output layer
    private final int OUTPUT_SIZE;
    
    // number of neurons in each layer
    final int[] LAYER_SIZE;

    // [layer][neuron prevlayer][neuron currentlayer]
    double[][][] weights;
    double[][][] deltaweights; // difference last iteration

    // [layer][toNeuron]
    private double[][] bias;

    // output of a specific neuron [layer][neuron]
    double[][] output;
    
    // TODO: NEW VARIABLES
    private double[][] error;
    private double[][] derivative;
    

    Network(String filename) throws IOException {


        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line = br.readLine();
        NUM_LAYERS = Integer.parseInt(line.split(",")[0]);
        INPUT_SIZE = Integer.parseInt(line.split(",")[1]);
        OUTPUT_SIZE = Integer.parseInt(line.split(",")[2]);

        //TODO: Check if readline == null
        line = br.readLine();
        LAYER_SIZE = new int[NUM_LAYERS];
        String[] arr = line.split(",");
        for (int i = 0; i < NUM_LAYERS; i++) {
            LAYER_SIZE[i] = Integer.parseInt(arr[i]);
        }

        line = br.readLine();
        arr = line.split(",");

        // weight init
        weights = new double[NUM_LAYERS][][];
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            weights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
        }

        // weight read
        int counter = 0;
        for (int i = 1; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i - 1]; j++) {
                for (int k = 0; k < LAYER_SIZE[i]; k++) {
                    weights[i][j][k] = Double.parseDouble(arr[counter]);
                    counter++;
                }
            }
        }

        line = br.readLine();
        arr = line.split(",");

        // deltaweight init
        deltaweights = new double[NUM_LAYERS][][];
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            deltaweights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
        }

        // deltaweight read
        counter = 0;
        for (int i = 1; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i - 1]; j++) {
                for (int k = 0; k < LAYER_SIZE[i]; k++) {
                    deltaweights[i][j][k] = Double.parseDouble(arr[counter]);
                    counter++;
                }
            }
        }
    
        line = br.readLine();
        arr = line.split(",");
    
        // bias init
        bias = new double[NUM_LAYERS][];
        for (int i = 0; i < NUM_LAYERS; i++) {
            bias[i] = new double[LAYER_SIZE[i]];
        }
    
        // bias read
        counter = 0;
        for (int i = 0; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i]; j++) {
                bias[i][j] = Double.parseDouble(arr[counter]);
                counter++;
            }
        }
    
        line = br.readLine();
        arr = line.split(",");
    
        // output init
        output = new double[NUM_LAYERS][];
        for (int i = 0; i < NUM_LAYERS; i++) {
            output[i] = new double[LAYER_SIZE[i]];
        }
    
        // output read
        counter = 0;
        for (int i = 0; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i]; j++) {
                output[i][j] = Double.parseDouble(arr[counter]);
                counter++;
            }
        }
    
        line = br.readLine();
        arr = line.split(",");
    
        // error init
        error = new double[NUM_LAYERS][];
        for (int i = 0; i < NUM_LAYERS; i++) {
            error[i] = new double[LAYER_SIZE[i]];
        }
    
        // error read
        counter = 0;
        for (int i = 0; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i]; j++) {
                error[i][j] = Double.parseDouble(arr[counter]);
                counter++;
            }
        }
    
        line = br.readLine();
        arr = line.split(",");
    
        // derivative init
        derivative = new double[NUM_LAYERS][];
        for (int i = 0; i < NUM_LAYERS; i++) {
            derivative[i] = new double[LAYER_SIZE[i]];
        }
    
        // derivative read
        counter = 0;
        for (int i = 0; i < NUM_LAYERS; i++) {
            for (int j = 0; j < LAYER_SIZE[i]; j++) {
                derivative[i][j] = Double.parseDouble(arr[counter]);
                counter++;
            }
        }

        br.close();


    }

    Network(int[] layer_size) {
        this.LAYER_SIZE = layer_size;
        this.NUM_LAYERS = layer_size.length;
        this.INPUT_SIZE = layer_size[0];
        this.OUTPUT_SIZE = layer_size[NUM_LAYERS - 1];

        output = new double[NUM_LAYERS][];
        weights = new double[NUM_LAYERS][][];
        deltaweights = new double[NUM_LAYERS][][];
        bias = new double[NUM_LAYERS][];
        error = new double[NUM_LAYERS][];
        derivative = new double[NUM_LAYERS][];
        
        // 0 init
        for (int layer = 0; layer < NUM_LAYERS; layer++) {
            output[layer] = new double[LAYER_SIZE[layer]];
            error[layer] = new double[LAYER_SIZE[layer]];
            derivative[layer] = new double[LAYER_SIZE[layer]];

            // randomize biases
            bias[layer] = new double[LAYER_SIZE[layer]];
            for (int j = 0; j < LAYER_SIZE[layer]; j++) {
                bias[layer][j] = -1 + Math.random() * 2;
            }
            
            // randomize weights
            if (layer > 0) {
                weights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
                for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                    for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                        weights[layer][prevNeuron][neuron] = -1 + Math.random() * 2;
                    }
                }
            }

            // init deltaweights with 0
            if (layer > 0) {
                deltaweights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
                for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                    for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                        deltaweights[layer][prevNeuron][neuron] = 0d;
                    }
                }
            }
        }
    }

    void trainAll(double[][] input, double[][] target, double learningRate, String learningMethod, double momentum) {
        for (int i = 0; i < input.length; i++) {
            log("i = " + i);
            train(input[i], target[i], learningRate, learningMethod, momentum);
        }
    }
    
    private void train(double[] input, double[] target, double learningRate, String learningMethod, double momentum) {
        if (learningMethod.equals(Main.BP)) {
            if (input.length == INPUT_SIZE) {
                feedForward(input, learningMethod);
                learnBP(target);
                updateweightsBP(learningRate, momentum);
            }
        } else if (learningMethod.equals(Main.ERS)) {
            if (input.length == INPUT_SIZE) {
                feedForward(input, learningMethod);
                learnERS(target);
                updateweightsERS(learningRate, momentum);
            }
        } else if (learningMethod.equals(Main.ERS2)) {
            if (input.length == INPUT_SIZE) {
                // hier kommt ers2 hin
            }
        } else {
            log("Unknown learning Method!");
        }

    }
    
    private double[] feedForward(double[] input, String learningMethod) {
        if (input.length != INPUT_SIZE) {
            log("feedForward(): input size does not equal first networklayer size!");
            log("input: " + input.length);
            log("layersize: " + input.length);
            return null;
        }
        output[0] = input;
        
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {

                // bias
                double sum = bias[layer][neuron];

                // sum of prev neuron outputs
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][prevNeuron][neuron];
                }

                // use logistic for backprop
                if (learningMethod.equals(Main.BP)) {
                    output[layer][neuron] = sigmoid(sum);
                }
                // use tanh for ERS and ERS2
                else if (learningMethod.equals(Main.ERS) || learningMethod.equals(Main.ERS2)) {
                    output[layer][neuron] = Math.tanh(sum);
                }

                // for BP
                derivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
            }
        }

        return output[NUM_LAYERS - 1];
    }
    
    private void learnBP(double[] target) {
        if (target.length != OUTPUT_SIZE) {
            System.out.println("Wrong label size!");
            return;
        }
        
        // output layer
        for (int neuron = 0; neuron < OUTPUT_SIZE; neuron++) {
            //TODO: switch tarjet and output
            error[NUM_LAYERS - 1][neuron] = (output[NUM_LAYERS - 1][neuron] - target[neuron]) * derivative[NUM_LAYERS - 1][neuron];
        }
        
        // every hidden layer back to front
        for (int layer = NUM_LAYERS - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < LAYER_SIZE[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][neuron][nextNeuron] * error[layer + 1][nextNeuron];
                }
                error[layer][neuron] = sum * derivative[layer][neuron];
            }
        }
    }

    private void updateweightsBP(double learningRate, double momentum) {
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    double deltaweight = -learningRate * output[layer - 1][prevNeuron] * error[layer][neuron];
                    //weights[layer][prevNeuron][neuron] += (1 - momentum) * deltaweight + momentum * deltaweights[layer][prevNeuron][neuron];
                    weights[layer][prevNeuron][neuron] += deltaweight + momentum * deltaweights[layer][prevNeuron][neuron];
                    deltaweights[layer][prevNeuron][neuron] = deltaweight;
                }
                bias[layer][neuron] += -learningRate * error[layer][neuron];
            }

        }
    }


    // tanh implementieren
    private void updateweightsERS(double learningRate, double momentum) {
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    double deltaweight = learningRate * Math.abs(1 - Math.abs(weights[layer][prevNeuron][neuron])) * error[layer][neuron] * signum(output[layer - 1][prevNeuron]);
                    weights[layer][prevNeuron][neuron] += deltaweight + momentum * deltaweights[layer][prevNeuron][neuron];
                    deltaweights[layer][prevNeuron][neuron] = deltaweight;
                }
                bias[layer][neuron] += learningRate * error[layer][neuron];
            }
        }
    }

    private void learnERS(double[] target) {
        if (target.length != OUTPUT_SIZE) {
            System.out.println("Wrong label size!");
            return;
        }

        // output layer
        for (int neuron = 0; neuron < OUTPUT_SIZE; neuron++) {
            error[NUM_LAYERS - 1][neuron] = target[neuron] - output[NUM_LAYERS - 1][neuron];
        }

        // every hidden layer back to front
        for (int layer = NUM_LAYERS - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < LAYER_SIZE[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][neuron][nextNeuron] * error[layer + 1][nextNeuron];
                }
                error[layer][neuron] = sum;
            }

        }
    }

    void testing(double[][] images, double[][] labels, String learningMethod) {
        int correct = 0;
        double rate = 0;
        for (int i = 0; i < images.length; i++) {
            double[] temp = feedForward(images[i], learningMethod);
            if (findMax(temp) == findMax(labels[i])) {
                correct++;
                rate += temp[findMax(temp)];
            }
        }

        System.out.println("Correct: " + correct + "/" + images.length);
        System.out.println("Correctness: " + round(rate / (double) (images.length) * 100) + "%");
    }
    
    private int findMax(double[] array) {
        int max = -1;
        double maxValue = 0.0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxValue) {
                max = i;
                maxValue = array[i];
            }
        }
        return max;
    }
    
    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    private double signum(double x) {
        if (x < 0) return -1;
        else if (x == 0) return 0;
        else return 1;
    }

    double[][] convertLables(int[] labels) {
        double[][] target = new double[labels.length][10];
        for (int i = 0; i < labels.length; i++) {
            target[i][labels[i]] = 1;
        }
        return target;
    }
    
    double[][] convertImages(int[][][] images) {
        double[][] arr = new double[images.length][images[0].length * images[0][0].length];
        for (int i = 0; i < images.length; i++) {
            for (int j = 0; j < images[0].length; j++) {
                for (int k = 0; k < images[0][0].length; k++) {
                    arr[i][j * images[0].length + k] = (double) images[i][k][j] * (1d / 255d);
                }
            }

        }
        return arr;
    }

    private double round(double value) {
        if (Double.isNaN(value)) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void log(String msg) {
        Main.log(msg);
    }


    @SuppressWarnings("Duplicates")
    void saveNetwork() {
        try {
            FileWriter fileWriter = new FileWriter("savedNetwork.csv");
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(NUM_LAYERS));
            sb.append(",");
            sb.append(String.valueOf(INPUT_SIZE));
            sb.append(",");
            sb.append(String.valueOf(OUTPUT_SIZE));
            sb.append("\n");

            for (int i = 0; i < LAYER_SIZE.length; i++) {
                sb.append(String.valueOf(LAYER_SIZE[i]));
                    sb.append(",");
            }
            
            sb.setLength(sb.length() -1);
            sb.append("\n");

            for (int i = 1; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i - 1]; j++) {
                    for (int k = 0; k < LAYER_SIZE[i]; k++) {
                        sb.append(String.valueOf(weights[i][j][k]));
                        sb.append(",");
                        
                    }
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");
            
            for (int i = 1; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i - 1]; j++) {
                    for (int k = 0; k < LAYER_SIZE[i]; k++) {
                        sb.append(String.valueOf(deltaweights[i][j][k]));
                        sb.append(",");
                    }
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");

            for (int i = 0; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i]; j++) {
                    sb.append(String.valueOf(bias[i][j]));
                    sb.append(",");
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");

            for (int i = 0; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i]; j++) {
                    sb.append(String.valueOf(output[i][j]));
                    sb.append(",");
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");

            for (int i = 0; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i]; j++) {
                    sb.append(String.valueOf(error[i][j]));
                    sb.append(",");
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");

            for (int i = 0; i < NUM_LAYERS; i++) {
                for (int j = 0; j < LAYER_SIZE[i]; j++) {
                    sb.append(String.valueOf(derivative[i][j]));
                    sb.append(",");
                }
            }

            sb.setLength(sb.length() -1);
            sb.append("\n");

            fileWriter.append(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}