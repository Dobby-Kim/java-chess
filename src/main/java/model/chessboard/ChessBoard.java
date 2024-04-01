package model.chessboard;

import java.util.Map;
import model.chessboard.state.CurrentTurn;
import model.chessboard.state.DefaultState;
import model.piece.Color;
import model.piece.PieceHolder;
import model.position.Position;

public class ChessBoard {
    private DefaultState currentState;

    public ChessBoard() {
        currentState = new CurrentTurn(ChessBoardFactory.create(), Color.WHITE);
    }

    public void move(Position source, Position destination) {
        this.currentState = currentState.move(source, destination);
        this.update();
    }

    private void update() {
        this.currentState = currentState.nextState();
    }

    public boolean isFinish() {
        return currentState.isFinish();
    }

    public Score aggregateScore() {
        double whiteScore = this.currentState.score(Color.WHITE);
        double blackScore = this.currentState.score(Color.BLACK);
        return new Score(whiteScore, blackScore, currentState.winner());
    }

    public Map<Position, PieceHolder> getChessBoard() {
        return currentState.getChessBoard();
    }
}
