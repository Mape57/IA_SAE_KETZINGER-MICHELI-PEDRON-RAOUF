package Donnees;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Chargement {
	public static Donnees charger(String pathImage, String pathLabel) throws IOException {
		// creation du stream image
		DataInputStream imageStream = new DataInputStream(new FileInputStream(pathImage));
		int fileType = imageStream.readInt();
		if (fileType != 2051) {
			throw new RuntimeException("Invalid file type");
		}

		// creation du stream label
		DataInputStream labelStream = new DataInputStream(new FileInputStream(pathLabel));
		int fileType2 = labelStream.readInt();
		if (fileType2 != 2049) {
			throw new RuntimeException("Invalid file type");
		}

		// enregistrement du nombre d'element
		int nbImagettes = imageStream.readInt();
		int nbLabels = labelStream.readInt();

		if (nbLabels != nbImagettes) {
			throw new RuntimeException("Number of labels and images do not match");
		}

		// enregistrement de la taille de chaque imagette
		int nbLignes = imageStream.readInt();
		int nbColonnes = imageStream.readInt();

		// parcours des fichiers & creation des imagettes
		Imagette[] imagettes = new Imagette[nbImagettes];
		for (int i = 0; i < nbImagettes; i++) {
			int[][] pixels = new int[nbLignes][nbColonnes];
			for (int ligne = 0; ligne < nbLignes; ligne++) {
				for (int colonne = 0; colonne < nbColonnes; colonne++) {
					pixels[ligne][colonne] = imageStream.readUnsignedByte();
				}
			}
			imagettes[i] = new Imagette(pixels, labelStream.readUnsignedByte());
		}
		return new Donnees(imagettes);
	}
}
