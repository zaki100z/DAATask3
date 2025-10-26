package algorithm;

import model.Graph;
import model.Edge;
import model.MSTResult;

import java.util.*;

public class PrimsAlgorithm implements MSTAlgorithm {
    private int operationsCount;

    @Override
    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        List<Edge> mstEdges = new ArrayList<>();
        Set<String> inMST = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        // Start with first node
        if (!graph.getNodes().isEmpty()) {
            String startNode = graph.getNodes().get(0);
            inMST.add(startNode);
            operationsCount++;

            // Add all edges from start node to heap
            addEdgesToHeap(graph, startNode, minHeap, inMST);

            while (!minHeap.isEmpty() && inMST.size() < graph.getVertexCount()) {
                operationsCount++;
                Edge minEdge = minHeap.poll();
                operationsCount++;

                String newNode = getNewNode(minEdge, inMST);
                if (newNode != null) {
                    mstEdges.add(minEdge);
                    inMST.add(newNode);
                    operationsCount += 2;

                    addEdgesToHeap(graph, newNode, minHeap, inMST);
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        int totalCost = calculateTotalCost(mstEdges);

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    private void addEdgesToHeap(Graph graph, String node, PriorityQueue<Edge> heap, Set<String> inMST) {
        for (Edge edge : graph.getEdges()) {
            operationsCount++;
            if ((edge.getFrom().equals(node) && !inMST.contains(edge.getTo())) ||
                    (edge.getTo().equals(node) && !inMST.contains(edge.getFrom()))) {
                heap.offer(edge);
                operationsCount++;
            }
        }
    }

    private String getNewNode(Edge edge, Set<String> inMST) {
        operationsCount++;
        if (!inMST.contains(edge.getFrom())) {
            return edge.getFrom();
        } else if (!inMST.contains(edge.getTo())) {
            return edge.getTo();
        }
        return null;
    }

    private int calculateTotalCost(List<Edge> edges) {
        return edges.stream().mapToInt(Edge::getWeight).sum();
    }

    @Override
    public String getAlgorithmName() {
        return "Prim";
    }
}