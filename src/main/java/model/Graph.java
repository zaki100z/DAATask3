package model;

import java.util.List;
import java.util.ArrayList;

public class Graph {
    private int id;
    private List<String> nodes;
    private List<Edge> edges;

    public Graph(int id, List<String> nodes, List<Edge> edges) {
        this.id = id;
        this.nodes = nodes;
        this.edges = edges;
    }

    // Getters
    public int getId() { return id; }
    public List<String> getNodes() { return nodes; }
    public List<Edge> getEdges() { return edges; }
    public int getVertexCount() { return nodes.size(); }
    public int getEdgeCount() { return edges.size(); }
}