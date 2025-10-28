package pt.isec.pa.guifcosta.model.data.piece.pieces;

import pt.isec.pa.guifcosta.model.data.board.*;
import pt.isec.pa.guifcosta.model.data.piece.Piece;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;

public class Pawn extends Piece {
    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Board board, Position newPosition) {
        // TODO: Implement pawn movement logic
        return false;
    }
}
