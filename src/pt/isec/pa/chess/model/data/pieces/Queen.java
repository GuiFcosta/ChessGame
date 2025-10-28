package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;

import java.util.List;

public class Queen extends Piece {
    public Queen(Position pos, boolean isWhite) {
        super(PieceType.QUEEN, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = straightMoves(board, false);
        moves.addAll(diagonalMoves(board, false));
        return moves;
    }
}
