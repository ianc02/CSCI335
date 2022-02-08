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

public class Mix extends CheckersSearcher {
    private int numNodes = 0;
    private int win = 10000000;
    private int lose = -10000000;
    private int alpha = lose;
    private int beta = win;
    private int dmax = getDepthLimit();
    private Basic basicEval = new Basic();

    public Mix(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelper(board.getCurrentPlayer(),0, board, 0, alpha, beta);
    }

    public Optional<Duple<Integer,Move>> selectMoveHelper(PlayerColor last, int dmaxModifier, Checkerboard board, int depth, int recursiveAlpha, int recursiveBeta) {
        PlayerColor protagonist = board.getCurrentPlayer();
        PlayerColor adversary = protagonist.opponent();
        ArrayList<Checkerboard> moves = board.getNextBoards();
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
        if (depth > getDepthLimit() +dmaxModifier) {
            if (!protagonist.equals(last)) {
                return Optional.of(new Duple<Integer, Move>(getEvaluator().applyAsInt(board), board.getLastMove()));
            }
        }
        int numberOfEvals = 0;
        int totalEval = 0;
        Hashtable<Checkerboard, Integer> usedBoardsEval = new Hashtable<>();
        Hashtable<Checkerboard, Integer> usedBoardsAlpha = new Hashtable<>();
        Hashtable<Checkerboard, Integer> usedBoardsBeta = new Hashtable<>();
        for (Checkerboard futureBoard :
                moves) {
            numNodes += 1;
            numberOfEvals++;
            Optional<Duple<Integer, Move>> recursiveResult = Optional.empty();
            if (futureBoard.getCurrentPlayer() == adversary) {
                recursiveResult = selectMoveHelper(protagonist, 0,futureBoard, depth + 1, -recursiveBeta, -recursiveAlpha);
                if (recursiveResult.isPresent()) {
                    totalEval+=recursiveResult.get().getFirst();
                    usedBoardsEval.put(futureBoard,recursiveResult.get().getFirst());
                    usedBoardsAlpha.put(futureBoard,-recursiveBeta);
                    usedBoardsBeta.put(futureBoard, -recursiveAlpha);
                    if (returnDuple.isEmpty() || recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                        returnDuple = Optional.of(new Duple<Integer, Move>(-recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                    }
                }
            } else {
                recursiveResult = selectMoveHelper(protagonist, 0,futureBoard, depth + 1, recursiveAlpha, recursiveBeta);
                if (recursiveResult.isPresent()) {
                    totalEval+=recursiveResult.get().getFirst();
                    usedBoardsEval.put(futureBoard,recursiveResult.get().getFirst());
                    usedBoardsAlpha.put(futureBoard,recursiveAlpha);
                    usedBoardsBeta.put(futureBoard,recursiveBeta);
                    if (returnDuple.isEmpty() || recursiveResult.get().getFirst() > returnDuple.get().getFirst()) {
                        returnDuple = Optional.of(new Duple<Integer, Move>(recursiveResult.get().getFirst(), futureBoard.getLastMove()));
                    }
                }
            }
            if (recursiveResult.isPresent()) {
                recursiveAlpha = Math.max(recursiveAlpha, recursiveResult.get().getFirst());
                if (recursiveAlpha >= recursiveBeta) {
                    break;
                }
            }
        }
        Optional<Duple<Integer,Move>> possibleReturnDuple = Optional.empty();
        float average = (float)totalEval/numberOfEvals;
        for (Checkerboard used : moves) {
            if (usedBoardsEval.containsKey(used)) {
                if(usedBoardsEval.get(used)> Math.abs(average)){
                    possibleReturnDuple = selectMoveHelper(protagonist,2, used, depth + 1, usedBoardsAlpha.get(used), usedBoardsBeta.get(used));
                    if(possibleReturnDuple.isPresent()){
                        if (possibleReturnDuple.get().getFirst() > returnDuple.get().getFirst()){
                            if (used.getCurrentPlayer() == protagonist) {
                                returnDuple = possibleReturnDuple;
                            }
                        }
                    }
                }
            }
        }
        return returnDuple;
    }
}
