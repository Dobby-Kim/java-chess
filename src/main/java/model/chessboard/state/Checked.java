package model.chessboard.state;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.piece.Color;
import model.piece.PieceHolder;
import model.piece.role.King;
import model.position.Position;
import model.position.Route;

public final class Checked extends CurrentTurn {
    private final List<Position> checkedPositions;

    protected Checked(Map<Position, PieceHolder> chessBoard, Color currentColor, List<Route> attackRoutes) {
        super(chessBoard, currentColor);
        this.checkedPositions = positionsInRoutes(attackRoutes);
    }

    private List<Position> positionsInRoutes(List<Route> attackRoutes) {
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
        if (currentPieceHolder.isKing()) {
            return canKingEscapeCurrentPosition(entry);
        }
        return checkedPositions.stream()
                .filter(checkedPosition -> isReachablePosition(entry, checkedPosition) && !currentPieceHolder.isKing())
                .map(destination -> currentPieceHolder.findRoute(currentPosition, destination))
                .anyMatch(route -> !isCheckedBy(currentColor.opponent()));
    }

    private boolean canKingEscapeCurrentPosition(Entry<Position, PieceHolder> entry) {
        King currentKing = King.from(currentColor);
        List<Position> availableKingPositions = currentKing.findAllAvailableRoutes(entry.getKey())
                .stream()
                .map(Route::getPositions)
                .flatMap(Collection::stream)
                .toList();
        return availableKingPositions.stream()
                .filter(escapeRoute -> isReachablePosition(entry, escapeRoute))
                .anyMatch(route -> !isCheckedBy(currentColor.opponent()));
    }
}
