package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;

public class BFS extends TreeSearch {

    public BFS(SearchProblem p, State s) {
        super(p, s);
    }

    public boolean solve() {
        // Initialisation de la file pour les nœuds à explorer et de l'ensemble des nœuds visités
        Queue<SearchNode> frontier = new ArrayDeque<>();
        HashSet<State> explored = new HashSet<>();

        // Ajouter le nœud racine
        SearchNode root = SearchNode.makeRootSearchNode(initial_state);
        frontier.add(root);

        if (ArgParse.DEBUG)
            System.out.println("[Début de la recherche en largeur]");

        while (!frontier.isEmpty()) {
            // Récupérer le premier nœud de la file
            SearchNode node = frontier.poll();
            State state = node.getState();

            if (ArgParse.DEBUG)
                System.out.println("Exploration de l'état : " + state);

            // Vérifier si l'état courant est un état but
            if (problem.isGoalState(state)) {
                end_node = node;
                if (ArgParse.DEBUG)
                    System.out.println("[But trouvé : " + state + "]");
                return true;
            }

            // Marquer l'état comme visité
            explored.add(state);

            // Récupérer les actions possibles et générer les successeurs
            ArrayList<Action> actions = problem.getActions(state);
            for (Action action : actions) {
                SearchNode childNode = SearchNode.makeChildSearchNode(problem, node, action);
                State childState = childNode.getState();

                // Ajouter à la file uniquement si le successeur n'a pas été exploré
                if (!explored.contains(childState) && !frontier.contains(childNode)) {
                    frontier.add(childNode);
                }
            }
        }

        // Si la file est vide et que nous n'avons pas trouvé le but, la recherche échoue
        if (ArgParse.DEBUG)
            System.out.println("[But non trouvé]");
        return false;
    }
}
