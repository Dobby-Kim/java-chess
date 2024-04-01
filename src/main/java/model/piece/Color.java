package model.piece;

public enum Color {
    BLACK, WHITE, NEUTRAL;

    public Color opponent() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }
}
