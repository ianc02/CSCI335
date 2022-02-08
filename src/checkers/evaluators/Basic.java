package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class Basic implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        PlayerColor current = c.getCurrentPlayer();
        PlayerColor opponent = current.opponent();
        return c.numPiecesOf(current) - c.numPiecesOf(opponent);
    }
}
