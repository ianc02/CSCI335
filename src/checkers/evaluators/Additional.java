package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class Additional implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        PlayerColor current = c.getCurrentPlayer();
        PlayerColor opponent = current.opponent();
        int value = 0;
        int pieces = c.numPiecesOf(current) - c.numPiecesOf(opponent);
        int kings = c.numKingsOf(current) - c.numKingsOf(opponent);
        int heat = 0;
        int back = 0;
        for (int i = 0; i < c.maxCol()+1; i++){
            for (int j = 0; j < c.maxRow()+1; j++){
                if (j==0){
                    if (c.colorAt(j,i,current) && current.equals(PlayerColor.RED)){
                        back +=1;
                    }
                }
                if (j==7){
                    if (c.colorAt(j,i,current) && current.equals(PlayerColor.BLACK)){
                        back +=1;
                    }
                }
                if (c.colorAt(j, i, current)) {
                    if (i==0 || i == 7){
                        heat += 8;
                    }
                    else if (i==1 || i == 6){
                        heat += 4;
                    }
                    else if (i==2 || i == 5){
                        heat += 2;
                    }
                    else{
                        heat += 1;
                    }
                }
            }
        }
        return pieces*2 + kings*50 + heat + back*200;
    }
}
