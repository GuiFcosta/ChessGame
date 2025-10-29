package pt.isec.pa.chess.ui.root;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ModelLog;

public class RootLog extends VBox {
    private final ListView<String> logListView;
    private final Button clearLogsButton;

    public RootLog() {
        logListView = new ListView<>();
        clearLogsButton = new Button("Clear Logs");

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(logListView, clearLogsButton);
    }

    private void registerHandlers() {
        ModelLog.getInstance().addListener(evt -> {
            if ("logAdded".equals(evt.getPropertyName())) {
                logListView.getItems().add((String) evt.getNewValue());
            } else if ("logsCleared".equals(evt.getPropertyName())) {
                logListView.getItems().clear();
            }
        });

        clearLogsButton.setOnAction(e -> ModelLog.getInstance().clearLogs());

    }
    private void update() {
        logListView.getItems().clear();
        logListView.getItems().addAll(ModelLog.getInstance().getLogs());
    }
}
