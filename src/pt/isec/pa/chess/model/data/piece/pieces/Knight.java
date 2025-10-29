package pt.isec.pa.chess.model.data.piece.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.Piece;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;

import java.util.List;

public class Knight extends Piece {
    Position[] MOVES = {
            new Position(-2, -1), new Position(-2, 1), // Up-Left, Up-Right
            new Position(-1, 2), new Position(1, 2), // Right-Up, Right-Down
            new Position(2, -1), new Position(2, 1), // Down-Left, Down-Right
            new Position(-1, -2), new Position(1, -2) // Left-Up, Left-Down
    };
    public Knight(Position pos, boolean isWhite) {
        super(PieceType.KNIGHT, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        return movePieces(board, MOVES, true);
    }
}
