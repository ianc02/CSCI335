package robosim.ai;
public enum closefar {
    CLOSE, FAR, DIRTCLOSE, DIRTHIT;
    public int getIndex() {
        for (int i = 0; i < closefar.values().length; i++) {
            if (closefar.values()[i].equals(this)) {
                return i;
            }
        }
        throw new IllegalStateException("This should never happen");
    }
}