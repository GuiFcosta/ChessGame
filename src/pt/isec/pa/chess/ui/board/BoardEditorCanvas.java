package pt.isec.pa.chess.ui.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;
import pt.isec.pa.chess.ui.res.ImageManager;

public class BoardEditorCanvas extends Canvas {
    private ChessGameManager gameManager;
    private BoardEditor editor;
    private double padding, borderSize, boardSize, cellSize, fontSize;

    public BoardEditorCanvas(ChessGameManager gameManager, BoardEditor editor) {
        this.gameManager = gameManager;
        this.editor = editor;

        registerHandlers();
        update();
    }

    private void registerHandlers() {
        // Listen for board state changes
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_BOARD_STATE, e -> {
            update();
        });

        // Handle mouse clicks for placing/removing pieces
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            double c = e.getX() - borderSize - padding;
            double r = e.getY() - borderSize - padding;

            if (c < 0 || c >= boardSize || r < 0 || r >= boardSize)
                return;

            Position pos = new Position((int) (r / cellSize), (int) (c / cellSize));

            if (e.getButton() == MouseButton.PRIMARY) {
                // Left click - place piece
                placePieceAt(pos, editor.getCurrentPieceType(), editor.isWhitePiece());
            } else if (e.getButton() == MouseButton.SECONDARY) {
                // Right click - remove piece
                removePieceAt(pos);
            }
        });
    }

    public void placePieceAt(Position position, String pieceType, boolean isWhite) {
        // Create piece string in the format expected by the game
        String pieceStr = (isWhite ? pieceType.toUpperCase() : pieceType.toLowerCase())
                + (char)('a' + position.c) + (8 - position.r);

        // Get current game state and modify it
        String currentState = gameManager.exportGame();
        String[] parts = currentState.split(",", 3);

        // Remove any existing piece at this position
        StringBuilder newPieces = new StringBuilder();
        if (parts.length > 2 && !parts[2].trim().isEmpty()) {
            String[] pieces = parts[2].split(",");
            for (String piece : pieces) {
                if (piece.length() >= 3) {
                    String posStr = piece.substring(1);
                    if (!posStr.equals((char)('a' + position.c) + "" + (8 - position.r))) {
                        if (newPieces.length() > 0) newPieces.append(",");
                        newPieces.append(piece);
                    }
                }
            }
        }

        // Add the new piece
        if (newPieces.length() > 0) newPieces.append(",");
        newPieces.append(pieceStr);

        // Reconstruct the game state
        String newState = parts[0] + "," + parts[1] + "," + newPieces.toString();
        gameManager.importGame(newState);
    }

    public void removePieceAt(Position position) {
        // Get current game state and modify it
        String currentState = gameManager.exportGame();
        String[] parts = currentState.split(",", 3);

        if (parts.length <= 2) return;

        // Remove piece at this position
        StringBuilder newPieces = new StringBuilder();
        if (!parts[2].trim().isEmpty()) {
            String[] pieces = parts[2].split(",");
            for (String piece : pieces) {
                if (piece.length() >= 3) {
                    String posStr = piece.substring(1);
                    if (!posStr.equals((char)('a' + position.c) + "" + (8 - position.r))) {
                        if (newPieces.length() > 0) newPieces.append(",");
                        newPieces.append(piece);
                    }
                }
            }
        }

        // Reconstruct the game state
        String newState = parts[0] + "," + parts[1] + "," + newPieces.toString();
        gameManager.importGame(newState);
    }

    public void update() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawBoard(gc);
        drawPieces(gc);
        drawHeader(gc);
    }

    public void resizeBoard(double width, double height) {
        double size = Math.min(width, height);
        this.setWidth(size);
        this.setHeight(size);

        cellSize = boardSize / (double) gameManager.getBoardSize();
        padding = size * 0.05;
        borderSize = size * 0.0375;
        boardSize = size - 2 * (padding + borderSize);
        fontSize = size * 0.025;

        update();
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRoundRect(padding, padding, getWidth() - 2 * padding, getHeight() - 2 * padding, 10, 10);

        int N = gameManager.getBoardSize();
        // Draw squares
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Position pos = new Position(r, c);
                boolean light = (r + c) % 2 == 0;
                gc.setFill(light ? Color.BEIGE : Color.rgb(147,191,133, 1));
                gc.fillRect(padding + borderSize + pos.c * cellSize, padding + borderSize + pos.r * cellSize, cellSize, cellSize);
            }
        }

        // Draw coordinates
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(fontSize));

        double offset = padding + borderSize / 2;
        double position = borderSize + cellSize / 2;

        for (int i = 0; i < gameManager.getBoardSize(); i++) {
            // Letters
            String letter = String.valueOf((char) ('a' + i));
            for (int j = 0; j < 2; j++)
                gc.fillText(letter, padding + position + i * cellSize - fontSize / 4,
                        (j == 0 ? offset + fontSize / 4 : getHeight() - offset + fontSize / 4));

            // Numbers
            String number = String.valueOf(gameManager.getBoardSize() - i);
            for (int j = 0; j < 2; j++)
                gc.fillText(number, (j == 0 ? offset - fontSize / 4 : getWidth() - offset - fontSize / 4),
                        padding + position + i * cellSize + fontSize / 4);
        }
    }

    private void drawPieces(GraphicsContext gc) {
        for (int r = 0; r < gameManager.getBoardSize(); r++) {
            for (int c = 0; c < gameManager.getBoardSize(); c++) {
                Position pos = new Position(r, c);
                String piece = gameManager.getPiece(pos);

                if (piece.isBlank())
                    continue;

                String type = piece.substring(0, 1);
                boolean isWhite = (type.equals(type.toUpperCase()));

                String name = (isWhite ? "W" : "B") + ".png";
                Image img = ImageManager.getImage("pieces/"
                        + PieceType.getName(type) + name);

                gc.drawImage(img, padding + borderSize + pos.c * cellSize, padding + borderSize + pos.r * cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawHeader(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(fontSize));

        String title = "Board Editor - Left click to place, Right click to remove";
        double textWidth = fontSize * title.length() * 0.3;
        gc.fillText(title, getWidth() / 2 - textWidth / 2, padding / 2 + fontSize / 2);
    }
}