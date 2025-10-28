package pt.isec.pa.guifcosta.model.data.piece.pieces;

import pt.isec.pa.guifcosta.model.data.board.Board;
import pt.isec.pa.guifcosta.model.data.board.Position;
import pt.isec.pa.guifcosta.model.data.piece.Piece;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;

public class King extends Piece {
        public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Board board, Position newPosition) {
        // TODO: Implement King's valid move logic
        return false;
    }
}
