import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DirectedGraph {
    private List<Integer> vertices;
    private Map<Integer, List<Integer>> edges;
    private ExecutorService executorService;
    private AtomicBoolean found = new AtomicBoolean(false);

    public DirectedGraph(List<Integer> vertices, Map<Integer, List<Integer>> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.executorService = Executors.newCachedThreadPool();
    }

    private boolean isAdjacent(int v1, int v2) {
        return this.edges.containsKey(v1) && this.edges.get(v1).contains(v2);
    }

    private boolean isValid(int v2, List<Integer> path) {
        return this.isAdjacent(path.get(path.size() - 1), v2) && !path.contains(v2);
    }

    private void hamCycleUtil(List<Integer> path, int depth) throws InterruptedException, ExecutionException {
        if (found.get()) {
            return;
        }

        if (path.size() == this.vertices.size()) {
            if (this.isAdjacent(path.get(path.size() - 1), path.get(0))) {
                found.set(true);
                path.add(vertices.get(0));
                System.out.println(path);
                return;
            }
        }

        int lastVertex = path.get(path.size() - 1);
        if(depth < 3)
        {List<Future<?>> futures = new ArrayList<>();

            for (int vertex : this.edges.getOrDefault(lastVertex, Collections.emptyList())) {
                if (isValid(vertex, path)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(vertex);
                    Future<?> future = executorService.submit(() -> {
                        try {
                            hamCycleUtil(newPath, depth + 1);
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    futures.add(future);
                }
            }

            for (Future<?> future : futures) {
                future.get();
            }}
        else{

            for (int vertex : this.edges.getOrDefault(lastVertex, Collections.emptyList())) {
                if (isValid(vertex, path)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(vertex);
                    hamCycleUtil(newPath, depth + 1);
                }
            }
        }
    }

    public void hamCycle() {
        List<Integer> path = new ArrayList<>();
        path.add(this.vertices.get(0));
        try {
            hamCycleUtil(path, 0);
            executorService.shutdown();
            if (!found.get()) {
                System.out.println("No Hamiltonian cycle found");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
