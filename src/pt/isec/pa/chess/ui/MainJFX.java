package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.root.RootLog;
import pt.isec.pa.chess.ui.root.RootPane;

import java.util.Objects;

public class MainJFX extends Application {
    ChessGameManager gameManager = new ChessGameManager();

    @Override
    public void start(Stage stage) {
        createStage(stage);
        Stage logStage = new Stage();
        createLogStage(logStage, stage.getX() + stage.getWidth(), stage.getY());

        stage.setOnCloseRequest(windowEvent -> logStage.close());
        logStage.setOnCloseRequest(windowEvent -> stage.close());
    }

    private void createStage(Stage stage) {
        RootPane root = new RootPane(gameManager);
        Scene scene = new Scene(root, 680, 720);

        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/images/pieces/pawnW.png"))));
        stage.setTitle("Chess Game");
        stage.show();
    }

    private void createLogStage(Stage stage, double x, double y) {
        RootLog root = new RootLog();
        Scene logScene = new Scene(root, 300, 300);

        stage.setScene(logScene);
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }
}