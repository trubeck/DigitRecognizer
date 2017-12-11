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

    // [layer][toNeuron]
    private double[][] bias;

    // output of a specific neuron [layer][neuron]
    double[][] output;
    
    // TODO: NEW VARIABLES
    private double[][] error;
    private double[][] derivative;
    
    
    Network(int[] layer_size) {
        this.LAYER_SIZE = layer_size;
        this.NUM_LAYERS = layer_size.length;
        this.INPUT_SIZE = layer_size[0];
        this.OUTPUT_SIZE = layer_size[NUM_LAYERS - 1];

        output = new double[NUM_LAYERS][];
        weights = new double[NUM_LAYERS][][];
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
            bias[layer][j] = -0.3 + Math.random() * 0.6;
            }
            
            // randomize weights
            if (layer > 0) {
                weights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
                for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                    for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                        weights[layer][prevNeuron][neuron] = -0.3 + Math.random() * 0.6;
                    }
                }
            }
        }
    }

    void trainSubSet(double[][] input, double[][] target, int start, int end, double learningRate) {
        //adapt input
        double[][] subsetInput = new double[input.length][end - start];
        double[][] subsetTarget = new double[target.length][end - start];
        for (int i = start; i < end; i++) {
            subsetInput[i] = input[start+i];
            subsetTarget[i] = target[start+i];
        }


        trainAll(subsetInput, subsetTarget, learningRate);
    }

    void trainAll(double[][] input, double[][] target, double learningRate) {
        for (int i = 0; i < input.length; i++) {
            train(input[i], target[i], learningRate);
        }
    }
    
    void train(double[] input, double[] target, double learningRate) {
        if (input.length == INPUT_SIZE) {
            feedForward(input);
            learnBP(target);
            updateweightsBP(learningRate);
        }
    }
    
    double[] feedForward(double[] input) {
        if (input.length != INPUT_SIZE) {
            log("feedForward(): input size does not equal first networklayer size!");
            log("input: " + input.length);
            log("layersize: " + input.length);
            return null;
        }
        this.output[0] = input;
        
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {

                // bias
                double sum = bias[layer][neuron];

                // sum of prev neuron outputs
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][prevNeuron][neuron];
                }
                output[layer][neuron] = sigmoid(sum);
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

    private void updateweightsBP(double learningRate) {
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    weights[layer][prevNeuron][neuron] += -learningRate * output[layer - 1][prevNeuron] * error[layer][neuron];
                }
                bias[layer][neuron] += -learningRate * error[layer][neuron];
            }

        }
    }

    //TODO: ask if delta == d
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

    // ERS
    // TODO: ist c die Lernrate?
    // TODO: w_ij(t) ist eine Funktion?
    private void updateweightsERS(double c) {
        for (int layer = 1; layer < NUM_LAYERS; layer++) {
            for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                    weights[layer][prevNeuron][neuron] += c * Math.abs(1 - Math.abs(weights[0][0][0])) * error[layer][neuron] * signum(output[layer - 1][prevNeuron]);
                }
                bias[layer][neuron] += c * error[layer][neuron];
            }

        }
    }

    void testing(double[][] images, double[][] labels){
        int correct = 0;
        double rate = 0;
        for (int i = 0; i < images.length; i++) {
            double[] temp = feedForward(images[i]);
            if (findMax(temp) == findMax(labels[i])) {
                correct++;
                rate += temp[findMax(temp)];
            }
        }

        System.out.println("Correct: " + correct + "/" + images.length);
        System.out.println("Correctness: " + round(rate/(double)(images.length) * 100, 2) + "%");
    }
    
    private int findMax(double[] array){
        int max = -1;
        double maxValue = 0.0;
        for (int i = 0; i < array.length; i++) {
            if(array[i] > maxValue){
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
                    arr[i][j * images[0].length + k] = (double)images[i][k][j] * (1d/255d);
                }
            }

        }
        return arr;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if (Double.isNaN(value)) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void log(String msg) {
        Main.log(msg);
    }
    
}