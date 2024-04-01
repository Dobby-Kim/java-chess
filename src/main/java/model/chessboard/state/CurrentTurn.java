package model.chessboard.state;

import java.util.HashMap;
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
        PieceHolder sourcePieceHolder = chessBoard.get(source);
        checkTurn(sourcePieceHolder);
        Route route = sourcePieceHolder.findRoute(source, destination);
        runMove(sourcePieceHolder, route);
        if (isCheckedBy(currentColor)) {
            List<Route> attackRoutes = findAttackRoutes();
            return new Checked(chessBoard, currentColor.opponent(), attackRoutes);
        }
        return this;
    }

    protected void runMove(PieceHolder sourcePieceHolder, Route route) {
        Map<Position, PieceHolder> chessBoardBackUp = new HashMap<>(chessBoard);
        sourcePieceHolder.progressMoveToDestination(pieceHoldersInRoute(route));
        if (isCheckedBy(currentColor.opponent())) {
            chessBoard = chessBoardBackUp;
            throw new IllegalArgumentException("해당 위치는 체크이므로 움직일 수 없습니다.");
        }
    }

    private void checkTurn(PieceHolder selectedPieceHolder) {
        if (!selectedPieceHolder.hasSameColor(this.currentColor)) {
            throw new IllegalArgumentException(currentColor.name() + " 진영의 기물을 움직여야 합니다.");
        }
    }

    @Override
    protected boolean isCheckedBy(Color attackingColor) {
        Position kingPosition = findKingPosition(attackingColor.opponent());
        Set<Entry<Position, PieceHolder>> targetColorPieces = findPieceHoldersByColor(attackingColor);
        return targetColorPieces.stream()
                .anyMatch(positionPieceHolderEntry -> isReachablePosition(positionPieceHolderEntry, kingPosition));
    }

    protected Set<Entry<Position, PieceHolder>> findPieceHoldersByColor(Color color) {
        return chessBoard.entrySet()
                .stream()
                .filter(positionPieceHolderEntry -> positionPieceHolderEntry.getValue()
                        .hasSameColor(color))
                .collect(Collectors.toSet());
    }

    protected Position findKingPosition(Color targetColor) {
        return findPieceHoldersByColor(targetColor).stream()
                .filter(positionPieceHolderEntry -> positionPieceHolderEntry.getValue()
                        .isKing())
                .findFirst()
                .map(Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("찾고자 하는 King이 존재하지 않습니다."));
    }

    protected boolean isReachablePosition(Entry<Position, PieceHolder> positionPieceEntry, Position targetPosition) {
        Position currentPosition = positionPieceEntry.getKey();
        PieceHolder currentPieceHolder = positionPieceEntry.getValue();
        try {
            Route routeToTarget = currentPieceHolder.findRoute(currentPosition, targetPosition);
            return currentPieceHolder.checkPieceHoldersOnMovingRoute(pieceHoldersInRoute(routeToTarget));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    protected List<PieceHolder> pieceHoldersInRoute(Route route) {
        return route.getPositions()
                .stream()
                .map(chessBoard::get)
                .toList();
    }

    protected List<Route> findAttackRoutes() {
        Position checkedKingPosition = findKingPosition(currentColor.opponent());
        return findPieceHoldersByColor(currentColor).stream()
                .filter(pieceHolder -> isReachablePosition(pieceHolder, checkedKingPosition))
                .map(attackingPieceHolder -> attackingPieceHolder.getValue()
                        .findRoute(attackingPieceHolder.getKey(), checkedKingPosition)
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
