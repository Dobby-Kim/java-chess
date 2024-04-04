package model.piece.role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.direction.Direction;
import model.direction.ShiftPattern;
import model.piece.Color;
import model.position.Position;
import model.position.Route;

public abstract class Role {
    protected static final double SCORE = 0;

    protected final Color color;
    private final ShiftPattern shiftPattern;

    protected Role(Color color, ShiftPattern shiftPattern) {
        this.color = color;
        this.shiftPattern = shiftPattern;
    }

    public boolean isSameColor(Color color) {
        return this.color == color;
    }

    public Route findDirectRoute(Position source, Position destination) {
        return findAllPossibleRoutes(source).stream()
                .filter(route -> route.contains(destination))
                .map(route -> route.directRouteTo(destination))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 기물이 이동할 수 없는 좌표입니다"));
    }

    protected Set<Route> findAllPossibleRoutes(Position source) {
        Set<Route> possibleRoutes = new HashSet<>();
        List<Direction> directions = shiftPattern.directions();
        for (Direction direction : directions) {
            possibleRoutes.add(findRouteByDirection(direction, source));
        }
        return possibleRoutes;
    }

    protected abstract Route findRouteByDirection(Direction direction, Position source);

    public boolean canCapture(Role destinationRole) {
        return destinationRole.color != this.color;
    }

    public boolean isOccupied() {
        return true;
    }

    public boolean isKing() {
        return false;
    }

    public abstract double score(boolean hasPawnInFile);

    public Color getColor() {
        return color;
    }
}
