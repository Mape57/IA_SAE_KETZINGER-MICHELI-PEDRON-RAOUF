package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class AlphaBetaPlayer extends Player {
	private int maxDepth;

	/**
	 * Represente un joueur
	 *
	 * @param g          l'instance du jeux
	 * @param player_one si joueur 1
	 */
	public AlphaBetaPlayer(Game g, boolean player_one, int maxDepth) {
		super(g, player_one);
		this.maxDepth = maxDepth;
	}

	@Override
	public Action getMove(GameState state) {
		if (this.player == PLAYER1) {
			return maxValue(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0).getAction();
		} else {
			return minValue(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0).getAction();
		}
	}

	private ActionValuePair maxValue(GameState state, double alpha, double beta, int currentDepth) {
		if (state.isFinalState() || currentDepth == maxDepth) {
			return new ActionValuePair(null, state.getGameValue());
		}

		ActionValuePair max = new ActionValuePair(null, Double.NEGATIVE_INFINITY);
		for (Action coup : game.getActions(state)) {
			this.incStateCounter();
			GameState s_suivant = (GameState) game.doAction(state, coup);
			ActionValuePair result = minValue(s_suivant, alpha, beta, currentDepth + 1);
			if (result.getValue() >= max.getValue()) {
				max = new ActionValuePair(coup, result.getValue());
				if (max.getValue() > alpha) {
					alpha = max.getValue();
				}
			}
			if (max.getValue() >= beta) {
				return max;
			}
		}
		return max;
	}

	private ActionValuePair minValue(GameState state, double alpha, double beta, int currentDepth) {
		if (state.isFinalState() || currentDepth == maxDepth) {
			return new ActionValuePair(null, state.getGameValue());
		}

		ActionValuePair min = new ActionValuePair(null, Double.POSITIVE_INFINITY);
		for (Action coup : game.getActions(state)) {
			this.incStateCounter();
			GameState s_suivant = (GameState) game.doAction(state, coup);
			ActionValuePair result = maxValue(s_suivant, alpha, beta, currentDepth + 1);
			if (result.getValue() <= min.getValue()) {
				min = new ActionValuePair(coup, result.getValue());
				if (min.getValue() < beta) {
					beta = min.getValue();
				}
			}
			if (min.getValue() <= alpha) {
				return min;
			}
		}
		return min;
	}
}
