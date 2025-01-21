package Donnees;

public class Donnees {
    private Imagette[] imagettes;

    public Donnees(Imagette[] imagettes) {
        this.imagettes = imagettes;
    }

    public Imagette[] getImagettes() {
        return imagettes;
    }

    public Imagette getImagette(int index) {
        return imagettes[index];
    }
}
