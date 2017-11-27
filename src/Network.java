import java.util.Random;

class Network {

    // number of layers
    final int NUM_LAYERS;
    
    // number of neurons in input layer
    final int INPUT_SIZE;
    
    // number of neurons in output layer
    final int OUTPUT_SIZE;
    
    // number of neurons in each layer
    final int[] LAYER_SIZE;

    // [layer][neuron prevlayer][neuron currentlayer]
    private double[][][] weights;

    // [layer][toNeuron]
    private double[][] bias;

    // output of a specific neuron [layer][neuron]
    private double[][] output;

    Network(int[] layer_size) {
        this.LAYER_SIZE = layer_size;
        this.NUM_LAYERS = layer_size.length;
        this.INPUT_SIZE = layer_size[0];
        this.OUTPUT_SIZE = layer_size[NUM_LAYERS - 1];

        output = new double[NUM_LAYERS][];
        weights = new double[NUM_LAYERS][][];
        bias = new double[NUM_LAYERS][];
        
        // 0 init
        for (int layer = 0; layer < NUM_LAYERS; layer++) {
            output[layer] = new double[LAYER_SIZE[layer]];

            // randomize biases
            bias[layer] = new double[LAYER_SIZE[layer]];
            for (int j = 0; j < LAYER_SIZE[layer]; j++) {
            bias[layer][j] = Math.random();
            }
            
            // randomize weights
            if (layer > 0) {
                weights[layer] = new double[LAYER_SIZE[layer - 1]][LAYER_SIZE[layer]];
                for (int neuron = 0; neuron < LAYER_SIZE[layer]; neuron++) {
                    for (int prevNeuron = 0; prevNeuron < LAYER_SIZE[layer - 1]; prevNeuron++) {
                        weights[layer][prevNeuron][neuron] = Math.random();
                    }

                }

            }
        }

    }

    double[] feedForward(double[] input) {
        if (input.length != INPUT_SIZE) return null;
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

            }
        }

        return output[NUM_LAYERS - 1];
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

}