import MLP.MLP;
import MLP.Sigmoide;
import MLP.TangenteHyperbolique;
import MLP.TransferFunction;

import java.util.Random;

public class Main_MLP_binaire {
    public static void main(String[] args) {
        double learningRate = 0.1;

        // Données d'entrée
        double[][] inputs= {
                {0.0, 0.0},
                {0.0, 1.0},
                {1.0, 0.0},
                {1.0, 1.0}
        };
        // Sorties AND - 1 Neurone
        double[][] outputsAND1Neurone = {
                {0.0},
                {0.0},
                {0.0},
                {1.0}
        };
        // Sorties AND - 2 Neurone
        double[][] outputsAND2Neurone = {
                {0.0, 0.0},  // Classe 0
                {0.0, 0.0},  // Classe 0
                {0.0, 0.0},  // Classe 0
                {1.0, 1.0}   // Classe 1
        };

        // Sorties OR - 1 Neurone
        double[][] outputsOR1Neurone = {
                {0.0},
                {1.0},
                {1.0},
                {1.0}
        };

        // Sorties OR - 2 Neurone
        double[][] outputsOR2Neurones = {
                {0.0, 1.0},  // Classe 0
                {1.0, 0.0},  // Classe 1
                {1.0, 0.0},  // Classe 1
                {1.0, 0.0}   // Classe 1
        };

        // Sorties XOR - 1 Neurone
        double[][] outputsXOR1Neurone = {
                {0.0},
                {1.0},
                {1.0},
                {0.0}
        };

        // Sorties XOR - 2 Neurones (One-Hot Encoding)
        double[][] outputsXOR2Neurones = {
                {0.0, 1.0},  // Classe 0
                {1.0, 0.0},  // Classe 1
                {1.0, 0.0},  // Classe 1
                {0.0, 1.0}   // Classe 0
        };

        // Définition des architectures (1 et 2 neurones en sortie)
        int[][] architectures = {
                {2, 4, 1},  // 1 neurone en sortie
                {2, 4, 2}   // 2 neurones en sortie
        };

        // Définition des fonctions d'activation
        TransferFunction[] activationFunctions = {
                new Sigmoide(),
                new TangenteHyperbolique()
        };

        // Boucle pour tester toutes les combinaisons
        for (int[] arch : architectures) {
            for (TransferFunction activationFunction : activationFunctions) {
                // Mélanger les données avant chaque entraînement
//                shuffleData(inputs, arch[arch.length - 1] == 1 ? outputsXOR1Neurone : outputsXOR2Neurones);

                // Création du MLP avec la bonne architecture et fonction d'activation
                MLP mlp = new MLP(arch, learningRate, activationFunction);
                String activationName = activationFunction.getClass().getSimpleName();
                String outputType = arch[arch.length - 1] == 1 ? "1 Neurone" : "2 Neurones";



                System.out.println("\n### Test AND avec " + outputType + " - Activation : " + activationName + " ###");
                if (arch[arch.length - 1] == 1) {
                    train1Neurone(mlp, inputs, outputsAND1Neurone);
                }
                else train2Neurones(mlp, inputs, outputsAND2Neurone);

                mlp = new MLP(arch, learningRate, activationFunction);
                System.out.println("\n### Test OR avec " + outputType + " - Activation : " + activationName + " ###");
                if (arch[arch.length - 1] == 1) {
                    train1Neurone(mlp, inputs, outputsOR1Neurone);
                }
                else {
                    train2Neurones(mlp, inputs, outputsOR2Neurones);
                }

                System.out.println("\n### Test XOR avec " + outputType + " - Activation : " + activationName + " ###");
                if (arch[arch.length - 1] == 1) {
                    train1Neurone(mlp, inputs, outputsXOR1Neurone);
                } else {
                    train2Neurones(mlp, inputs, outputsXOR2Neurones);
                }
            }
        }

    }

    // Entraînement 1 Neurone
    private static void train1Neurone(MLP mlp, double[][] inputs, double[][] outputs) {
        int maxEpochs = 25000;
        double tolerance = 0.001;


        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            double totalError = 0.0;
            boolean allCorrect = true;

            for (int i = 0; i < inputs.length; i++) {
                totalError += mlp.backPropagate(inputs[i], outputs[i]);
                double[] output = mlp.execute(inputs[i]);

                if (Math.abs(output[0] - outputs[i][0]) > tolerance) {
                    allCorrect = false;
                }
            }

            if (epoch % 1000 == 0) {
                System.out.println("Époque " + epoch + " - Erreur moyenne : " + totalError / inputs.length);
            }

            if (allCorrect) {
                System.out.println("Tous les exemples sont réussis à l'époque " + epoch + ".");
                break;
            }
        }

        // Affichage des résultats finaux
        System.out.println("\n🔹 Résultats finaux (1 Neurone) :");
        for (int i = 0; i < inputs.length; i++) {
            double[] output = mlp.execute(inputs[i]);
            System.out.println("Entrée : " + inputs[i][0] + ", " + inputs[i][1] +
                    " - Sortie : " + output[0] +
                    " (Attendu : " + outputs[i][0] + ")");
        }


        System.out.println();
    }

    // Entraînement 2 Neurones
    private static void train2Neurones(MLP mlp, double[][] inputs, double[][] outputs) {
        int maxEpochs = 25000;


        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            double totalError = 0.0;

            for (int i = 0; i < inputs.length; i++) {
                totalError += mlp.backPropagate(inputs[i], outputs[i]);
                double[] output = mlp.execute(inputs[i]);
            }

            if (epoch % 1000 == 0) {
                System.out.println("Époque " + epoch + " - Erreur moyenne : " + totalError / inputs.length);
            }
        }

        // Affichage des résultats finaux
        System.out.println("\n🔹 Résultats finaux (2 Neurones) :");
        for (int i = 0; i < inputs.length; i++) {
            double[] output = mlp.execute(inputs[i]);
            int predictedClass = (output[0] > output[1]) ? 1 : 0;
            System.out.println("Entrée : " + inputs[i][0] + ", " + inputs[i][1] +
                    " - Sortie : [" + output[0] + ", " + output[1] + "]" +
                    " (Classe prédite : " + predictedClass + ") "
            );
        }
        System.out.println();
    }

    // Mélange des données
    private static void shuffleData(double[][] inputs, double[][] outputs) {
        Random rand = new Random();
        int n = inputs.length;

        // Create an array of indices
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        // Shuffle the indices
        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = indices[i];
            indices[i] = indices[j];
            indices[j] = temp;
        }

        double[][] shuffledInputs = new double[n][inputs[0].length];
        double[][] shuffledOutputs = new double[n][outputs[0].length];

        for (int i = 0; i < n; i++) {
            shuffledInputs[i] = inputs[indices[i]];
            shuffledOutputs[i] = outputs[indices[i]];
        }

        for (int i = 0; i < n; i++) {
            inputs[i] = shuffledInputs[i];
            outputs[i] = shuffledOutputs[i];
        }
    }
}
