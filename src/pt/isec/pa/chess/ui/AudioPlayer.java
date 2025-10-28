package pt.isec.pa.chess.ui;

import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.pieces.PieceFactory;
import pt.isec.pa.chess.model.data.pieces.PieceType;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.util.List;

public class AudioPlayer {
    private ChessGameManager gameManager;
    private ChessMenuBar menu;
    private String effect;

    public AudioPlayer(ChessGameManager gameManager, ChessMenuBar menu) {
        this.gameManager = gameManager;
        this.menu = menu;

        registerHandlers();
    }

    private void registerHandlers() {
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_PLAYER_INIT, e -> {
            if (!menu.isSoundEnabled())
                return;

            SoundManager.play("effects/game-start.mp3");
        });
        gameManager.addPropertyChangeListener(ChessGameManager.PROP_PLAYER_MOVE, e -> {
            if (!menu.isSoundEnabled())
                return;

            String endState = gameManager.getEndState(),
                    pieceMove = gameManager.getPieceMoveType(),
                    specialMove = gameManager.getSpecialMove();
            if (endState != null)
                effect = switch (endState) {
                    case "Stalemate" -> "game-draw";
                    case "White Won" -> "game-win";
                    case "Black Won" -> "game-lose";
                    case "White is in Check", "Black is in Check" -> "move-check";
                    default -> null;
                };
            else if (specialMove != null)
                effect = switch (specialMove) {
                    case "Promotion" -> "promote";
                    case "Castle" -> "castle";
                    case "En Passant" -> "capture";
                    default -> null;
                };
            else if (pieceMove != null) {
                if ("Capture".equals(pieceMove))
                    effect = "capture";
                else
                    effect = null;
            }
            else
                effect = "move-" + (gameManager.isWhiteToMove() ? "self" : "opponent");

            SoundManager.play("effects/" + effect + ".mp3");

            if (!menu.isNarratorEnabled())
                return;
            narratorMove((Position) e.getOldValue(), (Position) e.getNewValue());
        });
    }

    private void narratorMove(Position from, Position to) {
        String language = menu.isEnglish() ? "en/" : "br/br_";
        String typeStr = gameManager.getPiece(to).substring(0, 1);
        typeStr = (menu.isEnglish() ? PieceType.getName(typeStr) : PieceFactory.getNamePT(typeStr));
        String fromStr = Position.convert(from);
        String toStr = Position.convert(to);

        List<String> files = List.of(
                "effects/" + effect + ".mp3",
                language + typeStr + ".mp3",
                language + fromStr.substring(0, 1) + ".mp3",
                language + fromStr.substring(1, 2) + ".mp3",
                language + toStr.substring(0, 1) + ".mp3",
                language + toStr.substring(1, 2) + ".mp3");
        SoundManager.playSequence(files);
    }
}
