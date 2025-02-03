package ia.problemes;

import java.util.Arrays;
import java.util.ArrayList;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.common.Misc;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

/**
 * Représente un état d'un jeu générique m,n,k Game
 * Utilisée pour le morpion, le puissance 4 et gomoku
 */

public abstract class AbstractMnkGameState extends GameState {

    public static final int O = 'O';
	public static final int X = 'X';
	public static final int EMPTY = ' ';

    protected int cols;
    protected int rows;
    protected int streak;

    // le plateau du jeu
    protected char[] board = null;

    // le dernier coup (pour l'affichage)
    protected Pair last_action;

    // la liste des pièces de la configuration gagnante (pour l'affichage)
    protected ArrayList<Pair> winning_move = null;

    // Une classe couple, pour stocker des coordonnée
    public class Pair {
        private int row;
        private int col;

        public Pair(int r, int c){
            this.row = r;
            this.col = c;
        }

        public int getRow(){ return this.row; }
        public int getCol(){ return this.col; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair other = (Pair) o;
            return this.row==other.getRow() && this.col==other.getCol();
        }

        @Override
        public Pair clone() {
            return new Pair(this.row, this.col);
        }
    }

    /**
     * Construire une grille vide de la bonne taille
     *
     * @param r nombre de lignes
     * @param c nombre de colonnes
     */
    public AbstractMnkGameState(int r, int c, int s) {
        this.rows = r;
        this.cols = c;
        this.streak = s;
        this.board = new char[r*c] ;
        for(int i=0; i < r*c; i++)
            this.board[i] = EMPTY;

        this.game_value =  Double.NaN;
        this.player_to_move = X; // convention X commence
        this.winning_move = new ArrayList<Pair>();
    }

    /**
     * Retourne la grille
     *
     * @return la grille du jeux
     */
    public char[] getBoard(){
        return board;
    }

	public boolean equalsState(State o) {
        AbstractMnkGameState other = (AbstractMnkGameState) o;
        return this.cols==other.cols &&
            this.rows==other.rows &&
            this.streak==other.streak &&
            Arrays.equals(this.board, other.getBoard()) &&
            this.winning_move.equals(other.winning_move) &&
            this.last_action.equals(other.last_action) &&
            super.equalsState(o);
	}

    public int hashState() {
        int result = 31 * Arrays.hashCode(this.board);
        result = 31 * result + Integer.hashCode(this.player_to_move);
        result = 31 * result + Double.hashCode(this.game_value);
		return result;
	}

    /*
     * Retourne l'adversaire. Si appelée avec X, on retourne O.
     *
     * @param player un joueur
     * @return l'autre joueur
     */
    public int otherPlayer(int player) {
        return ( player == X ? O : X );
    }

    /**
     *
     *{@inheritDoc}
     * <p>Affiche le plateaux de jeux</p>
     */
    @Override
    public String toString(){

        // première ligne de coordonnées en x
        String res = "   ";
        for (int i=0;i<this.cols; i++){
            if(i<10)
                res += "  "+i+" ";
            else
                res += " "+i+" ";
        }

        res += "\n   ";
        res += Misc.dupString("+---", cols);
        res += "+\n";

        // affiche les lignes
        for(int i=this.rows-1;i>=0; i--){
            if(i<10)
                res += "  "+i;
            else
                res += " "+i;
            for(int j=0; j<this.cols; j++){
                res += "| ";

                if(this.winning_move.contains(new Pair(i,j)))
                    res += Misc.BRIGHT_RED;
                if(this.last_action!=null &&
                   i==last_action.getRow() &&
                   j==last_action.getCol())
                    res += Misc.BRIGHT_GREEN;

                res += getValueAt(i,j) + Misc.RESET + " ";
            }
            res += "|\n   ";
            res += Misc.dupString("+---", cols);
            res += "+\n";
        }

        // coordonnées en x
        res += "   ";
        for (int i=0;i<this.cols; i++){
            if(i<10)
                res += "  "+i+" ";
            else
                res += " "+i+" ";
        }

        return res;
    }

    /**
     * on peut jouer là ?
     *
     * @param idx indice de la case
     * @return vrai si case vide
     */
    public boolean isLegal(int idx) {
        return this.board[idx]==EMPTY;
	}

    /**
     * Le joueur courant (player_to_move) joue à la case idx,
     * et on change de joueur.
     * @param idx l'index de la case
     */
	public void play(int idx) {

        // poser la pièce
        this.board[idx] = (char) player_to_move;

        // enregistrer le coup (pour l'affichage)
        this.last_action = new Pair (idx / this.cols, idx % this.cols);

        // change de joueur, pour le prochain coup
        this.player_to_move = ( this.player_to_move == X ? O : X );

        // Mettre a jour la valeur du jeu
        this.updateGameValue();
	}

    /**
     *
     *{@inheritDoc}
     * <p> Un gagnant ? plus de place ?
     */
    public boolean isFinalState(){
        return lineThroughBoard(X) || lineThroughBoard(O) ||
            getNumberOfMarkedPositions() == this.rows*this.cols;
    }


    // L'API privée

    // retourne la valeur au coordonnées données
    protected char getValueAt(int row, int col) {
		return board[getPositionAt(row, col)];
	}

    // retourne l'index de la case au coordonnées données
	protected int getPositionAt(int row, int col) {
		return row * this.cols + col;
	}

    // le nombre de case déjà remplies
    private int getNumberOfMarkedPositions() {
        int count = 0;
        for(int i=0; i<this.rows*this.cols; i++)
            if (board[i] != EMPTY)
					count++;
        return count;
    }

    /**
     * Un fonction d'évaluation pour cet état du jeu.
     * Permet de comparer différents états dans le cas ou on ne
     * peut pas développer tout l'arbre. Le joueur 1 (X) choisira les
     * actions qui mènent au état de valeur maximal, Le joueur 2 (O)
     * choisira les valeurs minimal.
     *
     * Cette fonction dépend du jeu.
     *
     * @return la valeur du jeux
     **/
    protected abstract double evaluationFunction();

    /**
     * {@inheritDoc}
     *
     * <p>Pendant le jeu, la valeur est donnée par une fonction d'évaluation
     *   sinon fixe le gagnant ou match nul</p>
     */
    protected void updateGameValue() {
        if (player_to_move == 21) {
            game_value = evaluationFunction_withDanger();
        } else {
		    if( lineThroughBoard(X) )  // un gagnant, lequel ?
		    	game_value = P1_WIN;
            else if( lineThroughBoard(O) )
                game_value =  P2_WIN;
		    else if (getNumberOfMarkedPositions() == this.rows*this.cols) // nul
		    	game_value = DRAW;
            else // utiliser une fonction dévaluation
                game_value = this.evaluationFunction();
        }
	}

    /**
     * vérifier s'il y a pas une ligne de faite (fin du jeux)
     *
     * @return vrai si partie fini
     */
	protected boolean lineThroughBoard(int player) {
        this.winning_move.clear();
		return (isAnyRowComplete(player) ||
                isAnyColumnComplete(player) ||
                isAnyDiagonalComplete(player));
	}

	//verifier s'il y a pas une ligne horizontale de faite
	private boolean isAnyRowComplete(int player) {
        for(int r=0; r<this.rows;r++)
            for(int c=0; c<=this.cols-this.streak; c++){
                boolean res = true;
                this.winning_move.clear();
                for(int k=0; k<this.streak; k++){
                    res &= this.getValueAt(r,c+k) == player;
                    this.winning_move.add(new Pair(r,c+k));
                }
                if(res)
                    return true;

            }
        this.winning_move.clear();
        return false;
	}

    // verifier s'il y a pas une ligne vertical de faite
	private boolean isAnyColumnComplete(int player) {
        for(int c=0; c<this.cols; c++)
            for(int r=0; r<=this.rows-this.streak; r++){
                boolean res = true;
                this.winning_move.clear();
                for(int k=0; k<this.streak; k++){
                    res &= this.getValueAt(r+k,c) == player;
                    this.winning_move.add(new Pair(r+k,c));
                }
                if(res)
                    return true;
            }
        this.winning_move.clear();
		return false;
	}

    // verifier s'il y a pas une ligne diagonale de faite
	private boolean isAnyDiagonalComplete(int player) {
        // 45 deg
        for(int c=0; c<=this.cols-this.streak; c++)
            for(int r=0; r<=this.rows-this.streak; r++){
                boolean res = true;
                this.winning_move.clear();
                for(int k=0; k<this.streak; k++){
                    res &= this.getValueAt(r+k,c+k) == player;
                        this.winning_move.add(new Pair(r+k,c+k));
                }
                if(res)
                    return true;
            }

        // -45 deg
        for(int c=0; c<=this.cols-this.streak; c++)
            for(int r=this.streak-1; r<this.rows; r++){
                boolean res = true;
                this.winning_move.clear();
                for(int k=0; k<this.streak; k++){
                    res &= this.getValueAt(r-k,c+k) == player;
                    this.winning_move.add(new Pair(r-k,c+k));
                }
                if(res)
                    return true;
            }
        this.winning_move.clear();
        return false;
	}



    private double evaluationFunction_withDanger() {

        AbstractMnkGameState mnkState = this;
        char[] board = mnkState.getBoard();

        double[] dangerBoard = new double[board.length];
        int streak = this.streak;
        int cols = this.cols;
        int rows = this.rows;
        int piece_count = 0;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                boolean x_in_bounds = x + streak <= cols;
                boolean y_in_bounds = y + streak <= rows;
                boolean neg_x_in_bounds = x - (streak - 1) >= 0;

                // verification horizontale
                if (x_in_bounds) {
                    int h_freeSpaces = 0;
                    int X_count = 0;
                    int O_count = 0;
                    for (int j = 0; j < streak; j++) {
                        char h_value = getVal(board, y, x + j, cols);
                        if (h_value == AbstractMnkGameState.EMPTY) {
                            h_freeSpaces++;
                        } else if (h_value == ((mnkState.getPlayerToMove() == Player.PLAYER2) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
                            h_freeSpaces -= (2 * streak);
                        }
                        X_count += (h_value == AbstractMnkGameState.X) ? 1 : 0;
                        O_count += (h_value == AbstractMnkGameState.O) ? 1 : 0;
                    }

                    for (int j = 0; j < streak; j++) {
                        if (h_freeSpaces <= 0) continue;
                        dangerBoard[y * cols + x + j] += (1 / Math.pow(2, h_freeSpaces));
                    }

                    if (X_count == streak) {
                        return GameState.P1_WIN;
                    } else if (O_count == streak) {
                        return GameState.P2_WIN;
                    }

                    piece_count += X_count + O_count;
                }

                // verification verticale
                if (y_in_bounds) {
                    int v_freeSpaces = 0;
                    int X_count = 0;
                    int O_count = 0;
                    for (int j = 0; j < streak; j++) {
                        char v_value = getVal(board, y + j, x, cols);
                        if (v_value == AbstractMnkGameState.EMPTY) {
                            v_freeSpaces++;
                        } else if (v_value == ((mnkState.getPlayerToMove() == Player.PLAYER2) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
                            v_freeSpaces -= (2 * streak);
                        }
                        X_count += (v_value == AbstractMnkGameState.X) ? 1 : 0;
                        O_count += (v_value == AbstractMnkGameState.O) ? 1 : 0;
                    }

                    for (int j = 0; j < streak; j++) {
                        if (v_freeSpaces <= 0) continue;
                        dangerBoard[(y + j) * cols + x] += (1 / Math.pow(2, v_freeSpaces));
                    }

                    if (X_count == streak) {
                        return GameState.P1_WIN;
                    } else if (O_count == streak) {
                        return GameState.P2_WIN;
                    }

                    piece_count += X_count + O_count;
                }

                // verification diagonale (bas droite)
                if (x_in_bounds && y_in_bounds) {
                    int d_freeSpaces = 0;
                    int X_count = 0;
                    int O_count = 0;
                    for (int j = 0; j < streak; j++) {
                        char d_value = getVal(board, y + j, x + j, cols);
                        if (d_value == AbstractMnkGameState.EMPTY) {
                            d_freeSpaces++;
                        } else if (d_value == ((mnkState.getPlayerToMove() == Player.PLAYER2) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
                            d_freeSpaces -= (2 * streak);
                        }
                        X_count += (d_value == AbstractMnkGameState.X) ? 1 : 0;
                        O_count += (d_value == AbstractMnkGameState.O) ? 1 : 0;
                    }

                    for (int j = 0; j < streak; j++) {
                        if (d_freeSpaces <= 0) continue;
                        dangerBoard[(y + j) * cols + x + j] += (1 / Math.pow(2, d_freeSpaces));
                    }

                    if (X_count == streak) {
                        return GameState.P1_WIN;
                    } else if (O_count == streak) {
                        return GameState.P2_WIN;
                    }

                    piece_count += X_count + O_count;
                }

                // verification diagonale (bas gauche)
                if (neg_x_in_bounds && y_in_bounds) {
                    int d2_freeSpaces = 0;
                    int X_count = 0;
                    int O_count = 0;
                    for (int j = 0; j < streak; j++) {
                        char d2_value = getVal(board, y + j, x - j, cols);
                        if (d2_value == AbstractMnkGameState.EMPTY) {
                            d2_freeSpaces++;
                        } else if (d2_value == ((mnkState.getPlayerToMove() == Player.PLAYER2) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
                            d2_freeSpaces -= (2 * streak);
                        }
                        X_count += (d2_value == AbstractMnkGameState.X) ? 1 : 0;
                        O_count += (d2_value == AbstractMnkGameState.O) ? 1 : 0;
                    }

                    for (int j = 0; j < streak; j++) {
                        if (d2_freeSpaces <= 0) continue;
                        dangerBoard[(y + j) * cols + x - j] += (1 / Math.pow(2, d2_freeSpaces));
                    }

                    if (X_count == streak) {
                        return GameState.P1_WIN;
                    } else if (O_count == streak) {
                        return GameState.P2_WIN;
                    }

                    piece_count += X_count + O_count;
                }
            }
        }

        if (piece_count == board.length) {
            return GameState.DRAW;
        }

        // faire la somme et retirer les dangers des cases déjà jouées
        double totalDanger = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == AbstractMnkGameState.EMPTY) {
                totalDanger += dangerBoard[i];
            }
        }

        return totalDanger;
    }

    private char getVal(char[] board, int row, int col, int cols) {
        return board[row * cols + col];
    }
}
