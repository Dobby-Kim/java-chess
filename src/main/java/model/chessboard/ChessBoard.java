package model.chessboard;

import java.util.Map;
import model.chessboard.state.CurrentTurn;
import model.chessboard.state.DefaultState;
import model.piece.Color;
import model.piece.PieceHolder;
import model.position.Position;

public class ChessBoard {
    private DefaultState currentState;

    public ChessBoard(FenCommand fenCommand) {
        if (fenCommand.isInitial()) {
            this.currentState = new CurrentTurn(ChessBoardFactory.initialBoard(), Color.WHITE);
            return;
        }
        String savedFen = fenCommand.fen();
        this.currentState = new CurrentTurn(ChessBoardFactory.loadBoard(savedFen),
                ChessBoardFenConverter.colorFromFEN(savedFen));
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
        return this.currentState.getChessBoard();
    }

    public Color getCurrentColor() {
        return this.currentState.getCurrentColor();
    }
}
