package model.piece.role;

import java.util.Set;
import model.direction.ShiftPattern;
import model.piece.Color;
import model.position.Position;
import model.position.Route;

public final class King extends SingleShiftRole {
    private static final double SCORE = 0;

    private King(Color color) {
        super(color, ShiftPattern.KING_PATTERN);
    }

    public static King from(Color color) {
        return new King(color);
    }

    public Set<Route> findAllAvailableRoutes(Position kingPosition) {
        return findAllPossibleRoutes(kingPosition);
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public double score(boolean hasPawnInFile) {
        return SCORE;
    }
}
