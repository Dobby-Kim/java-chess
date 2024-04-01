package model.chessboard.state;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import model.piece.Color;
import model.piece.PieceHolder;
import model.piece.role.King;
import model.position.Position;
import model.position.Route;

public final class Checked extends CurrentTurn {
    private final List<Position> checkedPositions;

    protected Checked(Map<Position, PieceHolder> chessBoard, Color currentColor, List<Route> attackRoutes) {
        super(chessBoard, currentColor);
        this.checkedPositions = attackedPositionsInRoutes(attackRoutes);
    }

    private List<Position> attackedPositionsInRoutes(List<Route> attackRoutes) {
        return attackRoutes.stream()
                .map(Route::getPositions)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public DefaultState nextState() {
        if (canAvoidCheck()) {
            return this;
        }
        return new CheckMate(chessBoard, currentColor);
    }

    private boolean canAvoidCheck() {
        return findPieceHoldersByColor(currentColor).stream()
                .anyMatch(this::canBlockAttack);
    }

    private boolean canBlockAttack(Entry<Position, PieceHolder> entry) {
        Position currentPosition = entry.getKey();
        PieceHolder currentPieceHolder = entry.getValue();
        boolean canKingEscape = false;
        if (currentPieceHolder.isKing()) {
            canKingEscape = canKingEscapeCurrentPosition(entry);
        }
        return canKingEscape || checkedPositions.stream()
                .filter(checkedPosition -> isReachablePosition(entry, checkedPosition) && !currentPieceHolder.isKing())
                .map(destination -> currentPieceHolder.findRoute(currentPosition, destination))
                .anyMatch(route -> canMove(currentPieceHolder, route));
    }

    private boolean canKingEscapeCurrentPosition(Entry<Position, PieceHolder> entry) {
        King currentKing = King.from(currentColor);
        Set<Route> availableRoutes = currentKing.findAllAvailableRoutes(entry.getKey());
        return availableRoutes.stream()
                .filter(Route::isValidRoute)
                .filter(escapeRoute -> isReachablePosition(entry, escapeRoute.getPositions()
                        .get(0)))
                .anyMatch(route -> canMove(entry.getValue(), route));
    }

    private boolean canMove(PieceHolder defenderPieceHolder, Route avaliableRoute) {
        try {
            runMove(defenderPieceHolder, avaliableRoute);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
