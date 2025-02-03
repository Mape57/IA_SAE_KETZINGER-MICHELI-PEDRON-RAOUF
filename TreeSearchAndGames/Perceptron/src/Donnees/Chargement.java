package Donnees;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Chargement {
	public static Donnees charger(String pathImage, String pathLabel, int limit) throws IOException {
		// Création du stream image
		DataInputStream imageStream = new DataInputStream(new FileInputStream(pathImage));
		DataInputStream labelStream = new DataInputStream(new FileInputStream(pathLabel));

		try {
			// Vérification du type de fichier image
			int fileType = imageStream.readInt();
			if (fileType != 2051) {
				throw new RuntimeException("Invalid image file type");
			}

			// Vérification du type de fichier label
			int fileType2 = labelStream.readInt();
			if (fileType2 != 2049) {
				throw new RuntimeException("Invalid label file type");
			}

			// Nombre total d'images et labels
			int nbImagettes = imageStream.readInt();
			int nbLabels = labelStream.readInt();

			if (nbLabels != nbImagettes) {
				throw new RuntimeException("Number of labels and images do not match");
			}

			// Taille de chaque imagette
			int nbLignes = imageStream.readInt();
			int nbColonnes = imageStream.readInt();

			// Limite du nombre d'images à charger
			int nbImagesACharger = Math.min(limit, nbImagettes);

			// Création des imagettes
			Imagette[] imagettes = new Imagette[nbImagesACharger];
			for (int i = 0; i < nbImagesACharger; i++) {
				int[][] pixels = new int[nbLignes][nbColonnes];
				for (int ligne = 0; ligne < nbLignes; ligne++) {
					for (int colonne = 0; colonne < nbColonnes; colonne++) {
						pixels[ligne][colonne] = imageStream.readUnsignedByte();
					}
				}
				imagettes[i] = new Imagette(pixels, labelStream.readUnsignedByte());
			}

			return new Donnees(imagettes);

		} finally {
			// Fermeture des streams pour éviter les fuites de mémoire
			imageStream.close();
			labelStream.close();
		}
	}
}
