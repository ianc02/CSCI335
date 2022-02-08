package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import checkers.evaluators.Basic;
import core.Duple;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {
    private int numNodes = 0;
    private int win = 10000000;
    private int lose = -10000000;
    private int dmax = getDepthLimit();
    private Basic basicEval = new Basic();

    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelper(board, 0);
    }

    public Optional<Duple<Integer,Move>> selectMoveHelper(Checkerboard board, int depth) {
        // ALGORITHM HERE
        PlayerColor protagonist = board.getCurrentPlayer();
        PlayerColor adversary = protagonist.opponent();
        ArrayList<Checkerboard> moves = board.getNextBoards();
        // Check board before getting successors
        int max = lose;
        Optional<Duple<Integer, Move>> returnDuple = Optional.empty();
        if (board.gameOver()) {
            if (board.playerWins(protagonist)) {
                return Optional.of(new Duple<Integer, Move>(win, board.getLastMove()));
            } else if (board.playerWins(adversary)) {
                return Optional.of(new Duple<Integer, Move>(lose, board.getLastMove()));
            } else {
                return Optional.of(new Duple<Integer, Move>(0, board.getLastMove()));
            }
        }
        if (depth > getDepthLimit()) {
            return Optional.of(new Duple<Integer, Move>(getEvaluator().applyAsInt(board), board.getLastMove()));
        }
        for (Checkerboard futureBoard :
                moves) {
            numNodes += 1;
            Optional<Duple<Integer, Move>> recursiveResult = selectMoveHelper(futureBoard, depth + 1);
            if (recursiveResult.isPresent()) {
                if (futureBoard.getCurrentPlayer() == adversary) {
                    if (returnDuple.isEmpty() || recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                        returnDuple = Optional.of(new Duple<Integer, Move>(-recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                    }
                } else {
                    if (returnDuple.isEmpty() || recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                        returnDuple = Optional.of(new Duple<Integer, Move>(recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                    }
                }
            }
        }

        return returnDuple;
    }
}
