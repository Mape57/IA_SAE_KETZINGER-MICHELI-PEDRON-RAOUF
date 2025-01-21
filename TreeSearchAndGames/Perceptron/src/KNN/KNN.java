package KNN;

import Donnees.Donnees;
import Donnees.Imagette;

import java.util.*;

public class KNN extends AlgoClassification {
	private int k;

	public KNN(Donnees donnees, int k) {
		super(donnees);
		this.k = k;
	}

	public int predire(Imagette img) {
		// recuperer les imagettes
		ArrayList<Imagette> data = new ArrayList<>(List.of(this.getDonnees().getImagettes()));
		TreeMap<Integer, Integer> mins = new TreeMap<>();
		for (int i = 0; i < this.k; i++) {
			Imagette imagette = data.get(i);
			int dist = imagette.comparer(img);
			mins.put(dist, imagette.getLabel());
		}
		// calculer les distances et en faire une liste
		// trouver les k voisins les plus proches
		for (int i = this.k; i < data.size(); i++) {
			Imagette imagette = data.get(i);
			int dist = imagette.comparer(img);
			int max = mins.lastKey();
			if (dist < max) {
				mins.remove(max);
				mins.put(dist, imagette.getLabel());
			}
		}
		// recuperer les labels des voisins
		ArrayList<Integer> labels = new ArrayList<>(mins.values());
		// savoir quel est le label le plus frequent
		int res = labels.get(0);
		int frequence = Collections.frequency(labels, res);
		for (Integer label : labels) {
			int nb = Collections.frequency(labels, label);
			if (nb > frequence) {
				res = label;
				frequence = nb;
			}
		}
		return res;
	}
}