package model.chessboard.state;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import model.piece.Color;
import model.piece.PieceHolder;
import model.position.Position;
import model.position.Route;

public class CurrentTurn extends DefaultState {
    public CurrentTurn(Map<Position, PieceHolder> chessBoard, Color currentColor) {
        super(chessBoard, currentColor);
    }

    @Override
    public DefaultState move(Position source, Position destination) {
        checkTurn(source);
        runMove(source, destination);
        if (isCheckedBy(currentColor)) {
            List<Route> attackRoutes = findAttackRoutes();
            return new Checked(chessBoard, currentColor.opponent(), attackRoutes);
        }
        return this;
    }

    private void checkTurn(Position sourcePosition) {
        if (!chessBoard.get(sourcePosition)
                .hasSameColor(this.currentColor)) {
            throw new IllegalArgumentException(currentColor.name() + " 진영의 기물을 움직여야 합니다.");
        }
    }

    protected void runMove(final Position sourcePosition, final Position destinationPosition) {
        PieceHolder sourcePieceHolder = chessBoard.get(sourcePosition);
        Route route = sourcePieceHolder.findRoute(sourcePosition, destinationPosition);
        if (isMoveOccurCheck(sourcePosition, route)) {
            throw new IllegalArgumentException("해당 위치는 체크이므로 움직일 수 없습니다.");
        }
        chessBoard.get(sourcePosition)
                .moveToDestination(pieceHoldersInRoute(route));
    }

    protected boolean isMoveOccurCheck(Position sourcePosition, Route route) {
        Map<Position, PieceHolder> chessBoardBackUp = getChessBoardBackUp();
        PieceHolder sourcePieceHolder = chessBoard.get(sourcePosition);
        sourcePieceHolder.moveToDestination(pieceHoldersInRoute(route));
        if (isCheckedBy(currentColor.opponent())) {
            chessBoard = chessBoardBackUp;
            return true;
        }
        chessBoard = chessBoardBackUp;
        return false;
    }

    private Map<Position, PieceHolder> getChessBoardBackUp() {
        return chessBoard.entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue()
                        .copy()));
    }

    @Override
    protected boolean isCheckedBy(Color attackingColor) {
        Position kingPosition = findKingPosition(attackingColor.opponent());
        Set<Position> attackingPosition = findPositionsByColor(attackingColor);
        return attackingPosition.stream()
                .anyMatch(positionPieceHolderEntry -> isReachablePosition(positionPieceHolderEntry, kingPosition));
    }

    protected Set<Position> findPositionsByColor(Color color) {
        return chessBoard.keySet()
                .stream()
                .filter(position -> chessBoard.get(position)
                        .hasSameColor(color))
                .collect(Collectors.toSet());
    }

    private Position findKingPosition(Color targetColor) {
        return findPositionsByColor(targetColor).stream()
                .filter(position -> chessBoard.get(position)
                        .isKing())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("찾고자 하는 King이 존재하지 않습니다."));
    }

    protected boolean isReachablePosition(Position sourcePosition, Position targetPosition) {
        PieceHolder currentPieceHolder = chessBoard.get(sourcePosition);
        try {
            Route routeToTarget = currentPieceHolder.findRoute(sourcePosition, targetPosition);
            return currentPieceHolder.canMoveThroughRoute(pieceHoldersInRoute(routeToTarget));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private List<PieceHolder> pieceHoldersInRoute(Route route) {
        return route.getPositions()
                .stream()
                .map(chessBoard::get)
                .toList();
    }

    protected List<Route> findAttackRoutes() {
        Position checkedKingPosition = findKingPosition(currentColor.opponent());
        return findPositionsByColor(currentColor).stream()
                .filter(attackingPosition -> isReachablePosition(attackingPosition, checkedKingPosition))
                .map(attackingPosition -> chessBoard.get(attackingPosition)
                        .findRoute(attackingPosition, checkedKingPosition)
                        .reverseRouteTowardSource())
                .toList();
    }

    @Override
    public final Color winner() {
        double whiteScore = score(Color.WHITE);
        double blackScore = score(Color.BLACK);
        if (whiteScore > blackScore) {
            return Color.WHITE;
        }
        if (whiteScore < blackScore) {
            return Color.BLACK;
        }
        return Color.NEUTRAL;
    }

    @Override
    public boolean isFinish() {
        return false;
    }

    @Override
    public DefaultState nextState() {
        return new CurrentTurn(chessBoard, currentColor.opponent());
    }
}
