package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;

import java.util.List;

public class Rook extends Piece {
    public Rook(Position pos, boolean isWhite) {
        super(PieceType.ROOK, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        return straightMoves(board, false);
    }
}
