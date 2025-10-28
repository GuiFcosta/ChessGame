package pt.isec.pa.guifcosta.model.data.piece.pieces;

import pt.isec.pa.guifcosta.model.data.board.*;
import pt.isec.pa.guifcosta.model.data.piece.Piece;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;

public class Queen extends Piece {
    public Queen(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Board board, Position newPosition){
        // TODO: Implement queen movement logic
        return false;
    }
}
