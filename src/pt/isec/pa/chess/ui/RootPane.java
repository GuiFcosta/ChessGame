package pt.isec.pa.chess.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.chess.model.ChessGameManager;

public class RootPane extends BorderPane {
    ChessGameManager gameManager;
    ChessBoardCanvas chessBoardCanvas;
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
