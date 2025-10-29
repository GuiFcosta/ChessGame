package pt.isec.pa.chess.model.data.piece.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.Piece;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;

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
