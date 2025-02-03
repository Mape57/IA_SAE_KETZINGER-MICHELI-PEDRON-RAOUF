import KNN.AlgoClassification;
import KNN.KNN;
import Donnees.Chargement;
import Donnees.Donnees;
import Donnees.Imagette;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Part1MainKnn {

    //public static final String NOMBRES_TRAIN_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-images.idx3-ubyte";
    //public static final String NOMBRES_TRAIN_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-labels.idx1-ubyte";
    //public static final String NOMBRES_TEST_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-images.idx3-ubyte";
    //public static final String NOMBRES_TEST_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-labels.idx1-ubyte";

    public static final String FASHION_TRAIN_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-images-idx3-ubyte";
    public static final String FASHION_TRAIN_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-labels-idx1-ubyte";
    public static final String FASHION_TEST_IMAGES = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-images-idx3-ubyte";
    public static final String FASHION_TEST_LABELS = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-labels-idx1-ubyte";

    public static void main(String[] args) throws IOException {
        int[] valeursK = {3, 5, 10};  // Test de différentes valeurs de k
        int limit = 1000;

        //String imagesEntrainement = NOMBRES_TRAIN_IMAGES
        //String etiquettesEntrainement = NOMBRES_TRAIN_LABELS;
        //String imagesTest = NOMBRES_TEST_IMAGES;
        //String etiquettesTest = NOMBRES_TEST_LABELS;

        String imagesEntrainement = FASHION_TRAIN_IMAGES;
        String etiquettesEntrainement = FASHION_TRAIN_LABELS;
        String imagesTest = FASHION_TEST_IMAGES;
        String etiquettesTest = FASHION_TEST_LABELS;

        Donnees donneesEntrainement = Chargement.charger(imagesEntrainement, etiquettesEntrainement, limit);
        Donnees donneesTest = Chargement.charger(imagesTest, etiquettesTest, limit);

        Donnees donneesEntrainementMelangees = melangerDonnees(donneesEntrainement);
        Donnees donneesTestMelangees = melangerDonnees(donneesTest);

        for (int k : valeursK) {
            testerKNN(donneesEntrainement, donneesTest, k, "non_melange");
            testerKNN(donneesEntrainementMelangees, donneesTestMelangees, k, "melange");
        }
    }

    private static void testerKNN(Donnees donneesEntrainement, Donnees donneesTest, int k, String type) {
        System.out.println("Test avec k=" + k + " pour les données " + type);
        AlgoClassification algo = new KNN(donneesEntrainement, k);
        List<Integer> listeEpoques = new ArrayList<>();
        List<Double> listePrecisions = new ArrayList<>();

        long debutTemps = System.currentTimeMillis();
        int nbCorrect = 0;
        for (int i = 0; i < donneesTest.getImagettes().length; i++) {
            Imagette img = donneesTest.getImagette(i);
            if (algo.predire(img) == img.getLabel()) {
                nbCorrect++;
            }
            if (i % 500 == 0) {
                double precision = Math.round((nbCorrect * 1000.0) / (i + 1)) / 10.0;
                listeEpoques.add(i);
                listePrecisions.add(precision);
                System.out.println("Précision à " + i + " images : " + precision + "%");
            }
        }
        long finTemps = System.currentTimeMillis();
        System.out.println("Temps de prédiction pour k=" + k + " (" + type + ") : " + (finTemps - debutTemps) + " ms");
        sauvegarderResultats("donnee_knn_k" + k + "_" + type + ".csv", listeEpoques, listePrecisions);
    }

    private static void sauvegarderResultats(String nomFichier, List<Integer> x, List<Double> y) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomFichier))) {
            writer.println("Epoque,Precision");
            for (int i = 0; i < x.size(); i++) {
                writer.println(x.get(i) + "," + y.get(i));
            }
            System.out.println("Résultats sauvegardés dans " + nomFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    private static Donnees melangerDonnees(Donnees donnees) {
        Imagette[] imagettes = donnees.getImagettes();
        List<Imagette> listeImagettes = new ArrayList<>(Arrays.asList(imagettes));
        Collections.shuffle(listeImagettes);
        Imagette[] imagettesMelangees = listeImagettes.toArray(new Imagette[0]);
        return new Donnees(imagettesMelangees);
    }
}
