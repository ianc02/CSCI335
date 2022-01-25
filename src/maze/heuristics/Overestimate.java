package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;


public class Overestimate implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {

        MazeExplorer goal = node.getGoal();
        Set<Pos> treasures = node.getAllTreasureFromMaze();
        int totalDistance = manhattan(goal.getLocation().getX(),goal.getLocation().getY(),node);
        for (Pos treasure :
                treasures) {
            totalDistance += manhattan(treasure.getX(), treasure.getY(), node);
        }
        return totalDistance;

    }

    private int manhattan(int x, int y, MazeExplorer node){
        return Math.abs((x - node.getLocation().getX())) + Math.abs((y - node.getLocation().getY()));
    }
}