package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.PriorityQueue;

public class UCS extends TreeSearch {
    public UCS(SearchProblem p, State s) {super(p, s);}

    @Override
    public boolean solve() {
        // File de priorité pour les nœuds à explorer
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>((n1, n2) -> Double.compare(n1.getCost(), n2.getCost()));
        HashSet<State> explored = new HashSet<>();

        // Ajouter le nœud racine
        SearchNode root = SearchNode.makeRootSearchNode(initial_state);
        frontier.add(root);

        if (ArgParse.DEBUG)
            System.out.println("[Début de la recherche en coût uniforme]");

        while (!frontier.isEmpty()) {
            // Récupérer le nœud avec le coût le plus faible
            SearchNode node = frontier.poll();
            State state = node.getState();

            if (ArgParse.DEBUG)
                System.out.println("Exploration de l'état : " + state + ", coût total : " + node.getCost());

            // Vérifier si l'état courant est un état but
            if (problem.isGoalState(state)) {
                end_node = node;
                if (ArgParse.DEBUG)
                    System.out.println("[But trouvé : " + state + ", coût total : " + node.getCost() + "]");
                return true;
            }

            // Marquer l'état comme visité
            explored.add(state);

            // Récupérer les actions possibles et générer les successeurs
            for (Action action : problem.getActions(state)) {
                SearchNode childNode = SearchNode.makeChildSearchNode(problem, node, action);
                State childState = childNode.getState();

                // Ajouter à la frontière uniquement si le successeur n'a pas été exploré ou si un meilleur chemin est trouvé
                if (!explored.contains(childState)) {
                    frontier.add(childNode);
                } else {
                    // Vérifier si un chemin moins coûteux existe
                    for (SearchNode n : frontier) {
                        if (n.getState().equals(childState) && n.getCost() > childNode.getCost()) {
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
