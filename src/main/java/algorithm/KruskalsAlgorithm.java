package algorithm;

import model.Graph;
import model.Edge;
import model.MSTResult;

import java.util.*;

public class KruskalsAlgorithm implements MSTAlgorithm {
    private int operationsCount;

    @Override
    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        // Sort edges by weight
        sortedEdges.sort(Comparator.comparingInt(Edge::getWeight));
        operationsCount += sortedEdges.size() * (int) Math.log(sortedEdges.size()); // Approximate sort operations

        UnionFind uf = new UnionFind(graph.getNodes());

        for (Edge edge : sortedEdges) {
            operationsCount++;
            String fromRoot = uf.find(edge.getFrom());
            String toRoot = uf.find(edge.getTo());
            operationsCount += 2;

            if (!fromRoot.equals(toRoot)) {
                mstEdges.add(edge);
                uf.union(edge.getFrom(), edge.getTo());
                operationsCount++;

                if (mstEdges.size() == graph.getVertexCount() - 1) {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        int totalCost = calculateTotalCost(mstEdges);

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    private int calculateTotalCost(List<Edge> edges) {
        return edges.stream().mapToInt(Edge::getWeight).sum();
    }

    @Override
    public String getAlgorithmName() {
        return "Kruskal";
    }

    // Union-Find (Disjoint Set) implementation
    private class UnionFind {
        private Map<String, String> parent;
        private Map<String, Integer> rank;

        public UnionFind(List<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public String find(String node) {
            operationsCount++;
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node))); // Path compression
                operationsCount++;
            }
            return parent.get(node);
        }

        public void union(String node1, String node2) {
            operationsCount++;
            String root1 = find(node1);
            String root2 = find(node2);

            if (!root1.equals(root2)) {
                // Union by rank
                if (rank.get(root1) < rank.get(root2)) {
                    parent.put(root1, root2);
                } else if (rank.get(root1) > rank.get(root2)) {
                    parent.put(root2, root1);
                } else {
                    parent.put(root2, root1);
                    rank.put(root1, rank.get(root1) + 1);
                }
                operationsCount += 3;
            }
        }
    }
}