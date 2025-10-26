package io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Graph;
import model.Edge;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    private Gson gson;

    public JSONReader() {
        this.gson = new Gson();
    }

    public List<Graph> readGraphsFromFile(String filePath) throws IOException {
        List<Graph> graphs = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray graphsArray = jsonObject.getAsJsonArray("graphs");

            for (JsonElement graphElement : graphsArray) {
                JsonObject graphObject = graphElement.getAsJsonObject();

                int id = graphObject.get("id").getAsInt();

                // Read nodes
                List<String> nodes = new ArrayList<>();
                JsonArray nodesArray = graphObject.getAsJsonArray("nodes");
                for (JsonElement nodeElement : nodesArray) {
                    nodes.add(nodeElement.getAsString());
                }

                // Read edges
                List<Edge> edges = new ArrayList<>();
                JsonArray edgesArray = graphObject.getAsJsonArray("edges");
                for (JsonElement edgeElement : edgesArray) {
                    JsonObject edgeObject = edgeElement.getAsJsonObject();
                    String from = edgeObject.get("from").getAsString();
                    String to = edgeObject.get("to").getAsString();
                    int weight = edgeObject.get("weight").getAsInt();
                    edges.add(new Edge(from, to, weight));
                }

                graphs.add(new Graph(id, nodes, edges));
            }
        }

        return graphs;
    }
}