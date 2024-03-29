package model.chessboard;

import static model.piece.Color.BLACK;
import static model.piece.Color.WHITE;
import static util.File.*;
import static util.Rank.*;

import java.util.HashMap;
import java.util.Map;
import model.piece.PieceHolder;
import model.piece.role.Bishop;
import model.piece.role.King;
import model.piece.role.Knight;
import model.piece.role.Pawn;
import model.piece.role.Queen;
import model.piece.role.Rook;
import model.piece.role.Square;
import model.position.Position;

public class ChessBoardFactory {
    private static final Map<Position, PieceHolder> chessBoard = new HashMap<>();

    private ChessBoardFactory() {
    }

    public static Map<Position, PieceHolder> create() {
        initRook();
        initKnight();
        initBishop();
        initKingQueen();
        initPawn();
        initSquare();
        return chessBoard;
    }

    private static void initRook() {
        chessBoard.put(Position.of(A.value(), ONE.value()), new PieceHolder(Rook.from(WHITE)));
        chessBoard.put(Position.of(H.value(), ONE.value()), new PieceHolder(Rook.from(WHITE)));
        chessBoard.put(Position.of(A.value(), EIGHT.value()), new PieceHolder(Rook.from(BLACK)));
        chessBoard.put(Position.of(H.value(), EIGHT.value()), new PieceHolder(Rook.from(BLACK)));
    }

    private static void initKnight() {
        chessBoard.put(Position.of(B.value(), ONE.value()), new PieceHolder(Knight.from(WHITE)));
        chessBoard.put(Position.of(G.value(), ONE.value()), new PieceHolder(Knight.from(WHITE)));
        chessBoard.put(Position.of(B.value(), EIGHT.value()), new PieceHolder(Knight.from(BLACK)));
        chessBoard.put(Position.of(G.value(), EIGHT.value()), new PieceHolder(Knight.from(BLACK)));
    }

    private static void initBishop() {
        chessBoard.put(Position.of(C.value(), ONE.value()), new PieceHolder(Bishop.from(WHITE)));
        chessBoard.put(Position.of(F.value(), ONE.value()), new PieceHolder(Bishop.from(WHITE)));
        chessBoard.put(Position.of(C.value(), EIGHT.value()), new PieceHolder(Bishop.from(BLACK)));
        chessBoard.put(Position.of(F.value(), EIGHT.value()), new PieceHolder(Bishop.from(BLACK)));
    }

    private static void initKingQueen() {
        chessBoard.put(Position.of(D.value(), ONE.value()), new PieceHolder(Queen.from(WHITE)));
        chessBoard.put(Position.of(E.value(), ONE.value()), new PieceHolder(King.from(WHITE)));
        chessBoard.put(Position.of(D.value(), EIGHT.value()), new PieceHolder(Queen.from(BLACK)));
        chessBoard.put(Position.of(E.value(), EIGHT.value()), new PieceHolder(King.from(BLACK)));
    }

    private static void initPawn() {
        for (int file = A.value(); file <= H.value(); file++) {
            chessBoard.put(Position.of(file, TWO.value()), new PieceHolder(Pawn.from(WHITE)));
            chessBoard.put(Position.of(file, SEVEN.value()), new PieceHolder(Pawn.from(BLACK)));
        }
    }

    private static void initSquare() {
        for (int file = A.value(); file <= H.value(); file++) {
            chessBoard.put(Position.of(file, THREE.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(file, FOUR.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(file, FIVE.value()), new PieceHolder(new Square()));
            chessBoard.put(Position.of(file, SIX.value()), new PieceHolder(new Square()));
        }
    }
}
