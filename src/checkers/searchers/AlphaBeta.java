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

public class AlphaBeta extends CheckersSearcher {
    private int numNodes = 0;
    private int win = 10000000;
    private int lose = -10000000;
    private int alpha = lose;
    private int beta = win;
    private int dmax = getDepthLimit();
    private Basic basicEval = new Basic();

    public AlphaBeta(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelper(board, 0, alpha, beta);
    }

    public Optional<Duple<Integer,Move>> selectMoveHelper(Checkerboard board, int depth, int recursiveAlpha, int recursiveBeta) {
        PlayerColor protagonist = board.getCurrentPlayer();
        PlayerColor adversary = protagonist.opponent();
        ArrayList<Checkerboard> moves = board.getNextBoards();
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
            Optional<Duple<Integer, Move>> recursiveResult = Optional.empty();
                if (futureBoard.getCurrentPlayer() == adversary) {
                    recursiveResult = selectMoveHelper(futureBoard, depth + 1, recursiveAlpha, recursiveBeta);
                    if (recursiveResult.isPresent()) {
                        if (returnDuple.isEmpty() || -recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                            returnDuple = Optional.of(new Duple<Integer, Move>(-recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                        }
                        if (recursiveResult.get().getFirst() < recursiveBeta){
                            recursiveBeta = recursiveResult.get().getFirst();
                        }
                    }
                } else {
                    recursiveResult = selectMoveHelper(futureBoard, depth + 1, recursiveAlpha, recursiveBeta);
                    if (recursiveResult.isPresent()) {
                        if (returnDuple.isEmpty() || recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                            returnDuple = Optional.of(new Duple<Integer, Move>(recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                        }
                        if (recursiveResult.get().getFirst() > recursiveAlpha){
                            recursiveAlpha = recursiveResult.get().getFirst();
                        }
                    }
                }
                if (recursiveAlpha >= recursiveBeta) {
                    break;

            }
        }
        return returnDuple;
    }
}
