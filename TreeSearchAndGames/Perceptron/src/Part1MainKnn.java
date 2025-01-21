import KNN.AlgoClassification;
import KNN.KNN;
import Donnees.Chargement;
import Donnees.Donnees;
import Donnees.Imagette;

import java.io.IOException;

public class Part1MainKnn {

    public static final String NOMBRES_TRAIN_IMAGES = "TreeSearchAndGames/Perceptron/Data/Nombre/train-images.idx3-ubyte";
    public static final String NOMBRES_TRAIN_LABELS = "TreeSearchAndGames/Perceptron/Data/Nombre/train-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
        System.out.println("Début du chargement des imagettes...");
        Donnees testData = Chargement.charger(NOMBRES_TRAIN_IMAGES, NOMBRES_TRAIN_LABELS);
        System.out.println("Première imagette : " + testData.getImagette(0).getLabel());
        System.out.println("Dernière imagette : " + testData.getImagette(testData.getImagettes().length -1).getLabel());
        System.out.println("Chargement des imagettes OK");
        System.out.println();

        AlgoClassification algo = new KNN(testData, 10);

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
        System.out.println("Nombre d'echec : " + (testData.getImagettes().length - nbCorrect));
    }
}
