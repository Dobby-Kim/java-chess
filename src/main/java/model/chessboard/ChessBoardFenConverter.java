package model.chessboard;

import static model.piece.Color.BLACK;
import static model.piece.Color.WHITE;
import static util.Rank.EIGHT;
import static util.Rank.FIVE;
import static util.Rank.FOUR;
import static util.Rank.ONE;
import static util.Rank.SEVEN;
import static util.Rank.SIX;
import static util.Rank.THREE;
import static util.Rank.TWO;

import java.util.HashMap;
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
import view.ChessSymbol;

public class ChessBoardFenConverter {

    private ChessBoardFenConverter() {
    }

    public static String toFEN(Map<Position, PieceHolder> board) {
        StringBuilder fen = new StringBuilder();
        for (int rank = 8; rank >= 1; rank--) {
            convertRank(board, rank, fen);
        }
        return fen.toString();
    }

    public static String toFEN(ChessBoard chessBoard) {
        Map<Position, PieceHolder> board = chessBoard.getChessBoard();
        StringBuilder fen = new StringBuilder();
        for (int rank = 8; rank >= 1; rank--) {
            convertRank(board, rank, fen);
        }
        Color currentTurn = chessBoard.getCurrentColor();
        if (currentTurn == WHITE) {
            return fen.append(" w").toString();
        }
        return fen.append(" b").toString();
    }

    private static void convertRank(Map<Position, PieceHolder> chessBoard, int rank, StringBuilder fen) {
        int emptyCount = 0;
        for (int file = 1; file <= 8; file++) {
            Position position = Position.of(file, rank);
            PieceHolder pieceHolder = chessBoard.get(position);
            emptyCount = appendPieceOrEmpty(fen, pieceHolder, emptyCount);
        }
        if (emptyCount > 0) {
            fen.append(emptyCount);
        }
        if (rank > 1) {
            fen.append('/');
        }
    }

    private static int appendPieceOrEmpty(StringBuilder fen, PieceHolder pieceHolder, int emptyCount) {
        if (!pieceHolder.getRole()
                .isOccupied()) {
            return emptyCount + 1;
        }
        if (emptyCount > 0) {
            fen.append(emptyCount);
        }
        fen.append(getFenNotation(pieceHolder));
        return 0;
    }

    private static char getFenNotation(PieceHolder pieceHolder) {
        String role = role(pieceHolder);
        char symbol = ChessSymbol.getSymbolForRole(role);
        if (pieceHolder.hasSameColor(WHITE)) {
            return Character.toUpperCase(symbol);
        }
        return Character.toLowerCase(symbol);
    }

    private static String role(PieceHolder pieceHolder) {
        return pieceHolder.getRole()
                .getClass()
                .getSimpleName();
    }

    public static Map<Position, PieceHolder> fromFEN(String fen) {
        Map<Position, PieceHolder> chessBoard = new HashMap<>();
        cleanBoard(chessBoard);
        String[] rows = fen.split(" ")[0].split("/");
        for (int rank = 1; rank <= rows.length; rank++) {
            parseRow(chessBoard, rows[rank - 1], rank);
        }
        return chessBoard;
    }

    private static void parseRow(Map<Position, PieceHolder> chessBoard, String row, int rank) {
        int file = 1;
        for (char piece : row.toCharArray()) {
            if (Character.isDigit(piece)) {
                file += Character.getNumericValue(piece);
                continue;
            }
            Position position = Position.of(file, 9 - rank);
            chessBoard.put(position, createPieceHolder(piece));
            file++;
        }
    }

    private static PieceHolder createPieceHolder(char piece) {
        if (Character.isLowerCase(piece)) {
            return new PieceHolder(getBlackPiece(piece));
        }
        return new PieceHolder(getWhitePiece(Character.toLowerCase(piece)));
    }

    private static Role getBlackPiece(char piece) {
        return getRole(piece, BLACK);
    }

    private static Role getWhitePiece(char piece) {
        return getRole(piece, WHITE);
    }

    private static Role getRole(char piece, Color color) {
        return switch (piece) {
            case 'p' -> Pawn.from(color);
            case 'r' -> Rook.from(color);
            case 'n' -> Knight.from(color);
            case 'b' -> Bishop.from(color);
            case 'q' -> Queen.from(color);
            case 'k' -> King.from(color);
            default -> new Square();
        };
    }

    public static Color colorFromFEN(String fen) {
        String turn = fen.split(" ")[1];
        if ("w".equals(turn)) {
            return WHITE;
        }
        return BLACK;
    }

    private static void cleanBoard(Map<Position, PieceHolder> chessBoard) {
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
