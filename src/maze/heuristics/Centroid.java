package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Set;
import java.util.function.ToIntFunction;


public class Centroid implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        MazeExplorer goal = node.getGoal();
        Set<Pos> treasures = node.getAllTreasureFromMaze();
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
        int nx = node.getLocation().getX();
        int ny = node.getLocation().getY();
        int dx = ((x + gx + nx)/3);
        int dy = ((y + gy+ ny)/3);
        return (Math.abs(nx-dx) + Math.abs(gx-dx) + Math.abs(ny-dy) + Math.abs(gy-dy));
    }
}
