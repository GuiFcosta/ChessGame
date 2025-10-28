package pt.isec.pa.guifcosta.model.data.piece.pieces;

import pt.isec.pa.guifcosta.model.data.board.*;
import pt.isec.pa.guifcosta.model.data.piece.Piece;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;

public class Rook extends Piece {
    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Board board, Position newPosition){
        // TODO: Implement rook movement logic
        return false;
    }
}
