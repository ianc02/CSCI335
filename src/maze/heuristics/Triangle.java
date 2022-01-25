package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;


public class Triangle implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        MazeExplorer goal = node.getGoal();
        Set<Pos> treasures = node.getAllTreasureFromMaze();
        int goalToTreasure = 0;
        int nodeToTreasure = 0;
        int goalToNode = 0;
        int x = 0;
        int y = 0;
        int c = 0;
        for (Pos t :
                treasures) {
            x += t.getX();
            y += t.getY();
            c ++;
        }
        if (c>0) {
            x /= c;
            y /= c;
        }
        int gx = goal.getLocation().getX();
        int gy = goal.getLocation().getY();
        int nx = goal.getLocation().getX();
        int ny = goal.getLocation().getY();
        goalToTreasure = Math.abs(x-gx) + Math.abs(y-gy);
        goalToNode = Math.abs(gx-nx) + Math.abs(gy-ny);
        nodeToTreasure = Math.abs(x-nx) + Math.abs(y-ny);
        int dx = ((goalToTreasure + goalToNode + nodeToTreasure));
        int dy = ((goalToTreasure + goalToNode+ nodeToTreasure));
        return dx + dy;
    }
}
