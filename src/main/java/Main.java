import model.Graph;
import model.MSTResult;
import model.Edge;
import algorithm.MSTAlgorithm;
import algorithm.PrimsAlgorithm;
import algorithm.KruskalsAlgorithm;
import io.JSONReader;
import io.JSONWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize components
            JSONReader reader = new JSONReader();
            JSONWriter writer = new JSONWriter();
            MSTAlgorithm prim = new PrimsAlgorithm();
            MSTAlgorithm kruskal = new KruskalsAlgorithm();

            // Find or create input file
            String inputFilePath = findInputFilePath();
            File inputFile = new File(inputFilePath);

            if (!inputFile.exists()) {
                createInputFileIfNotExists();
            }

            // Read input graphs
            List<Graph> graphs = reader.readGraphsFromFile(inputFilePath);
            System.out.println("Loaded " + graphs.size() + " graphs");

            // Process each graph
            Map<Integer, Map<String, MSTResult>> results = new HashMap<>();

            for (Graph graph : graphs) {
                System.out.println("\nProcessing Graph " + graph.getId() +
                        " (Vertices: " + graph.getVertexCount() +
                        ", Edges: " + graph.getEdgeCount() + ")");

                Map<String, MSTResult> algorithmResults = new HashMap<>();

                // Run Prim's algorithm
                System.out.println("Running Prim's algorithm...");
                MSTResult primResult = prim.findMST(graph);
                algorithmResults.put("Prim", primResult);
                printResults("Prim", primResult);

                // Run Kruskal's algorithm
                System.out.println("Running Kruskal's algorithm...");
                MSTResult kruskalResult = kruskal.findMST(graph);
                algorithmResults.put("Kruskal", kruskalResult);
                printResults("Kruskal", kruskalResult);

                // Verify both algorithms produce same total cost
                if (primResult.getTotalCost() != kruskalResult.getTotalCost()) {
                    System.err.println("WARNING: Total cost mismatch between Prim and Kruskal!");
                }

                results.put(graph.getId(), algorithmResults);
            }

            // Write results to output file
            writer.writeResultsToFile("ass_3_output.json", graphs, results);
            System.out.println("\nResults written to ass_3_output.json");

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String findInputFilePath() {
        String[] possiblePaths = {
                "ass_3_input.json",
                "src/main/resources/ass_3_input.json",
                "resources/ass_3_input.json",
                "input/ass_3_input.json"
        };

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("Found input file at: " + file.getAbsolutePath());
                return path;
            }
        }

        // If no file found, use the resources directory
        String defaultPath = "src/main/resources/ass_3_input.json";
        System.out.println("Input file not found. Will create at: " + defaultPath);
        return defaultPath;
    }

    private static void createInputFileIfNotExists() throws IOException {
        String filePath = "src/main/resources/ass_3_input.json";

        // Create resources directory if it doesn't exist
        Files.createDirectories(Paths.get("src/main/resources"));

        // Create the JSON file with the required content
        String jsonContent = """
            {
              "graphs": [
                {
                  "id": 1,
                  "nodes": ["A", "B", "C", "D", "E"],
                  "edges": [
                    {"from": "A", "to": "B", "weight": 4},
                    {"from": "A", "to": "C", "weight": 3},
                    {"from": "B", "to": "C", "weight": 2},
                    {"from": "B", "to": "D", "weight": 5},
                    {"from": "C", "to": "D", "weight": 7},
                    {"from": "C", "to": "E", "weight": 8},
                    {"from": "D", "to": "E", "weight": 6}
                  ]
                },
                {
                  "id": 2,
                  "nodes": ["A", "B", "C", "D"],
                  "edges": [
                    {"from": "A", "to": "B", "weight": 1},
                    {"from": "A", "to": "C", "weight": 4},
                    {"from": "B", "to": "C", "weight": 2},
                    {"from": "C", "to": "D", "weight": 3},
                    {"from": "B", "to": "D", "weight": 5}
                  ]
                }
              ]
            }""";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonContent);
        }
        System.out.println("Created input file: " + filePath);
    }

    private static void printResults(String algorithmName, MSTResult result) {
        System.out.println(algorithmName + " Results:");
        System.out.println("  Total Cost: " + result.getTotalCost());
        System.out.println("  Operations: " + result.getOperationsCount());
        System.out.println("  Execution Time: " + result.getExecutionTimeMs() + " ms");
        System.out.println("  MST Edges: " + result.getMstEdges().size());
        for (Edge edge : result.getMstEdges()) {
            System.out.println("    " + edge.getFrom() + " - " + edge.getTo() + " (" + edge.getWeight() + ")");
        }
    }
}