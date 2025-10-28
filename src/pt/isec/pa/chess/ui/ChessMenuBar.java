package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

public class ChessMenuBar extends MenuBar {
    ChessGameManager gameManager;
    MenuBar menuBar;
    Menu menuGame, menuMode, menuAudio, menuAccessibility;
    MenuItem newGame, openGame, saveGame, importGame, exportGame, quitGame, undoMove, redoMove;
    CheckMenuItem menuCheckAudio, menuNarrator, menuEng, menuPor, normalMode, showMoves, learningMode;

    private boolean isSoundEnabled;

    public ChessMenuBar(ChessGameManager gameManager) {
        this.gameManager = gameManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        menuBar = new MenuBar();

        menuGame = new Menu("Game");
        newGame = new MenuItem("New");
        openGame = new MenuItem("Open");
        saveGame = new MenuItem("Save");
        importGame = new MenuItem("Import");
        exportGame = new MenuItem("Export");
        quitGame = new MenuItem("Quit");

        menuMode = new Menu("Mode");
        normalMode = new CheckMenuItem("Normal");
        learningMode = new CheckMenuItem("Learning");

        showMoves = new CheckMenuItem("Show possible moves");
        undoMove = new MenuItem("Undo");
        redoMove = new MenuItem("Redo");

        menuAudio = new Menu("Audio");
        menuCheckAudio = new CheckMenuItem("On");
        menuAccessibility = new Menu("Accessibility");
        menuNarrator = new CheckMenuItem("Narrator");
        menuEng = new CheckMenuItem("English");
        menuPor = new CheckMenuItem("Portuguese");

        menuGame.getItems().addAll(newGame, openGame, saveGame, importGame, exportGame, new SeparatorMenuItem(), quitGame);
        menuMode.getItems().addAll(normalMode, learningMode, new SeparatorMenuItem(),showMoves, undoMove, redoMove);
        menuAudio.getItems().addAll(menuCheckAudio);
        menuAccessibility.getItems().addAll(menuNarrator, new SeparatorMenuItem(), menuEng, menuPor);
        setMode(false);
        setAudio();
        setNarrator();
        setLanguage(true);

        this.getMenus().addAll(menuGame, menuMode, menuAudio, menuAccessibility);
    }

    private void registerHandlers() {
        newGame.setOnAction(e -> {askNames(); createNewGame();});
        openGame.setOnAction(e -> openGame());
        saveGame.setOnAction(e -> saveGame());
        importGame.setOnAction(e -> importGame());
        exportGame.setOnAction(e -> exportGame());
        quitGame.setOnAction(e -> Platform.exit());
        normalMode.setOnAction(e -> setMode(false));
        learningMode.setOnAction(e -> setMode(true));
        showMoves.setOnAction(e -> setShowMoves());
        undoMove.setOnAction(e -> undo());
        redoMove.setOnAction(e -> redo());
        menuCheckAudio.setOnAction(e -> setAudio());
        menuNarrator.setOnAction(e -> setNarrator());
        menuEng.setOnAction(e -> setLanguage(true));
        menuPor.setOnAction(e -> setLanguage(false));
    }

    private void update() {}

    private void createNewGame() {
        gameManager.resetGame();
        ((RootPane) this.getScene().getRoot()).chessBoardCanvas.update();
    }

    private void askNames() {
        TextInputDialog dialog1 = new TextInputDialog("Jogador1");
        dialog1.setTitle("Novo Jogo");
        dialog1.setHeaderText("Nome do jogador branco:");
        dialog1.setContentText("Branco:");
        String whiteName = dialog1.showAndWait().orElse("White");

        TextInputDialog dialog2 = new TextInputDialog("Jogador2");
        dialog2.setTitle("Novo Jogo");
        dialog2.setHeaderText("Nome do jogador preto:");
        dialog2.setContentText("Preto:");
        String blackName = dialog2.showAndWait().orElse("Black");

        gameManager.setPlayerNames(whiteName, blackName);
    }

    private void openGame() {
        askNames();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Chess file (*.chess)", "*.chess"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (file != null) {
            try {
                gameManager.loadGame(file.getAbsolutePath());
                System.out.println("Jogo carregado com sucesso!");
                System.out.println(gameManager.exportGame());
            } catch (ChessGameManager.ChessException e) {
                System.out.println("Erro ao carregar o jogo: " + e.getMessage());
            }
        }
    }

    private void saveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Chess file (*.chess)", "*.chess"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showSaveDialog(this.getScene().getWindow());
        if (file != null) {
            try {
                gameManager.saveGame(file.getAbsolutePath());
                System.out.println("Jogo salvo com sucesso!");
            } catch (ChessGameManager.ChessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void importGame() {
        //askNames();

        // Criar um diálogo de escolha
        Alert choiceDialog = new Alert(Alert.AlertType.CONFIRMATION);
        choiceDialog.setTitle("Import Game");
        choiceDialog.setHeaderText("Como deseja importar o jogo?");
        choiceDialog.setContentText("Escolha sua opção:");

        // Criar botões personalizados
        ButtonType pasteButton = new ButtonType("Colar Texto");
        ButtonType fileButton = new ButtonType("Selecionar Arquivo");
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        choiceDialog.getButtonTypes().setAll(pasteButton, fileButton, cancelButton);

        // Mostrar diálogo e processar a escolha
        Optional<ButtonType> result = choiceDialog.showAndWait();

        if (result.isPresent()) {
            askNames();

            if (result.get() == pasteButton) {
                // Opção de colar texto
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Import Game");
                dialog.setHeaderText("Importar Jogo");
                dialog.setContentText("Cole os dados do jogo:");

                dialog.showAndWait().ifPresent(data -> {
                    try {
                        gameManager.importGame(data);
                        System.out.println("Jogo importado com sucesso!");
                        System.out.println(gameManager.exportGame());
                    } catch (Exception e) {
                        System.out.println("Erro ao importar o jogo: " + e.getMessage());
                    }
                });
            } else if (result.get() == fileButton) {
                // Opção de selecionar arquivo
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Import Game");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                        new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));

                File file = fileChooser.showOpenDialog(this.getScene().getWindow());
                if (file != null) {
                    try (Scanner scanner = new Scanner(file)) {
                        StringBuilder content = new StringBuilder();

                        while (scanner.hasNextLine())
                            content.append(scanner.nextLine()).append("\n");

                        String data = content.toString().trim();
                        if (!data.isBlank()) {
                            gameManager.importGame(data);
                            System.out.println("Jogo importado com sucesso!");
                            System.out.println(gameManager.exportGame());
                        }
                    } catch (Exception ex) {
                        System.out.println("Erro ao importar o jogo: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void exportGame() {
        try {
            String exportedData = gameManager.exportGame();
            TextArea textArea = new TextArea(exportedData);
            textArea.setEditable(false);

            ButtonType copyButton = new ButtonType("Copiar", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", copyButton, ButtonType.CLOSE);
            alert.setTitle("Export Game");
            alert.setHeaderText("Dados do Jogo Exportado");
            alert.getDialogPane().setContent(textArea);

            alert.showAndWait().ifPresent(response -> {
                if (response == copyButton) {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(exportedData);
                    clipboard.setContent(content);
                    System.out.println("Dados copiados para a área de transferência!");
                }
            });
        } catch (Exception e) {
            System.out.println("Erro ao exportar o jogo: " + e.getMessage());
        }
    }

    private void setMode(boolean isLearningMode) {
        normalMode.setSelected(!isLearningMode);
        learningMode.setSelected(isLearningMode);
        showMoves.setDisable(!isLearningMode);
        undoMove.setDisable(!isLearningMode);
        redoMove.setDisable(!isLearningMode);

        gameManager.setLearningMode(isLearningMode ? showMoves.isSelected() : false);
    }

    private void setShowMoves() {
        boolean isShowMoves = showMoves.isSelected();

        gameManager.setLearningMode(isShowMoves);
    }

    private void undo() {
        gameManager.undo();
    }

    private void redo() {
        gameManager.redo();
    }

    private void setAudio() {
        this.isSoundEnabled = !this.isSoundEnabled;
        menuCheckAudio.setSelected(isSoundEnabled);
    }

    public void setNarrator() {
        boolean isNarratorEnabled = menuNarrator.isSelected();

        menuPor.setDisable(!isNarratorEnabled);
        menuEng.setDisable(!isNarratorEnabled);
    }

    public void setLanguage(boolean isEnglish) {
        menuEng.setSelected(isEnglish);
        menuPor.setSelected(!isEnglish);
    }

    public boolean isSoundEnabled() {
        return menuCheckAudio.isSelected();
    }

    public boolean isNarratorEnabled() {
        return menuNarrator.isSelected();
    }

    public boolean isEnglish() {
        return menuEng.isSelected();
    }
}
