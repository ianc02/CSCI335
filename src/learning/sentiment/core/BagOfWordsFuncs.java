package learning.sentiment.core;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BagOfWordsFuncs {
    public static <K, N extends Number> double cosineDistance(Map<K,N> a, Map<K, N> b) {
        return 1.0 - cosineSimilarity(a, b);
    }

    public static <K, N extends Number> double cosineSimilarity(Map<K,N> a, Map<K, N> b) {
        return dotProduct(a, b) / (magnitude(a) * magnitude(b));
    }

    public static <K, N extends Number> double magnitude(Map<K,N> a) {
        return Math.sqrt(dotProduct(a, a));
    }

    public static <K, N extends Number> double dotProduct(Map<K, N> a, Map<K, N> b) {
        double result = 0.0;
        for (K key: keysFromBoth(a, b)) {
            result += a.get(key).doubleValue() * b.get(key).doubleValue();
        }
        return result;
    }

    public static <K, N extends Number> HashMap<K,Double> weightedAverage(Map<K,N> a, Map<K, N> b, double aWeight) {
        // TODO: Find the weighted average of Maps a and b. Multiply the value for each key in a with aWeight,
        //  then multiply the corresponding value from b by (1 - aWeight), and add them.
        //  Since N extends Number, you can use the Number.doubleValue() method to get a concrete value.
        HashMap<K, Double> averages = new HashMap<>();
        for (K key :
                keysFromBoth(a,b)) {
            averages.put(key,((a.get(key).doubleValue() *aWeight) + (b.get(key).doubleValue() * (1-aWeight))));
        }
        return averages;
    }

    public static <K, N> HashSet<K> allKeysFrom(Map<K,N> a, Map<K, N> b) {
        HashSet<K> all = new HashSet<>(a.keySet());
        all.addAll(b.keySet());
        return all;
    }

    public static <K, N> HashSet<K> keysFromBoth(Map<K, N> a, Map<K, N> b) {
        HashSet<K> both = new HashSet<>(a.keySet());
        both.retainAll(b.keySet());
        return both;
    }
}
