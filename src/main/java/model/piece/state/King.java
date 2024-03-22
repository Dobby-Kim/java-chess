package model.piece.state;

import model.direction.ShiftPattern;
import model.piece.Color;

public final class King extends SingleShiftRole {

    private King(Color color) {
        super(color, ShiftPattern.KING_PATTERN);
    }

    public static King from(Color color) {
        return new King(color);
    }
}
