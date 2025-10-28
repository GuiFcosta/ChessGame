package pt.isec.pa.guifcosta.model.data.piece.pieces;

import pt.isec.pa.guifcosta.model.data.board.*;
import pt.isec.pa.guifcosta.model.data.piece.*;
import pt.isec.pa.guifcosta.model.data.piece.tools.*;

public class Bishop extends Piece {
    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Board board, Position newPosition) {
        // TODO: Implement Bishop's valid move logic
        return false;
    }
}
