package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class DFS extends TreeSearch {

    public DFS(SearchProblem p, State s) {
        super(p, s);
    }

    public boolean solve() {
        // Initialisation de la pile pour les nœuds à explorer et de l'ensemble des nœuds visités
        Stack<SearchNode> frontier = new Stack<>();
        HashSet<State> explored = new HashSet<>();

        // Ajouter le nœud racine
        SearchNode root = SearchNode.makeRootSearchNode(initial_state);
        frontier.push(root);

        if (ArgParse.DEBUG)
            System.out.println("[Début de la recherche en profondeur]");

        while (!frontier.isEmpty()) {
            // Récupérer le dernier nœud ajouté à la pile
            SearchNode node = frontier.pop();
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

                // Ajouter à la pile uniquement si le successeur n'a pas été exploré
                if (!explored.contains(childState) && !frontier.contains(childNode)) {
                    frontier.push(childNode);
                }
            }
        }

        // Si la pile est vide et que nous n'avons pas trouvé le but, la recherche échoue
        if (ArgParse.DEBUG)
            System.out.println("[But non trouvé]");
        return false;
    }
}
