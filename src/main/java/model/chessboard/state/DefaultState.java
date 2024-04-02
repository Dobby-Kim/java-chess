package model.chessboard.state;

import java.util.List;
import java.util.Map;
import model.piece.Color;
import model.piece.PieceHolder;
import model.piece.role.Pawn;
import model.position.Position;
import util.File;

public abstract class DefaultState {
    protected Map<Position, PieceHolder> chessBoard;
    protected final Color currentColor;

    protected DefaultState(Map<Position, PieceHolder> chessBoard, Color currentColor) {
        this.chessBoard = chessBoard;
        this.currentColor = currentColor;
    }

    public abstract DefaultState move(Position source, Position destination);

    protected abstract boolean isCheckedBy(Color targetColor);

    public abstract DefaultState nextState();

    public abstract boolean isFinish();

    public final double score(Color targetColor) {
        double totalScore = 0D;
        for (int file = File.A.value(); file <= File.H.value(); file++) {
            totalScore += calculateTotalScoreByColor(targetColor, file);
        }
        return totalScore;
    }

    private double calculateTotalScoreByColor(Color color, int file) {
        List<PieceHolder> pieceHoldersInFile = Position.inFile(file)
                .stream()
                .map(chessBoard::get)
                .filter(pieceHolder -> pieceHolder.hasSameColor(color))
                .toList();
        long pawnNumberInFile = pieceHoldersInFile.stream()
                .filter(pieceHolder -> pieceHolder.getRole() instanceof Pawn)
                .count();
        return sumScoreInFile(pieceHoldersInFile, pawnNumberInFile >= 2);
    }

    private double sumScoreInFile(List<PieceHolder> pieceHoldersInFile, boolean hasPawnInFile) {
        double accumulate = 0D;
        for (PieceHolder pieceHolder : pieceHoldersInFile) {
            accumulate += pieceHolder.score(hasPawnInFile);
        }
        return accumulate;
    }

    public abstract Color winner();

    public Map<Position, PieceHolder> getChessBoard() {
        return Map.copyOf(chessBoard);
    }

    public Color getCurrentColor() {
        return currentColor;
    }
}
