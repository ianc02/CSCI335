package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    private PriorityQueue<SearchNode<T>> queue;
    private HashMap<SearchNode<T>,T> visited = new HashMap<SearchNode<T>,T>();
    // HINT: Use java.util.PriorityQueue

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.queue = new PriorityQueue<SearchNode<T>>(Comparator.comparingInt(node -> node.getDepth() + heuristic.applyAsInt(node.getValue())));

        // TODO: Your code here

    }

    @Override
    public void enqueue(SearchNode<T> node) {
        // TODO: Your code here
        if (!visited.containsValue(node.getValue())){
            queue.add(node);
            visited.put(node,node.getValue());
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: Your code here
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}
