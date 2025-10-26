package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Graph;
import model.MSTResult;
import model.Edge;
import algorithm.MSTAlgorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONWriter {
    private Gson gson;

    public JSONWriter() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void writeResultsToFile(String filePath,
                                   List<Graph> graphs,
                                   Map<Integer, Map<String, MSTResult>> results) throws IOException {

        JsonObject output = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        for (Graph graph : graphs) {
            JsonObject graphResult = new JsonObject();
            graphResult.addProperty("graph_id", graph.getId());

            // Input statistics
            JsonObject inputStats = new JsonObject();
            inputStats.addProperty("vertices", graph.getVertexCount());
            inputStats.addProperty("edges", graph.getEdgeCount());
            graphResult.add("input_stats", inputStats);

            // Prim's results
            MSTResult primResult = results.get(graph.getId()).get("Prim");
            graphResult.add("prim", createAlgorithmResult(primResult));

            // Kruskal's results
            MSTResult kruskalResult = results.get(graph.getId()).get("Kruskal");
            graphResult.add("kruskal", createAlgorithmResult(kruskalResult));

            resultsArray.add(graphResult);
        }

        output.add("results", resultsArray);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(output, writer);
        }
    }

    private JsonObject createAlgorithmResult(MSTResult result) {
        JsonObject algorithmResult = new JsonObject();

        // MST edges
        JsonArray mstEdgesArray = new JsonArray();
        for (Edge edge : result.getMstEdges()) {
            JsonObject edgeObject = new JsonObject();
            edgeObject.addProperty("from", edge.getFrom());
            edgeObject.addProperty("to", edge.getTo());
            edgeObject.addProperty("weight", edge.getWeight());
            mstEdgesArray.add(edgeObject);
        }
        algorithmResult.add("mst_edges", mstEdgesArray);

        algorithmResult.addProperty("total_cost", result.getTotalCost());
        algorithmResult.addProperty("operations_count", result.getOperationsCount());
        algorithmResult.addProperty("execution_time_ms", result.getExecutionTimeMs());

        return algorithmResult;
    }
}