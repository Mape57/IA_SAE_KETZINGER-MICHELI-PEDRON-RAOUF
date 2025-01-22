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
		Set<Map.Entry<Imagette, Integer>> distances = new TreeSet<>(Comparator.comparingInt(Map.Entry::getValue));
		for (Imagette img2 : this.getDonnees().getImagettes()) {
			distances.add(new AbstractMap.SimpleEntry<>(img2, img.comparer(img2)));
		}

		List<Integer> labels = new ArrayList<>();
		for (Map.Entry<Imagette, Integer> entry : distances) {
			if (labels.size() == k) {
				break;
			}
			labels.add(entry.getKey().getLabel());
		}

		Map<Integer, Integer> count = new HashMap<>();
		Integer bestLabel = null;
		int bestCount = 0;
		for (int label : labels) {
			int newCount = count.getOrDefault(label, 0) + 1;
			count.put(label, newCount);
			if (newCount > bestCount) {
				bestLabel = label;
				bestCount = newCount;
			}
		}

		return bestLabel;
	}
}