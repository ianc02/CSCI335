package learning.markov;

import learning.core.Histogram;

import javax.swing.text.html.Option;
import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        // TODO: YOUR CODE HERE
        if (!(label2symbol2symbol.containsKey(label))){
            label2symbol2symbol.put(label, new HashMap<Optional<S>, Histogram<S>>());
        }
        if (!label2symbol2symbol.get(label).containsKey(prev)){
            label2symbol2symbol.get(label).put(prev, new Histogram<S>());
        }
        label2symbol2symbol.get(label).get(prev).bump(next);
    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        // TODO: YOUR CODE HERE
        Optional<S> prev = Optional.empty();
        double prob = 1.0;
        for (S let :
                sequence) {
            double total = 0.0;
            double cur = 0.0;
            try {
                total = label2symbol2symbol.get(label).get(prev).getTotalCounts();
                cur = label2symbol2symbol.get(label).get(prev).getCountFor(let);
            }
            catch (Exception e) {
                total = 0.0;
                cur = 0.0;
            }
            prev = Optional.of(let);
            prob *= ((cur+1)/(total+1));
        }
        return prob;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L, Double> returnMap = new LinkedHashMap<>();
        double sum = 0.0;
        for (L l :
                allLabels()) {
            sum += probability(sequence, l);
        }
        for (L label :
                allLabels()) {
            double t1 = probability(sequence, label);

            returnMap.put(label,(t1/sum));
        }
        return returnMap;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L, Double> labelMap = labelDistribution(sequence);
        double max = 0.0;
        L maxL = null;
        for (L label :
                allLabels()) {
            if (labelMap.get(label) > max){
                max = labelMap.get(label);
                maxL = label;
            }
        }
        return maxL;
    }
}
