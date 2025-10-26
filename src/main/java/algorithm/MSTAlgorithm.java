package algorithm;

import model.Graph;
import model.MSTResult;

public interface MSTAlgorithm {
    MSTResult findMST(Graph graph);
    String getAlgorithmName();
}