package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.*;
import java.util.function.ToIntFunction;


public class BestFirstQueue<T> implements SearchQueue<T> {
    private PriorityQueue<SearchNode<T>> queue;
    private HashMap<T, Integer> visited = new HashMap<>();
    private ToIntFunction<T> heuristic;
    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.getDepth() + heuristic.applyAsInt(node.getValue())));
        this.heuristic = heuristic;
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        if (!visited.containsKey(node.getValue())){
            queue.add(node);
            visited.put(node.getValue(), heuristic.applyAsInt(node.getValue()));
        }
        else{
            if (heuristic.applyAsInt(node.getValue()) < visited.get(node.getValue())){
                queue.add(node);
            }
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}