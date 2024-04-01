package controller;

import static util.InputRetryHelper.inputRetryHelper;
import static view.OutputView.printChessBoard;
import static view.OutputView.printInitialGamePrompt;

import java.util.List;
import model.chessboard.ChessBoard;
import model.chessboard.ChessBoardFenConverter;
import model.chessboard.FenCommand;
import model.chessboard.Score;
import model.position.Position;
import repository.UserDao;
import view.GameCommand;
import view.InputView;
import view.OutputView;
import view.dto.InfoMapper;
import view.dto.PieceInfo;

public class Controller {
    private final UserDao userDao = new UserDao();

    public void execute() {
        GameCommand gameCommand = executeInitial();
        userDao.initializeDatabase();
        ChessBoard chessBoard = new ChessBoard(new FenCommand(userDao.loadFenValues(), userDao.isInitialGame()));
        while (!gameCommand.isEnd() && !chessBoard.isFinish()) {
            List<PieceInfo> pieceInfos = InfoMapper.toPieceInfoMapper(chessBoard);
            printChessBoard(pieceInfos);
            gameCommand = inputRetryHelper(() -> runGame(chessBoard));
            showCurrentStatus(gameCommand, chessBoard);
        }
        showFianalStatus(chessBoard);
    }

    private GameCommand executeInitial() {
        printInitialGamePrompt();
        return inputRetryHelper(InputView::inputInitialGameCommand);
    }

    private void showFianalStatus(ChessBoard chessBoard) {
        List<PieceInfo> pieceInfos = InfoMapper.toPieceInfoMapper(chessBoard);
        printChessBoard(pieceInfos);
        Score currentScore = chessBoard.aggregateScore();
        OutputView.printScore(currentScore, chessBoard.isFinish());
        userDao.updateFen(ChessBoardFenConverter.toFEN(chessBoard.getChessBoard()));
    }

    private void showCurrentStatus(GameCommand gameCommand, ChessBoard chessBoard) {
        if (gameCommand == GameCommand.STATUS) {
            Score currentScore = chessBoard.aggregateScore();
            OutputView.printScore(currentScore, false);
        }
    }

    private GameCommand runGame(ChessBoard chessBoard) {
        List<String> inputCommand = InputView.parseCommand();
        GameCommand gameCommand = inputRetryHelper(() -> GameCommand.from(inputCommand));
        if (gameCommand == GameCommand.MOVE) {
            executeMoveCommand(chessBoard, inputCommand);
        }
        return gameCommand;
    }

    private void executeMoveCommand(ChessBoard chessBoard, List<String> inputCommand) {
        Position source = generateSourcePosition(inputCommand);
        Position destination = generateDestinationPosition(inputCommand);
        chessBoard.move(source, destination);
    }

    private Position generateSourcePosition(List<String> inputCommand) {
        int file = GameCommand.toSourceFileValue(inputCommand);
        int rank = GameCommand.toSourceRankValue(inputCommand);
        return Position.of(file, rank);
    }

    private Position generateDestinationPosition(List<String> inputCommand) {
        int file = GameCommand.toDestinationFileValue(inputCommand);
        int rank = GameCommand.toDestinationRankValue(inputCommand);
        return Position.of(file, rank);
    }
}
