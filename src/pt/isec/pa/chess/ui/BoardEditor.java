package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.pieces.PieceType;

public class BoardEditor {
    private ChessGameManager gameManager;
    private Stage editorStage;
    private BoardEditorCanvas boardCanvas;
    private String currentPieceType = "P"; // Default to Pawn
    private boolean isWhitePiece = true; // Default to white
    private VBox pieceSelector;

    public BoardEditor(ChessGameManager gameManager) {
        this.gameManager = gameManager;
        createEditorWindow();
    }

    private void createEditorWindow() {
        editorStage = new Stage();
        editorStage.setTitle("Board Editor");
        editorStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();

        // Create the board canvas for editing
        boardCanvas = new BoardEditorCanvas(gameManager, this);
        root.setCenter(boardCanvas);

        // Create controls panel
        VBox controlsPanel = createControlsPanel();
        root.setLeft(controlsPanel);

        Scene scene = new Scene(root, 800, 600);
        editorStage.setScene(scene);

        // Handle window resize
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double availableWidth = newVal.doubleValue() - controlsPanel.getPrefWidth();
            boardCanvas.resizeBoard(availableWidth, scene.getHeight());
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double availableWidth = scene.getWidth() - controlsPanel.getPrefWidth();
            boardCanvas.resizeBoard(availableWidth, newVal.doubleValue());
        });
    }

    private VBox createControlsPanel() {
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setPrefWidth(180);
        controls.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label title = new Label("Board Editor");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Piece selection
        Label pieceLabel = new Label("Select Piece:");
        pieceLabel.setStyle("-fx-font-weight: bold;");

        pieceSelector = createPieceSelector();

        // Color selection
        Label colorLabel = new Label("Color:");
        colorLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup colorGroup = new ToggleGroup();
        RadioButton whiteRadio = new RadioButton("White");
        RadioButton blackRadio = new RadioButton("Black");
        whiteRadio.setToggleGroup(colorGroup);
        blackRadio.setToggleGroup(colorGroup);
        whiteRadio.setSelected(true);

        whiteRadio.setOnAction(e -> isWhitePiece = true);
        blackRadio.setOnAction(e -> isWhitePiece = false);

        VBox colorBox = new VBox(5, whiteRadio, blackRadio);

        // Action buttons
        Button clearButton = new Button("Clear Board");
        clearButton.setOnAction(e -> clearBoard());
        clearButton.setPrefWidth(150);

        Button exportButton = new Button("Export Board");
        exportButton.setOnAction(e -> exportBoard());
        exportButton.setPrefWidth(150);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> editorStage.close());
        cancelButton.setPrefWidth(150);

        // Instructions
        Label instructions = new Label("Instructions:\n• Click on piece buttons to select\n• Click on board to place/remove\n• Right-click to remove piece");
        instructions.setWrapText(true);
        instructions.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        controls.getChildren().addAll(
                title,
                new Separator(),
                pieceLabel,
                pieceSelector,
                new Separator(),
                colorLabel,
                colorBox,
                new Separator(),
                clearButton,
                exportButton,
                cancelButton,
                new Separator(),
                instructions
        );

        return controls;
    }

    private VBox createPieceSelector() {
        VBox selector = new VBox(5);

        ToggleGroup pieceGroup = new ToggleGroup();

        // Create buttons for each piece type
        String[] pieces = {"P", "R", "N", "B", "Q", "K"};
        String[] names = {"Pawn", "Rook", "Knight", "Bishop", "Queen", "King"};

        for (int i = 0; i < pieces.length; i++) {
            RadioButton button = new RadioButton(names[i]);
            button.setToggleGroup(pieceGroup);
            button.setUserData(pieces[i]);

            if (i == 0) button.setSelected(true); // Select Pawn by default

            button.setOnAction(e -> {
                currentPieceType = (String) button.getUserData();
            });

            selector.getChildren().add(button);
        }

        return selector;
    }

    private void clearBoard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Board");
        alert.setHeaderText("Clear the entire board?");
        alert.setContentText("This will remove all pieces from the board.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Clear the board by importing an empty game state
            String emptyBoard = gameManager.getWhitePlayerName() + "-" + gameManager.getBlackPlayerName() + ",WHITE,";
            gameManager.importGame(emptyBoard);
            boardCanvas.update();
        }
    }

    private void exportBoard() {
        String exportedGame = gameManager.exportGame();

        // Show exportGame dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Board Exported");
        alert.setHeaderText("Board exported successfully!");

        TextArea textArea = new TextArea(exportedGame);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefRowCount(3);

        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
        alert.setContentText("The board has been exported. You can copy the text below:");

        alert.showAndWait();

        editorStage.close();
    }

    public void show() {
        // Start with a clear board
        clearBoard();

        // Show the editor
        editorStage.showAndWait();
    }

    // Getters for the canvas to access current selections
    public String getCurrentPieceType() {
        return currentPieceType;
    }

    public boolean isWhitePiece() {
        return isWhitePiece;
    }

    public void placePiece(Position position) {
        boardCanvas.placePieceAt(position, currentPieceType, isWhitePiece);
    }

    public void removePiece(Position position) {
        boardCanvas.removePieceAt(position);
    }
}