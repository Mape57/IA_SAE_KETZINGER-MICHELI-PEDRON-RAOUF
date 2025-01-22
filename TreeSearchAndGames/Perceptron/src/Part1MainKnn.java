import KNN.AlgoClassification;
import KNN.KNN;
import Donnees.Chargement;
import Donnees.Donnees;
import Donnees.Imagette;

import java.io.IOException;

public class Part1MainKnn {
    public static final String NOMBRES_TRAIN_IMAGES = "Perceptron/Data/Nombre/train-images.idx3-ubyte";
    public static final String NOMBRES_TRAIN_LABELS = "Perceptron/Data/Nombre/train-labels.idx1-ubyte";
    public static final String NOMBRES_TEST_IMAGES = "Perceptron/Data/Nombre/t10k-images.idx3-ubyte";
    public static final String NOMBRES_TEST_LABELS = "Perceptron/Data/Nombre/t10k-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
        System.out.println("Début du chargement des imagettes...");
        Donnees trainData = Chargement.charger(NOMBRES_TRAIN_IMAGES, NOMBRES_TRAIN_LABELS);
        System.out.println("Chargement des imagettes d'entrainement OK");
        Donnees testData = Chargement.charger(NOMBRES_TEST_IMAGES, NOMBRES_TEST_LABELS);
        System.out.println("Chargement des imagettes de test OK\n");

        AlgoClassification algo = new KNN(trainData, 10);

        int nbCorrect = 0;
        System.out.println("Début de la prédiction...");
        for (int i = 0; i < testData.getImagettes().length; i++) {
            Imagette img = testData.getImagette(i);
            if (algo.predire(img) == img.getLabel()) {
                nbCorrect++;
            }
            if (i%100==0) System.out.println("Prédiction faite pour " + i + " imagettes (" + nbCorrect + " correctes)");
        }
        System.out.println();

        System.out.println("Pourcentage de reussite : " + (nbCorrect * 100 / testData.getImagettes().length) + "%");
        System.out.println("Nombre de reussite : " + nbCorrect);
        System.out.println("Nombre d'echec : " + (trainData.getImagettes().length - nbCorrect));
    }
}
