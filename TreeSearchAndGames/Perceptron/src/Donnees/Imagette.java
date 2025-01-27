package Donnees;

public class Imagette {
    private int[][] pixels;
    private int label;

    public Imagette(int[][] pixels, int label) {
        this.pixels = pixels;
        this.label = label;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public int getLabel() {
        return label;
    }

    public int comparer(Imagette img){
        int total = 0;
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                total += Math.abs(this.pixels[i][j] - img.pixels[i][j]);
            }
        }
        return total;
    }

}
