package view.dto;

import model.piece.PieceHolder;
import model.position.Position;

public record PieceInfo(
        Position position,
        PieceHolder pieceHolder) {

    public int file() {
        return position().file();
    }

    public int rank() {
        return position.rank();
    }

    public String role() {
        return pieceHolder.getClass()
                .getSimpleName();
    }

    public String color() {
        return pieceHolder.getColor()
                .name();
    }
}
