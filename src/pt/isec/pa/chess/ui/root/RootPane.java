package pt.isec.pa.chess.ui.root;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.board.ChessMenuBar;
import pt.isec.pa.chess.ui.audio.AudioPlayer;
import pt.isec.pa.chess.ui.board.ChessBoardCanvas;

public class RootPane extends BorderPane {
    ChessGameManager gameManager;
    public ChessBoardCanvas chessBoardCanvas;
    ChessMenuBar menu;
    StackPane boardPane;
    AudioPlayer audioPlayer;

    public RootPane(ChessGameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.resetGame();

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #eeeeee;");

        chessBoardCanvas = new ChessBoardCanvas(gameManager);
        menu = new ChessMenuBar(gameManager);
        boardPane = new StackPane(chessBoardCanvas);
        audioPlayer = new AudioPlayer(gameManager, menu);

        this.setTop(menu);
        this.setCenter(boardPane);
    }

    private void registerHandlers() {
        boardPane.widthProperty().addListener(observable -> chessBoardCanvas.resizeBoard(getWidth(), getHeight() - menu.getHeight()));
        boardPane.heightProperty().addListener(observable -> chessBoardCanvas.resizeBoard(getWidth(), getHeight() - menu.getHeight()));
    }

    private void update() {
    }
}
