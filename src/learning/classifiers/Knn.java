package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;


    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }


    @Override
    public L classify(V value) {
        // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
        //  to find the most popular label.
        Comparator<Duple<Double, L>> comparator = new Comparator<Duple<Double, L>>() {
            @Override
            public int compare(Duple<Double, L> o1, Duple<Double, L> o2) {
                Double dist1 = o1.getFirst();
                Double dist2 = o2.getFirst();
                if (dist1.toString().equals("NaN")){
                    return -1;
                }
                if (dist2.toString().equals("NaN")){
                    return 1;
                }
                if (dist1<dist2){
                    return -1;
                }
                else if (dist2<dist1){
                    return 1;
                }
                return 0;
            }
        };
        ArrayList<Duple<Double, L>> distanceLabels = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        for (Duple<V, L> dataPoint :
                data) {
            double dist = distance.applyAsDouble(value, dataPoint.getFirst());

            distanceLabels.add(new Duple<>(dist, dataPoint.getSecond()));
            distances.add(dist);

        }
        Histogram<L> histogram = new Histogram<>();

        distanceLabels.sort(comparator);
        ArrayList<Duple<Double,L>> kList = new ArrayList<>();
        if (k<=distanceLabels.size()){
            for(int i = 0; i<k; i++){
                kList.add(distanceLabels.get(i));
                histogram.bump(distanceLabels.get(i).getSecond());
            }
            return histogram.getPluralityWinner();
        }
        else{
            for (Duple<Double, L> dLabel :
                    distanceLabels) {
                histogram.bump(dLabel.getSecond());
            }
            return histogram.getPluralityWinner();
        }



    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        data.addAll(training);
    }
}
