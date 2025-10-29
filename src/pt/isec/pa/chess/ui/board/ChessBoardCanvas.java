package pt.isec.pa.chess.ui.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;
import pt.isec.pa.chess.ui.res.ImageManager;

import java.util.List;

public class ChessBoardCanvas extends Canvas {
    ChessGameManager gameManager;
    double padding, borderSize, boardSize, cellSize, fontSize;

    private Position from, to, selected;
    List<Position> moves;

    public ChessBoardCanvas(ChessGameManager gameManager) {
        this.gameManager = gameManager;
        moves = List.of();

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
    }

    private void registerHandlers() {
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_BOARD_STATE, e -> {
            selected = null;
            update();
        });
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_PLAYER_INIT, e -> {
            from = null;
            to = null;
            selected = null;
            moves = List.of();
            update();
        });
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_PLAYER_MOVE, e -> {
            from = (Position) e.getOldValue();
            to = (Position) e.getNewValue();
            selected = null;
            moves = List.of();
            update();
        });
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_LEARNING_MODE, e -> {
            update();
        });
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            double c = e.getX() - borderSize - padding; // Adjust for the extra space
            double r = e.getY() - borderSize - padding; // Adjust for the extra space

            if (c < 0 || c >= boardSize || r < 0 || r >= boardSize)
                return;

            Position pos = new Position((int) (r / cellSize), (int) (c / cellSize));
            if (!pos.isValidPosition())
                return;

            if (!gameManager.isEmpty(pos) && gameManager.isPieceSameColor(pos, gameManager.isWhiteToMove())) {
                selected = pos;
                moves = gameManager.getMoves(pos);
                update();
            } else if (selected != null) {
                gameManager.makeMove(selected, pos);
            }
        });
    }

    public void update() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawHeader(gc);
        drawBoard(gc);
        drawPieces(gc);
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
        // desenha casas
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Position pos = new Position(r, c);
                boolean light = (r + c) % 2 == 0;
                gc.setFill(light ? Color.BEIGE : Color.rgb(147,191,133, 1));
                gc.fillRect(padding + borderSize + pos.c * cellSize, padding + borderSize + pos.r * cellSize, cellSize,
                        cellSize);
                // destaque da seleção
                if (selected != null && selected.equals(pos)) {
                    gc.setFill(Color.GRAY);
                    gc.fillOval(padding + borderSize + pos.c * cellSize + cellSize * 0.25,
                            padding + borderSize + pos.r * cellSize + cellSize * 0.25,
                            cellSize * 0.5, cellSize * 0.5);
                }
                // destaque dos movimentos possíveis
                if(gameManager.isLearningMode()){
                    if(moves.contains(pos) && gameManager.isEmpty(pos)) {
                        gc.setFill(Color.YELLOW);
                        gc.fillOval(padding + borderSize + pos.c * cellSize + cellSize * 0.25,
                                padding + borderSize + pos.r * cellSize + cellSize * 0.25,
                                cellSize * 0.5, cellSize * 0.5);
                    }
                }
            }
        }

        double offset = padding + borderSize / 2;
        double position = borderSize + cellSize / 2;

        gc.setFill(Color.WHITE);
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

                if (gameManager.isLearningMode() && moves.contains(pos) && !gameManager.isPieceSameColor(pos, gameManager.isWhiteToMove()))
                    gc.setGlobalAlpha(0.5);

                String name = (isWhite ? "W" : "B") + ".png";
                Image img = ImageManager.getImage("pieces/"
                        + PieceType.getName(type) + name);

                gc.drawImage(img, padding + borderSize + pos.c * cellSize, padding + borderSize + pos.r * cellSize, cellSize, cellSize);
                gc.setGlobalAlpha(1.0);

            }
        }
    }

    private void drawHeader (GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(fontSize));

        String text = "";
        if (gameManager.getWinner() == 2)
            text = "White Won!";
        else if (gameManager.getWinner() == -2)
            text = "Black Won!";
        else if (gameManager.getWinner() == 1)
            text = "White Is Check!";
        else if (gameManager.getWinner() == -1)
            text = "Black Is Check!";

        String whitePlayer = gameManager.getCurrentPlayer().equals("WHITE") ?
                "▶" + gameManager.getWhitePlayerName() :
                gameManager.getWhitePlayerName();
        String blackPlayer = gameManager.getCurrentPlayer().equals("BLACK") ?
                "▶" + gameManager.getBlackPlayerName() :
                gameManager.getBlackPlayerName();

        double textWidth = fontSize * whitePlayer.length() * 0.5;
        gc.fillText(whitePlayer, borderSize, padding / 2 + fontSize / 2);
        gc.fillText(blackPlayer, getWidth() - borderSize - textWidth, padding / 2 + fontSize / 2);
        gc.fillText(text, getWidth() / 2 - textWidth / 2, padding / 2 + fontSize / 2);
    }
}

