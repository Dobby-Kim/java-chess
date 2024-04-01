package model.piece.role;

import model.direction.ShiftPattern;
import model.piece.Color;

public final class Bishop extends MultiShiftRole {
    private static final int SCORE = 3;

    private Bishop(Color color) {
        super(color, ShiftPattern.BISHOP_PATTERN);
    }

    public static Bishop from(Color color) {
        return new Bishop(color);
    }

    @Override
    public double score(boolean hasPawnInFile) {
        return SCORE;
    }
}
