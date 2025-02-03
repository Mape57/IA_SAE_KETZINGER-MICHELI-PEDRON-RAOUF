package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.util.Collections;
import java.util.List;

public class MinMaxPlayer extends Player {
	private int maxDepth;

	/**
	 * Represente un joueur
	 *
	 * @param g          l'instance du jeux
	 * @param player_one si joueur 1
	 */
	public MinMaxPlayer(Game g, boolean player_one, int maxDepth) {
		super(g, player_one);
		this.maxDepth = maxDepth;
	}

	@Override
	public Action getMove(GameState state) {
		if (this.player == PLAYER1) {
			return maxValue(state, 0).getAction();
		} else {
			return minValue(state, 0).getAction();
		}
	}

	private ActionValuePair maxValue(GameState state, int currentDepth) {
		if (state.isFinalState() || currentDepth == maxDepth) {
			return new ActionValuePair(null, state.getGameValue());
		}

		double V_max = -Double.MAX_VALUE;
		Action C_max = null;

		List<Action> actions = game.getActions(state);
		Collections.shuffle(actions);
		ActionValuePair result = null;

		for (Action c : actions) {
			this.incStateCounter();
			GameState S_suivant = (GameState) game.doAction(state, c);
			result = minValue(S_suivant, currentDepth + 1);
			if (result.getValue() >= V_max) {
				V_max = result.getValue();
				C_max = c;
			}
		}

		if (C_max == null) {
			V_max = result.getValue();
			C_max = actions.getLast();
		}

		return new ActionValuePair(C_max, V_max);
	}

	private ActionValuePair minValue(GameState state, int currentDepth) {
		if (state.isFinalState() || currentDepth == maxDepth) {
			return new ActionValuePair(null, state.getGameValue());
		}

		double V_min = Double.MAX_VALUE;
		Action C_min = null;

		List<Action> actions = game.getActions(state);
		Collections.shuffle(actions);
		ActionValuePair result = null;

		for (Action c : actions) {
			this.incStateCounter();
			GameState S_suivant = (GameState) game.doAction(state, c);
			result = maxValue(S_suivant, currentDepth + 1);

			if (result.getValue() <= V_min) {
				V_min = result.getValue();
				C_min = c;
			}
		}

		if (C_min == null) {
			V_min = result.getValue();
			C_min = actions.getLast();
		}

		return new ActionValuePair(C_min, V_min);
	}
}
