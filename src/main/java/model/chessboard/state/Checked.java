package model.chessboard.state;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import model.piece.Color;
import model.piece.PieceHolder;
import model.piece.role.King;
import model.position.Position;
import model.position.Route;

public final class Checked extends CurrentTurn {
    private final List<Position> checkedPositions;

    Checked(Map<Position, PieceHolder> chessBoard, Color currentColor, List<Route> attackRoutes) {
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
        return findPositionsByColor(currentColor).stream()
                .anyMatch(this::canBlockAttack);
    }

    private boolean canBlockAttack(Position defensePosition) {
        PieceHolder currentPieceHolder = chessBoard.get(defensePosition);
        if (currentPieceHolder.isKing()) {
            return canKingEscapeCurrentPosition(defensePosition);
        }
        return checkedPositions.stream()
                .filter(checkedPosition -> isReachablePosition(defensePosition, checkedPosition))
                .map(checkedPosition -> currentPieceHolder.findRoute(defensePosition, checkedPosition))
                .anyMatch(blockRoute -> !isMoveOccurCheck(defensePosition, blockRoute));
    }

    private boolean canKingEscapeCurrentPosition(Position kingPosition) {
        King currentKing = King.from(currentColor);
        return currentKing.findAllAvailableRoutes(kingPosition)
                .stream()
                .filter(Route::isValidRoute)
                .noneMatch(route -> isMoveOccurCheck(kingPosition, route));
    }
}
