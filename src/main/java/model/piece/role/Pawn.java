package model.piece.role;

import static model.direction.Direction.N;
import static model.direction.Direction.S;

import java.util.ArrayList;
import java.util.List;
import model.direction.Direction;
import model.direction.ShiftPattern;
import model.piece.Color;
import model.position.Position;
import model.position.Route;

public final class Pawn extends Role {
    private static final int INITIAL_WHITE_PAWN_RANK = 2;
    private static final int INITIAL_BLACK_PAWN_RANK = 7;
    private static final double SCORE = 1;

    private boolean isTryToTake;

    private Pawn(Color color, ShiftPattern shiftPattern) {
        super(color, shiftPattern);
        isTryToTake = false;
    }

    public static Pawn from(Color color) {
        if (color == Color.WHITE) {
            return new Pawn(color, ShiftPattern.WHITE_PAWN_PATTERN);
        }
        return new Pawn(color, ShiftPattern.BLACK_PAWN_PATTERN);
    }

    @Override
    public Route findDirectRoute(Position source, Position destination) {
        Route directRoute = findAllPossibleRoutes(source).stream()
                .filter(route -> route.contains(destination))
                .map(route -> route.directRouteTo(destination))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 기물이 이동할 수 없는 좌표입니다"));
        updatePawnAction(directRoute.getDirection());
        return directRoute;
    }

    private void updatePawnAction(Direction direction) {
        if (direction == N || direction == S) {
            isTryToTake = false;
            return;
        }
        isTryToTake = true;
    }

    @Override
    protected Route findRouteByDirection(Direction direction, Position source) {
        List<Position> sequentialPositions = new ArrayList<>();
        source = doubleShift(direction, source, sequentialPositions);
        singleShift(direction, source, sequentialPositions);
        return new Route(direction, sequentialPositions);
    }

    private void singleShift(Direction direction, Position source, List<Position> sequentialPositions) {
        if (source.hasAvailableNextPositionToDirection(direction)) {
            source = source.getNextPosition(direction);
            sequentialPositions.add(source);
        }
    }

    private Position doubleShift(Direction direction, Position source, List<Position> sequentialPositions) {
        if ((source.rank() == INITIAL_WHITE_PAWN_RANK && direction == N) || (source.rank() == INITIAL_BLACK_PAWN_RANK
                && direction == S)) {
            source = source.getNextPosition(direction);
            sequentialPositions.add(source);
        }
        return source;
    }

    @Override
    public boolean canCapture(Role destinationRole) {
        if (destinationRole.color != this.color.opponent() && isTryToTake != destinationRole.isOccupied()) {
            return false;
        }
        isTryToTake = false;
        return true;
    }

    @Override
    public double score(boolean hasPawnInFile) {
        if (hasPawnInFile) {
            return SCORE / 2;
        }
        return SCORE;
    }
}
