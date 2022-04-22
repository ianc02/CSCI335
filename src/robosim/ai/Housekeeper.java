package robosim.ai;
import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

import java.util.ArrayList;
import java.util.Optional;

public class Housekeeper implements Controller {
    private QTable qValues = new QTable(closefar.values().length, Action.values().length,
            1, 8, 200, 0.25);
    @Override
    public void control(Simulator sim) {
        closefar state = getState(sim);
        int chosenAction = qValues.senseActLearn(state.getIndex(), reward(sim));
        Action.values()[chosenAction].applyTo(sim);
    }
    public closefar getState(Simulator sim) {
        ArrayList<Duple<SimObject, Polar>> visible = sim.allVisibleObjects();

        if (sim.findClosestDirt().isPresent() && sim.findClosestDirt().get().getR() < 50){
            return closefar.DIRTCLOSE;
        }
        if (sim.findClosestProblem() < 30) {
            return closefar.CLOSE;


        } else{
            return closefar.FAR;
        }
    }
    public double reward(Simulator sim) {
        if (sim.isVaccumed()){
            return 100.0;
        }
        else if (sim.wasHit()) {
            return -10.0;
        }


//        else if (Action.values()[qValues.getLastAction()].equals(Action.FORWARD)) {
//            return 1.0;
//        }
        else {
            return 0.0;
        }
    }
}