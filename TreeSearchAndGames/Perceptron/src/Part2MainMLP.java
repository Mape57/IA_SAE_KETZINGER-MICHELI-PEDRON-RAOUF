import MLP.MLP;
import Donnees.Chargement;
import Donnees.Donnees;
import Donnees.Imagette;
import MLP.Sigmoide;
import MLP.TangenteHyperbolique;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.io.FileWriter;

public class Part2MainMLP {

	// Fichiers MNIST
	//public static final String IMAGES_TRAIN = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-images.idx3-ubyte";
	//public static final String ETIQUETTES_TRAIN = "TreeSearchAndGAmes/Perceptron/Data/Nombre/train-labels.idx1-ubyte";
	//public static final String IMAGES_TEST = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-images.idx3-ubyte";
	//public static final String ETIQUETTES_TEST = "TreeSearchAndGAmes/Perceptron/Data/Nombre/t10k-labels.idx1-ubyte";

	//Fichiers Fashion MNIST
	public static final String IMAGES_TRAIN = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-images-idx3-ubyte";
	public static final String ETIQUETTES_TRAIN = "TreeSearchAndGAmes/Perceptron/Data/Fashion/train-labels-idx1-ubyte";
	public static final String IMAGES_TEST = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-images-idx3-ubyte";
	public static final String ETIQUETTES_TEST = "TreeSearchAndGAmes/Perceptron/Data/Fashion/t10k-labels-idx1-ubyte";

	public static void main(String[] args) {
		int limit = 1000;
		try {
			// Charger les données
			System.out.println("Chargement des données...");
			Donnees donneesTrain = Chargement.charger(IMAGES_TRAIN, ETIQUETTES_TRAIN, limit);
			Donnees donneesTest = Chargement.charger(IMAGES_TEST, ETIQUETTES_TEST, limit);
			System.out.println("Données chargées avec succès.\n");

			//Mélanger les données
			//System.out.println("Mélange des données d'entraînement...");
			//donneesTrain = melangerDonnees(donneesTrain);
			//System.out.println("Données mélangées.\n");

			// Tests sur différents paramètres
			testerNombreDeNeurones(donneesTrain, donneesTest);
			testerNombreDeCouches(donneesTrain, donneesTest);
			testerTauxApprentissage(donneesTrain, donneesTest);
			testerEvolutionTauxApprentissage(donneesTrain, donneesTest);

		} catch (IOException e) {
			System.err.println("Erreur lors du chargement des données : " + e.getMessage());
		}
	}

	/**
	 * Teste l'influence du nombre de neurones cachés.
	 */
	private static void testerNombreDeNeurones(Donnees donneesTrain, Donnees donneesTest) {
		System.out.println("Début des tests sur le nombre de neurones...");
		int[] nombreNeuronesOptions = {9, 50, 100, 250, 500};
		List<String[]> resultats = new ArrayList<>();
		resultats.add(new String[]{"Nombre de Neurones", "Précision", "Temps (ms)"});

		for (int nombreNeurones : nombreNeuronesOptions) {
			int[] couches = {784, nombreNeurones, 10};
			MLP perceptron = new MLP(couches, 0.1, new Sigmoide());

			long startTime = System.currentTimeMillis();
			double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
			long endTime = System.currentTimeMillis();

			resultats.add(new String[]{String.valueOf(nombreNeurones), String.valueOf(precision), String.valueOf(endTime - startTime)});
			System.out.println("Nombre de neurones cachés : " + nombreNeurones + " - Précision : " + precision + "% - Temps : " + (endTime - startTime) + " ms");
		}
		ecrireResultatsCSV("resultats_nombre_neurones.csv", resultats);
		System.out.println("Tests sur le nombre de neurones terminés.\n");
	}

	/**
	 * Teste l'influence du nombre de couches cachées.
	 */
	private static void testerNombreDeCouches(Donnees donneesTrain, Donnees donneesTest) {
		System.out.println("Début des tests sur le nombre de couches...");
		int[][] configurationsCouches = {
				{784, 10}, {784, 100, 100, 10}, {784, 250, 250, 10}, {784, 100, 100, 100, 10},
				{784, 250, 250, 250, 10}, {784, 100, 100, 100, 100, 10}, {784, 250, 250, 250, 250, 10},
				{784, 100, 100, 100, 100, 100, 10}, {784, 250, 250, 250, 250, 250, 10},
		};
		List<String[]> resultats = new ArrayList<>();
		resultats.add(new String[]{"Configuration des Couches", "Précision", "Temps (ms)"});

		for (int[] couches : configurationsCouches) {
			MLP perceptron = new MLP(couches, 0.1, new Sigmoide());

			long startTime = System.currentTimeMillis();
			double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
			long endTime = System.currentTimeMillis();

			resultats.add(new String[]{Arrays.toString(couches), String.valueOf(precision), String.valueOf(endTime - startTime)});
			System.out.println("Configuration des couches : " + Arrays.toString(couches) + " - Précision : " + precision + "% - Temps : " + (endTime - startTime) + " ms");
		}
		ecrireResultatsCSV("resultats_nombre_couches.csv", resultats);
		System.out.println("Tests sur le nombre de couches terminés.\n");
	}

	/**
	 * Teste l'influence du taux d'apprentissage.
	 */
	private static void testerTauxApprentissage(Donnees donneesTrain, Donnees donneesTest) {
		System.out.println("Début des tests sur le taux d'apprentissage...");
		double[] tauxApprentissageOptions = {0.01, 0.1, 0.5, 1.0};
		List<String[]> resultats = new ArrayList<>();
		resultats.add(new String[]{"Taux d'Apprentissage", "Précision", "Temps (ms)"});

		for (double tauxApprentissage : tauxApprentissageOptions) {
			int[] couches = {784, 100, 10};
			MLP perceptron = new MLP(couches, tauxApprentissage, new Sigmoide());

			long startTime = System.currentTimeMillis();
			double precision = entrainerEtEvaluer(perceptron, donneesTrain, donneesTest);
			long endTime = System.currentTimeMillis();

			resultats.add(new String[]{String.valueOf(tauxApprentissage), String.valueOf(precision), String.valueOf(endTime - startTime)});
			System.out.println("Taux d'apprentissage : " + tauxApprentissage + " - Précision : " + precision + "% - Temps : " + (endTime - startTime) + " ms");
		}
		ecrireResultatsCSV("resultats_taux_apprentissage.csv", resultats);
		System.out.println("Tests sur le taux d'apprentissage terminés.\n");
	}

	private static void testerEvolutionTauxApprentissage(Donnees donneesTrain, Donnees donneesTest) {
			System.out.println("Début de l'entraînement avec augmentation du taux d'apprentissage...");

			double tauxInitial = 0.1;
			double tauxMax = 1.0;
			double augmentation = 0.05;
			int epochs = 20;           // Nombre total d'époques
			int[] couches = {784, 100, 10};  // Configuration du réseau

			MLP perceptron = new MLP(couches, tauxInitial, new Sigmoide());
			List<String[]> resultats = new ArrayList<>();
			resultats.add(new String[]{"Époque", "Taux d'Apprentissage", "Précision (%)"});

			double tauxActuel = tauxInitial;

			for (int epoch = 0; epoch < epochs; epoch++) {
				// Augmenter le taux d’apprentissage, sans dépasser le maximum
				if (tauxActuel < tauxMax) {
					tauxActuel += augmentation;
				}
				perceptron.setLearningRate(tauxActuel); // Mise à jour du taux

				// Entraînement pour l'époque actuelle
				for (Imagette imagette : donneesTrain.getImagettes()) {
					double[] entrees = aplatirEtNormaliser(imagette.getPixels());
					double[] sortiesDesirees = new double[10];
					sortiesDesirees[imagette.getLabel()] = 1;
					perceptron.backPropagate(entrees, sortiesDesirees);
				}

				// Évaluation de la précision
				long predictionsCorrectes = Arrays.stream(donneesTest.getImagettes())
						.filter(imagette -> {
							double[] entrees = aplatirEtNormaliser(imagette.getPixels());
							double[] sortiesPredites = perceptron.execute(entrees);
							int etiquettePredite = obtenirEtiquetteDepuisSortie(sortiesPredites);
							return etiquettePredite == imagette.getLabel();
						})
						.count();

				double precision = (double) predictionsCorrectes / donneesTest.getImagettes().length * 100;

				resultats.add(new String[]{String.valueOf(epoch), String.valueOf(tauxActuel), String.valueOf(precision)});
				System.out.println("Époque " + epoch + " - Taux d'apprentissage : " + tauxActuel + " - Précision : " + precision + "%");
			}

			ecrireResultatsCSV("resultats_evolution_taux_apprentissage.csv", resultats);
			System.out.println("Entraînement terminé. Résultats enregistrés.");
		}


	private static void ecrireResultatsCSV(String nomFichier, List<String[]> donnees) {
		try (FileWriter writer = new FileWriter(nomFichier)) {
			for (String[] ligne : donnees) {
				writer.append(String.join(",", ligne));
				writer.append("\n");
			}
			System.out.println("Résultats sauvegardés dans " + nomFichier);
		} catch (IOException e) {
			System.err.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
		}
	}

	private static Donnees melangerDonnees(Donnees donnees) {
		Imagette[] imagettes = donnees.getImagettes();
		List<Imagette> listeImagettes = new ArrayList<>(Arrays.asList(imagettes));

		// Mélanger les données
		Collections.shuffle(listeImagettes);

		// Reconvertir en tableau
		Imagette[] imagettesMelangees = listeImagettes.toArray(new Imagette[0]);

		// Retourner les données mélangées
		return new Donnees(imagettesMelangees);
	}

	/**
	 * Entraîne et évalue un réseau.
	 */
	private static double entrainerEtEvaluer(MLP perceptron, Donnees donneesTrain, Donnees donneesTest) {
		// Entraînement
		Arrays.stream(donneesTrain.getImagettes()).forEach(imagette -> {
			double[] entrees = aplatirEtNormaliser(imagette.getPixels());
			double[] sortiesDesirees = new double[10];
			sortiesDesirees[imagette.getLabel()] = 1;
			perceptron.backPropagate(entrees, sortiesDesirees);
		});
		// Évaluation
		long predictionsCorrectes = Arrays.stream(donneesTest.getImagettes())
				.filter(imagette -> {
					double[] entrees = aplatirEtNormaliser(imagette.getPixels());
					double[] sortiesPredites = perceptron.execute(entrees);
					int etiquettePredite = obtenirEtiquetteDepuisSortie(sortiesPredites);
					return etiquettePredite == imagette.getLabel();
				})
				.count();

		return (double) predictionsCorrectes / donneesTest.getImagettes().length * 100;
	}

	/**
	 * Aplatie et normalise une image pour l'entrée du réseau.
	 */
	private static double[] aplatirEtNormaliser(int[][] pixels) {
		return Arrays.stream(pixels)
				.flatMapToInt(Arrays::stream)
				.mapToDouble(val -> val / 255.0)
				.toArray();
	}

	/**
	 * Renvoie l'étiquette prédite à partir des sorties du réseau.
	 */
	private static int obtenirEtiquetteDepuisSortie(double[] sorties) {
		return IntStream.range(0, sorties.length)
				.reduce((indiceMax, i) -> sorties[i] > sorties[indiceMax] ? i : indiceMax)
				.orElse(0);
	}
}
