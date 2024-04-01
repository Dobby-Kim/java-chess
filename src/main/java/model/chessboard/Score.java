package model.chessboard;

import model.piece.Color;

public record Score(double whiteScore, double blackScore, Color currrentWinner) {

    public String winner() {
        if (currrentWinner == Color.WHITE) {
            return "백";
        }
        if (currrentWinner == Color.BLACK) {
            return "흑";
        }
        return "무승부";
    }
}
