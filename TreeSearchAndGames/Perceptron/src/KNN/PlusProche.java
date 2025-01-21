package KNN;

import Donnees.Donnees;
import Donnees.Imagette;

import java.io.IOException;

public class PlusProche extends AlgoClassification{
    public PlusProche(Donnees donnees) throws IOException {
		super(donnees);
	}

    @Override
    public int predire(Imagette img) {
        int res = this.getDonnees().getImagette(0).getLabel();
        int dist = this.getDonnees().getImagette(0).comparer(img);
        for (Imagette imagette : this.getDonnees().getImagettes()) {
            int tmp = imagette.comparer(img);
            if (tmp < dist) {
                dist = tmp;
                res = imagette.getLabel();
            }
        }
        return res;
    }
}
