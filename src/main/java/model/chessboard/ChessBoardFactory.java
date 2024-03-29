package model.chessboard;

import static model.piece.Color.BLACK;
import static model.piece.Color.WHITE;
import static util.File.*;
import static util.Rank.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.piece.Color;
import model.piece.PieceHolder;
import model.piece.role.Bishop;
import model.piece.role.King;
import model.piece.role.Knight;
import model.piece.role.Pawn;
import model.piece.role.Queen;
import model.piece.role.Role;
import model.piece.role.Rook;
import model.piece.role.Square;
import model.position.Position;

public class ChessBoardFactory {
    private static final Map<Position, PieceHolder> chessBoard = new HashMap<>();

    private ChessBoardFactory() {
    }

    public static Map<Position, PieceHolder> create() {
        initRookBishopKnight();
        initKingQueen();
        initPawn();
        initSquare();
        return chessBoard;
    }

    private static void initRookBishopKnight() {
        initWhiteRookBishopKnight();
        initBlackRookBishopKnight();
    }

    private static void initWhiteRookBishopKnight() {
        List<Role> whiteTypePieces = generateColoredPieces(WHITE);
        for (int file = A.value(); file <= C.value(); file++) {
            Role role = whiteTypePieces.get(file - 1);
            int mirrorFileIndex = 9 - file;
            chessBoard.put(Position.of(file, ONE.value()), new PieceHolder(role));
            chessBoard.put(Position.of(mirrorFileIndex, ONE.value()), new PieceHolder(role));
        }
    }

    private static void initBlackRookBishopKnight() {
        List<Role> blackTypePieces = generateColoredPieces(BLACK);
        for (int file = A.value(); file <= C.value(); file++) {
            Role role = blackTypePieces.get(file - 1);
            int mirrorFileIndex = 9 - file;
            chessBoard.put(Position.of(file, EIGHT.value()), new PieceHolder(role));
            chessBoard.put(Position.of(mirrorFileIndex, EIGHT.value()), new PieceHolder(role));
        }
    }

    private static List<Role> generateColoredPieces(Color color) {
        return List.of(Rook.from(color), Knight.from(color), Bishop.from(color));
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
