package pt.isec.pa.guifcosta.model.data.piece;

import pt.isec.pa.guifcosta.model.data.board.Position;
import pt.isec.pa.guifcosta.model.data.piece.pieces.*;
import pt.isec.pa.guifcosta.model.data.piece.tools.*;

public class PieceFactory {
    public static Piece createPiece(Type type, Color color, Position position) {
        return switch (type) {
            case PAWN -> new Pawn(color, position);
            case BISHOP -> new Bishop(color, position);
            case ROOK -> new Rook(color, position);
            case KNIGHT -> new Knight(color, position);
            case KING -> new King(color, position);
            case QUEEN -> new Queen(color, position);
        };
    }
}
