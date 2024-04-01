package model.piece.role;


import model.direction.ShiftPattern;
import model.piece.Color;

public final class Rook extends MultiShiftRole {
    private static final int SCORE = 5;

    private Rook(Color color) {
        super(color, ShiftPattern.ROOK_PATTERN);
    }

    public static Rook from(Color color) {
        return new Rook(color);
    }

    @Override
    public double score(boolean hasPawnInFile) {
        return SCORE;
    }
}
