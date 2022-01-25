package maze.heuristics;

import maze.core.MazeExplorer;
import java.util.function.ToIntFunction;


public class Manhattan implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        MazeExplorer goal = node.getGoal();
        int goalx = goal.getLocation().getX();
        int goaly = goal.getLocation().getY();
        int nodex = node.getLocation().getX();
        int nodey = node.getLocation().getY();
        int dx = Math.abs(goalx - nodex);
        int dy = Math.abs(goaly - nodey); //Use absolute Value
        return dx + dy;
    }
}
