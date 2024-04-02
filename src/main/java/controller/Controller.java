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
import repository.ChessDao;
import view.GameCommand;
import view.InputView;
import view.OutputView;
import view.dto.InfoMapper;
import view.dto.PieceInfo;

public class Controller {
    private final ChessDao chessDao = new ChessDao();

    public void execute() {
        GameCommand gameCommand = executeInitial();
        chessDao.ensureDatabaseInitialized();
        ChessBoard chessBoard = new ChessBoard(new FenCommand(chessDao.loadFenValue(), chessDao.isInitialGame()));
        while (!gameCommand.isEnd() && !chessBoard.isFinish()) {
            List<PieceInfo> pieceInfos = InfoMapper.toPieceInfoMapper(chessBoard);
            printChessBoard(pieceInfos);
            gameCommand = inputRetryHelper(() -> runGame(chessBoard));
            showCurrentStatus(gameCommand, chessBoard);
        }
        processFinalStatus(chessBoard);
    }

    private GameCommand executeInitial() {
        printInitialGamePrompt();
        if (!chessDao.isInitialGame()) {
            OutputView.printSavedGameExistPrompt();
        }
        return inputRetryHelper(InputView::inputInitialGameCommand);
    }

    private void processFinalStatus(ChessBoard chessBoard) {
        List<PieceInfo> pieceInfos = InfoMapper.toPieceInfoMapper(chessBoard);
        printChessBoard(pieceInfos);
        Score currentScore = chessBoard.aggregateScore();
        OutputView.printScore(currentScore, chessBoard.isFinish());
        if (chessBoard.isFinish()) {
            chessDao.initFen();
            return;
        }
        chessDao.updateFen(ChessBoardFenConverter.toFEN(chessBoard));
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
