package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

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

		ActionValuePair best = new ActionValuePair(null, Double.NEGATIVE_INFINITY);
		for (Action action : game.getActions(state)) {
			this.incStateCounter();
			ActionValuePair result = minValue((GameState) game.doAction(state, action), currentDepth + 1);
			if (result.getValue() >= best.getValue()) {
				best = new ActionValuePair(action, result.getValue());
			}
		}
		return best;
	}

	private ActionValuePair minValue(GameState state, int currentDepth) {
		if (state.isFinalState() || currentDepth == maxDepth) {
			return new ActionValuePair(null, state.getGameValue());
		}

		ActionValuePair best = new ActionValuePair(null, Double.POSITIVE_INFINITY);
		for (Action action : game.getActions(state)) {
			this.incStateCounter();
			ActionValuePair result = maxValue((GameState) game.doAction(state, action), currentDepth + 1);
			if (result.getValue() <= best.getValue()) {
				best = new ActionValuePair(action, result.getValue());
			}
		}
		return best;
	}
}
