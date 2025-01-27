package MLP;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public interface TransferFunction  {
    /**
     * Function de transfert
     * @param value entrée
     * @return sortie de la fonction sur l'entrée
     */
    public double evaluate(double value);

    /**
     * Dérivée de la fonction de tranfert
     * @param value entrée
     * @return sortie de la fonction dérivée sur l'entrée
     */
    public double evaluateDer(double value);
}