import KNN.AlgoClassification;
import KNN.KNN;
import Donnees.Chargement;
import Donnees.Donnees;
import Donnees.Imagette;

import java.io.IOException;

public class Part1MainKnn {

    //Nombres MNIST dataset
    public static final String NOMBRES_TRAIN_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-images.idx3-ubyte";
    public static final String NOMBRES_TRAIN_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-labels.idx1-ubyte";
    public static final String NOMBRES_TEST_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-images.idx3-ubyte";
    public static final String NOMBRES_TEST_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-labels.idx1-ubyte";

    //Fashion MNIST dataset
    public static final String FASHION_TRAIN_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-images-idx3-ubyte";
    public static final String FASHION_TRAIN_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-labels-idx1-ubyte";
    public static final String FASHION_TEST_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-images-idx3-ubyte";
    public static final String FASHION_TEST_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-labels-idx1-ubyte";


    public static void main(String[] args) throws IOException {
        System.out.println("Début du chargement des imagettes...");
        //Donnees trainData = Chargement.charger(NOMBRES_TRAIN_IMAGES, NOMBRES_TRAIN_LABELS);
        Donnees trainData = Chargement.charger(FASHION_TRAIN_IMAGES, FASHION_TRAIN_LABELS);
        System.out.println("Chargement des imagettes d'entrainement OK");
        //Donnees testData = Chargement.charger(NOMBRES_TEST_IMAGES, NOMBRES_TEST_LABELS);
        Donnees testData = Chargement.charger(FASHION_TEST_IMAGES, FASHION_TEST_LABELS);
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
