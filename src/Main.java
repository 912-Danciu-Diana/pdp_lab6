import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> vertices = new ArrayList<>(Arrays.asList(0,1,2,3));
        Map<Integer, List<Integer>> edges = new HashMap<>();
        edges.put(0, Arrays.asList(1, 2));
        edges.put(1, List.of(2));
        edges.put(2, Arrays.asList(0, 3));
        edges.put(3, Arrays.asList(3, 0));
        DirectedGraph g = new DirectedGraph(vertices, edges);
        g.hamCycle();
    }
}