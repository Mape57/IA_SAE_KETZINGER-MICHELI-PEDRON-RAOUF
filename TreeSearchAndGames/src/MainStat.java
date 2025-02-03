import ia.algo.jeux.AlphaBetaPlayer;
import ia.algo.jeux.MinMaxPlayer;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameEngine;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;
import ia.problemes.MnkGame;

import java.io.FileWriter;
import java.io.IOException;

public class MainStat {
	private static final int[] Ns = {3, 4, 5, 6, 7, 8, 9};
	private static final int[] Ps = {2, 3, 4};
	private static final int NB_GAMES = 10;

	public static void main(String[] args) throws IOException {

		String filename = "results_alpbet.csv";
		FileWriter writer = new FileWriter(filename);
		writer.write("taille;profondeur;temps;p1_win_rate;p2_win_rate;draw_rate;p1_profondeur;p2_profondeur\n");
		for (int n_value : Ns) {
			for (int p_value : Ps) {
				float total_time = 0;
				float total_p1_states = 0;
				float total_p2_states = 0;
				float total_p1_win = 0;
				float total_p2_win = 0;
				float total_draw = 0;

				for (int i = 0; i < NB_GAMES; i++) {
					Game game = new MnkGame(n_value, n_value, 4);
					Player p1 = new MinMaxPlayer(game, true, p_value);
					Player p2 = new MinMaxPlayer(game, false, p_value);
					GameEngine game_engine = new GameEngine(game, p1, p2);

					long startTime = System.currentTimeMillis();
					GameState end_game = game_engine.gameLoop();
					long estimatedTime = System.currentTimeMillis() - startTime;

					total_time += estimatedTime;
					total_p1_states += p1.getStateCounter();
					total_p2_states += p2.getStateCounter();

					double end_game_value = game_engine.getEndGameValue(end_game);
					if (end_game_value == GameState.P1_WIN) {
						total_p1_win++;
					} else if (end_game_value == GameState.P2_WIN) {
						total_p2_win++;
					} else {
						total_draw++;
					}
					System.out.println("Game " + i + " finished");
				}
				writer.write(n_value + ";" + p_value + ";" + total_time / NB_GAMES + ";" + total_p1_win / NB_GAMES + ";" + total_p2_win / NB_GAMES + ";" + total_draw / NB_GAMES + ";" + total_p1_states / NB_GAMES + ";" + total_p2_states / NB_GAMES + "\n");
				writer.flush();
			}
		}
		writer.close();
	}
}
