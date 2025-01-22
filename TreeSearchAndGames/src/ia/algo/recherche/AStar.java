package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar extends TreeSearch {
    public AStar(SearchProblem p, State s) {super(p, s);}

    @Override
    public boolean solve() {
        // File de priorité basée sur la fonction f(n) = g(n) + h(n)
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>(
                (n1, n2) -> Double.compare(n1.getCost() + n1.getHeuristic(), n2.getCost() + n2.getHeuristic())
        );
        HashSet<State> explored = new HashSet<>();

        // Ajouter le nœud racine
        SearchNode root = SearchNode.makeRootSearchNode(initial_state);
        frontier.add(root);

        if (ArgParse.DEBUG)
            System.out.println("[Début de la recherche A*]");

        while (!frontier.isEmpty()) {
            // Récupérer le nœud avec le plus petit f(n)
            SearchNode node = frontier.poll();
            State state = node.getState();

            if (ArgParse.DEBUG)
                System.out.println("Exploration de l'état : " + state +
                        ", coût g(n) : " + node.getCost() +
                        ", heuristique h(n) : " + node.getHeuristic());

            // Vérifier si l'état courant est un état but
            if (problem.isGoalState(state)) {
                end_node = node;
                if (ArgParse.DEBUG)
                    System.out.println("[But trouvé : " + state +
                            ", coût total f(n) : " + (node.getCost() + node.getHeuristic()) + "]");
                return true;
            }

            // Marquer l'état comme visité
            explored.add(state);

            // Récupérer les actions possibles et générer les successeurs
            for (Action action : problem.getActions(state)) {
                SearchNode childNode = SearchNode.makeChildSearchNode(problem, node, action);
                State childState = childNode.getState();

                // Ajouter à la frontière uniquement si le successeur n'a pas été exploré
                if (!explored.contains(childState)) {
                    frontier.add(childNode);
                } else {
                    // Vérifier si un chemin moins coûteux existe
                    for (SearchNode n : frontier) {
                        if (n.getState().equals(childState) &&
                                (n.getCost() + n.getHeuristic() > childNode.getCost() + childNode.getHeuristic())) {
                            frontier.remove(n);
                            frontier.add(childNode);
                            break;
                        }
                    }
                }
            }
        }

        // Si la file est vide et que nous n'avons pas trouvé le but, la recherche échoue
        if (ArgParse.DEBUG)
            System.out.println("[But non trouvé]");
        return false;
    }
}
