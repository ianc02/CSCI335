package learning.handwriting.learners;

import learning.classifiers.Knn;
import learning.handwriting.core.Drawing;

public class Knn10 extends Knn<Drawing,String> {
    public Knn10() {
        super(10, (d1, d2) -> (double)d1.distance(d2));
    }
}
