import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MainDijksta {

    public static void main(String[] args) {

       String graphType = Graph.TEST;
       Graph graph = new Graph(graphType);
        Path file = Paths.get("test_shortest_path"+".txt");
        findDistance(graph, file);
        //----------------------------------
        graphType = Graph.NOEL;
        graph = new Graph(graphType);
        file = Paths.get("noel_shortest_path"+".txt");
        findDistance(graph, file);
        //---------------------------------
        graphType = Graph.SHENTEL;
        graph = new Graph(graphType);
        file = Paths.get("shentel_shortest_path"+".txt");
        findDistance(graph, file);
        //-----------------------------------
        graphType = Graph.SAGO;
        graph = new Graph(graphType);
        file = Paths.get("sago_shortest_path"+".txt");
        findDistance(graph, file);
        //----------------------------------------
        graphType = Graph.SPIRALIGHT;
        graph = new Graph(graphType);
        file = Paths.get("spiralight_shortest_path"+".txt");
        findDistance(graph, file);
        graphType = Graph.MISSOURI;
        graph = new Graph(graphType);
        file = Paths.get("missouri_shortest_path"+".txt");
        findDistance(graph, file);
    }

    private static void findDistance(Graph graph, Path file) {
        for (int nodeIndex =0 ; nodeIndex < graph.model.nodeNum ; nodeIndex ++){
           float[] dist_array = graph.model.dijkstra(graph.model.makeGraphMatrix(), nodeIndex);
           List<String> list = changeArrToList(dist_array);
           try {
               Files.write(file,list, StandardOpenOption.APPEND);
           } catch (IOException e) {
               e.printStackTrace();
           }

       }
    }

    private static List<String> changeArrToList(float[] dist_array) {
        List<String> list = new ArrayList<>();
        StringBuilder stt = new StringBuilder();
        for (int i=0; i< dist_array.length ; i ++){
            if (i == dist_array.length-1)stt.append(dist_array[i]);
            else  stt.append(dist_array[i]).append(" ");

        }
        list.add(stt.toString());
        return list;
    }

}

 class GraphModel {
    public int nodeNum;
    public int linkNum;
    public List<EdgeModel> edgeModelList = new ArrayList<>();
    public List<NodeModel> nodeModelList = new ArrayList<>();
    public int maxLevel;


    public long[][] makeGraphMatrix() {
        long graph[][] = new long[nodeNum][nodeNum];
        for (int xIndex = 0; xIndex < nodeNum; xIndex++) {
            for (int yIndex = 0; yIndex < nodeNum; yIndex++) {
                for (int edge = 0; edge < edgeModelList.size(); edge++) {
                    if (xIndex == (int) edgeModelList.get(edge).source && yIndex == (int) edgeModelList.get(edge).target ||
                            xIndex == (int) edgeModelList.get(edge).target && yIndex == (int) edgeModelList.get(edge).source) {
                        graph[xIndex][yIndex] = edgeModelList.get(edge).distance;
                        break;
                    } else {
                        graph[xIndex][yIndex] = 0;
                    }
                }

            }
        }
        return graph;
    }


    // Function that implements Dijkstra's
    // single source shortest path
    // algorithm for a graph represented
    // using adjacency matrix
    // representation
    public float[] dijkstra(long[][] adjacencyMatrix, int startVertex) {
        int nVertices = adjacencyMatrix[0].length;

        // shortestDistances[i] will hold the
        // shortest distance from src to i
        float[] shortestDistances = new float[nVertices];

        // added[i] will true if vertex i is
        // included / in shortest path tree
        // or shortest distance from src to
        // i is finalized
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as
        // INFINITE and added[] as false
        for (int vertexIndex = 0; vertexIndex < nVertices;
             vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        // Distance of source vertex from
        // itself is always 0
        shortestDistances[startVertex] = 0;

        // Parent array to store shortest
        // path tree
        long[] parents = new long[nVertices];

        // The starting vertex does not
        // have a parent
        parents[startVertex] = -1;

        // Find shortest path for all
        // vertices
        for (int i = 1; i < nVertices; i++) {

            // Pick the minimum distance vertex
            // from the set of vertices not yet
            // processed. nearestVertex is
            // always equal to startNode in
            // first iteration.
            int nearestVertex = -1;
            float shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++) {
                if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] <
                                shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as
            // processed
            added[nearestVertex] = true;

            // Update dist value of the
            // adjacent vertices of the
            // picked vertex.
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++) {
                long edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0
                        && ((shortestDistance + edgeDistance) <
                        shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        float[] dist_array = shortestDistances;
        return dist_array ;
        // printSolution(startVertex, shortestDistances, parents);
    }

    // A utility function to print
    // the constructed distances
    // array and shortest paths
    private void printSolution(int startVertex,
                               long[] distances,
                               long[] parents) {
        int nVertices = distances.length;
        System.out.print("Vertex\t Distance\tPath");

        for (int vertexIndex = 0;
             vertexIndex < nVertices;
             vertexIndex++) {
            if (vertexIndex != startVertex) {
                System.out.print("\n" + startVertex + " -> ");
                System.out.print(vertexIndex + " \t\t ");
                System.out.print(distances[vertexIndex] + "\t\t");
                printPath(vertexIndex, parents);
            }
        }
    }

    // Function to print shortest path
    // from source to currentVertex
    // using parents array
    private void printPath(long currentVertex,
                           long[] parents) {

        // Base case : Source node has
        // been processed
        if (currentVertex == -1) {
            return;
        }
        printPath(parents[(int) currentVertex], parents);
        System.out.print(currentVertex + " ");
    }
}

 class EdgeModel {

    public long source;
    public long target;
    public String id;
    public long distance;

    public EdgeModel(long source, long target, String id, long distance) {
        this.source = source;
        this.target = target;
        this.id = id;
        this.distance = distance;
    }
}

 class NodeModel {
    public String name;
    public long id;

    public NodeModel(String name, long id) {
        this.name = name;
        this.id = id;
    }
}

/**
 * @author boshra
 * this class is for making graph of mec server: we use graphs of real world obtained from http://www.topology-zoo.org/explore.html
 * utils.Graph 1 : Spiralight               model.NodeModel = 15 Link = 16
 * utils.Graph 2 : Sago                     model.NodeModel = 18 Link = 17
 * utils.Graph 3 : Shentel                  model.NodeModel = 28 Link = 35
 * utils.Graph 4 : Missouri                 model.NodeModel = 67 Link = 83
 */

 class Graph {
    public static final String SPIRALIGHT = "spiralight";
    public static final String SAGO = "sago";
    public static final String SHENTEL = "shentel";
    public static final String MISSOURI = "missouri";
    public static final String TEST = "test";
    public static final String NOEL = "noel";
    public GraphModel model;


    public Graph(String type) {
        model = new GraphModel();
        switch (type) {
            case SPIRALIGHT:
                model = creatGraph(SPIRALIGHT);
                break;
            case NOEL:
                model = creatGraph(NOEL);
                break;
            case SAGO:
                model = creatGraph(SAGO);
                break;
            case SHENTEL:
                model = creatGraph(SHENTEL);
                break;
            case MISSOURI:
                model = creatGraph(MISSOURI);
                break;
            case TEST:
                model = creatGraph(TEST);
                break;
        }


    }

    public GraphModel getGraphModel() {
        return model;
    }

    private GraphModel creatGraph(String type) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {

            switch (type) {
                case TEST:
                    jsonObject = (JSONObject) parser.parse(new FileReader("test.json"));
                    break;
                case NOEL:
                    jsonObject = (JSONObject) parser.parse(new FileReader("Noel.json"));
                    break;
                case SPIRALIGHT:
                    jsonObject = (JSONObject) parser.parse(new FileReader("Spiralight.json"));
                    break;
                case SAGO:
                    jsonObject = (JSONObject) parser.parse(new FileReader("Sago.json"));
                    break;
                case SHENTEL:
                    jsonObject = (JSONObject) parser.parse(new FileReader("Shentel.json"));
                    break;
                case MISSOURI:
                    jsonObject = (JSONObject) parser.parse(new FileReader("Missouri.json"));
                    break;
            }
        } catch (Exception e) {
        }
        GraphModel graphModel = parseJsonFile(jsonObject);
        return graphModel;
    }

    private GraphModel parseJsonFile(JSONObject jsonObject) {

        GraphModel graphModel = new GraphModel();
        JSONArray nodesArr = (JSONArray) jsonObject.get("nodes");
        graphModel.nodeNum = nodesArr.size();

        JSONArray edgeArr = (JSONArray) jsonObject.get("edges");
        graphModel.linkNum = edgeArr.size();

        for (Object aNodesArr : nodesArr) {
            JSONObject object = (JSONObject) aNodesArr;
            String name = (String) object.get("label");
            long id = (long) object.get("id");
            NodeModel nodeModel = new NodeModel(name, id);
            graphModel.nodeModelList.add(nodeModel);

        }

       // graphModel.prepareAdjacencyList();
        for (int i = 0; i < graphModel.nodeNum; i++) {
            for (int j = 0; j < graphModel.linkNum; j++) {
                JSONObject obj = (JSONObject) edgeArr.get(j);
                long source = (long) obj.get("source");
                long target = (long) obj.get("target");
                String id = (String) obj.get("id");
                long distance = (long) obj.get("distance");

                if (source == i) {
                    EdgeModel edgeModel = new EdgeModel(source, target, id, distance);
                    //graphModel.addAdjacencyEdge((int) source, (int) target);
                    graphModel.edgeModelList.add(edgeModel);
                }

            }

        }

        return graphModel;
    }
}
