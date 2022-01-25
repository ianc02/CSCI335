package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.ToIntFunction;


public class GreedyTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        MazeExplorer goal = node.getGoal();
        Set<Pos> treasures = node.getAllTreasureFromMaze();
        Set<Pos> found = node.getAllTreasureFound();
        if (treasures.isEmpty()) {
            return manhattan(goal.getLocation().getX(), goal.getLocation().getY(), node);
        }
        new ArrayList<Pos>(treasures).removeAll(found);
        Pos min = null;
        for (Pos t :
                treasures) {
            if (min == null) {
                min = t;
            }
            else{
                if (manhattan(t.getX(),t.getY(),node) < manhattan(min.getX(), min.getY(), node)){
                    min = t;
                }
            }
        }
        return manhattan(min.getX(), min.getY(), node) + manhattan(min.getX(),min.getY(),goal);
    }

    private int manhattan(int x, int y, MazeExplorer node){
        return Math.abs((x - node.getLocation().getX())) + Math.abs((y - node.getLocation().getY()));
    }
}
