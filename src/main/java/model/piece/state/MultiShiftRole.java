package model.piece.state;

import java.util.ArrayList;
import java.util.List;
import model.direction.Direction;
import model.direction.ShiftPattern;
import model.piece.Color;
import model.position.Position;
import model.position.Route;

public abstract class MultiShiftRole extends Role {
    protected MultiShiftRole(Color color, ShiftPattern shiftPattern) {
        super(color, shiftPattern);
    }

    @Override
    protected Route findMovingPatternRoute(Direction direction, Position movedPosition) {
        List<Position> sequentialPositions = new ArrayList<>();
        while (movedPosition.isAvailablePosition(direction)) {
            movedPosition = movedPosition.getNextPosition(direction);
            sequentialPositions.add(movedPosition);
        }
        return new Route(direction, sequentialPositions);
    }
}
