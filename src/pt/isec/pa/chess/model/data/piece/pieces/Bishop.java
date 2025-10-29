package pt.isec.pa.chess.model.data.piece.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.Piece;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(Position pos, boolean isWhite) {
        super(PieceType.BISHOP, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        return diagonalMoves(board, false);
    }
}

