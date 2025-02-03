package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;
import ia.problemes.AbstractMnkGame;
import ia.problemes.AbstractMnkGameState;

import java.util.Arrays;

public class DangerPlayer extends Player {
	/**
	 * Represente un joueur
	 *
	 * @param g          l'instance du jeux
	 * @param player_one si joueur 1
	 */
	public DangerPlayer(Game g, boolean player_one) {
		super(g, player_one);
	}

	@Override
	public Action getMove(GameState state) {
		if (!(state instanceof AbstractMnkGameState))
			throw new IllegalArgumentException("The state must be an instance of AbstractMnkGameState");
		if (!(game instanceof AbstractMnkGame))
			throw new IllegalArgumentException("The game must be an instance of AbstractMnkGame");

		AbstractMnkGame mnkGame = (AbstractMnkGame) game;
		AbstractMnkGameState mnkState = (AbstractMnkGameState) state;
		char[] board = mnkState.getBoard();

		double[] dangerBoard = new double[board.length];
		int streak = mnkGame.getStreak();
		int cols = mnkGame.getCols();
		int rows = mnkGame.getRows();

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				boolean x_in_bounds = x + streak <= cols;
				boolean y_in_bounds = y + streak <= rows;
				boolean neg_x_in_bounds = x - (streak - 1) >= 0;

				// verification horizontale
				if (x_in_bounds) {
					int h_freeSpaces = 0;
					for (int j = 0; j < streak; j++) {
						char h_value = getValueAt(board, y, x + j, cols);
						if (h_value == AbstractMnkGameState.EMPTY) {
							h_freeSpaces++;
						} else if (h_value == ((this.player == PLAYER1) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
							h_freeSpaces -= (2 * streak);
						}
					}

					for (int j = 0; j < streak; j++) {
						if (h_freeSpaces <= 0) continue;
						dangerBoard[y * cols + x + j] += (1 / Math.pow(2, h_freeSpaces));
					}
				}

				// verification verticale
				if (y_in_bounds) {
					int v_freeSpaces = 0;
					for (int j = 0; j < streak; j++) {
						char v_value = getValueAt(board, y + j, x, cols);
						if (v_value == AbstractMnkGameState.EMPTY) {
							v_freeSpaces++;
						} else if (v_value == ((this.player == PLAYER1) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
							v_freeSpaces -= (2 * streak);
						}
					}

					for (int j = 0; j < streak; j++) {
						if (v_freeSpaces <= 0) continue;
						dangerBoard[(y + j) * cols + x] += (1 / Math.pow(2, v_freeSpaces));
					}
				}

				// verification diagonale (bas droite)
				if (x_in_bounds && y_in_bounds) {
					int d_freeSpaces = 0;
					for (int j = 0; j < streak; j++) {
						char d_value = getValueAt(board, y + j, x + j, cols);
						if (d_value == AbstractMnkGameState.EMPTY) {
							d_freeSpaces++;
						} else if (d_value == ((this.player == PLAYER1) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
							d_freeSpaces -= (2 * streak);
						}
					}

					for (int j = 0; j < streak; j++) {
						if (d_freeSpaces <= 0) continue;
						dangerBoard[(y + j) * cols + x + j] += (1 / Math.pow(2, d_freeSpaces));
					}
				}

				// verification diagonale (bas gauche)
				if (neg_x_in_bounds && y_in_bounds) {
					int d2_freeSpaces = 0;
					for (int j = 0; j < streak; j++) {
						char d2_value = getValueAt(board, y + j, x - j, cols);
						if (d2_value == AbstractMnkGameState.EMPTY) {
							d2_freeSpaces++;
						} else if (d2_value == ((this.player == PLAYER1) ? AbstractMnkGameState.X : AbstractMnkGameState.O)) {
							d2_freeSpaces -= (2 * streak);
						}
					}

					for (int j = 0; j < streak; j++) {
						if (d2_freeSpaces <= 0) continue;
						dangerBoard[(y + j) * cols + x - j] += (1 / Math.pow(2, d2_freeSpaces));
					}
				}
			}
		}


		// Trouver le danger le plus grand en retirant les dangers des cases déjà jouées
		double maxDanger = -1;
		int maxDangerIndex = 0;
		for (int i = 0; i < dangerBoard.length; i++) {
			if (board[i] != AbstractMnkGameState.EMPTY) continue;
			if (dangerBoard[i] > maxDanger) {
				maxDanger = dangerBoard[i];
				maxDangerIndex = i;
			}
		}

		// print the danger board
		System.out.println("Danger board for player " + (this.player == PLAYER1 ? "1" : "2"));
		for (int i = 0; i < rows; i++) {
			System.out.println(Arrays.toString(Arrays.copyOfRange(dangerBoard, i * cols, (i + 1) * cols)));
		}

		// Trouver la position correspondante
		int x = maxDangerIndex % cols;
		int y = maxDangerIndex / cols;
		int actionIndex = y * cols + x;
		return new Action(String.valueOf(actionIndex));
	}

	// retourne la valeur au coordonnées données
	private char getValueAt(char[] board, int row, int col, int cols) {
		return board[row * cols + col];
	}
}
