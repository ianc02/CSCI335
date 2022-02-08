package checkers.core;

import java.awt.*;

public enum PlayerColor {
    BLACK, RED;

    public PlayerColor opponent() {
        return this == BLACK ? RED : BLACK;
    }

    public Color color() {
        return this == BLACK ? new Color(40, 40, 40) : new Color(128, 0, 0);
    }
}
