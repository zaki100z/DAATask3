package model;

public class Edge {
    private String from;
    private String to;
    private int weight;

    // Constructor, getters, setters
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    // Getters and setters
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public int getWeight() { return weight; }

    @Override
    public String toString() {
        return "Edge{from='" + from + "', to='" + to + "', weight=" + weight + '}';
    }
}