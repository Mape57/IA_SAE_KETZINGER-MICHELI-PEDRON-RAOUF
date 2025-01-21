package KNN;

import Donnees.Donnees;
import Donnees.Imagette;


public abstract class AlgoClassification {
    private Donnees donnees;

    public AlgoClassification(Donnees donnees) {
        this.donnees = donnees;
    }

    public abstract int predire(Imagette testImagette);

    public Donnees getDonnees() {
        return donnees;
    }
}
