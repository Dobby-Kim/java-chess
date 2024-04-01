package controller;

import static model.piece.Color.BLACK;
import static model.piece.Color.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.File.C;
import static util.File.D;
import static util.File.E;
import static util.File.F;
import static util.Rank.EIGHT;
import static util.Rank.FIVE;
import static util.Rank.FOUR;
import static util.Rank.ONE;
import static util.Rank.SEVEN;
import static util.Rank.SIX;
import static util.Rank.THREE;
import static util.Rank.TWO;

import java.util.Map;
import model.chessboard.ChessBoardFactory;
import model.chessboard.ChessBoardFenConverter;
import model.piece.PieceHolder;
import model.piece.role.Bishop;
import model.piece.role.Knight;
import model.piece.role.Pawn;
import model.piece.role.Square;
import model.position.Position;
import org.junit.jupiter.api.Test;

class ChessBoardFenConverterTest {
    private static final Map<Position, PieceHolder> chessBoard = ChessBoardFactory.initialBoard();

    @Test
    void toFen_() {
        String fen = ChessBoardFenConverter.toFEN(chessBoard);
        String expectedFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        assertEquals(expectedFen, fen);
    }

    @Test
    void toFen() {
        cleanBoard();
        chessBoard.put(Position.of(D.value(), 5), new PieceHolder(Pawn.from(BLACK)));
        chessBoard.put(Position.of(E.value(), 4), new PieceHolder(Pawn.from(WHITE)));
        chessBoard.put(Position.of(C.value(), 4), new PieceHolder(Knight.from(WHITE)));
        chessBoard.put(Position.of(F.value(), 5), new PieceHolder(Bishop.from(BLACK)));
        String fen = ChessBoardFenConverter.toFEN(chessBoard);

        String expectedFen = "8/8/8/3p1b2/2N1P3/8/8/8";

        assertEquals(expectedFen, fen);
    }


    private void cleanBoard() {
        for (int i = 1; i <= 8; i++) {
            chessBoard.put(Position.of(i, ONE.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, TWO.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, THREE.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, FOUR.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, FIVE.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, SIX.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, SEVEN.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(i, EIGHT.value()), new PieceHolder(new Square()));
        }
    }
}
