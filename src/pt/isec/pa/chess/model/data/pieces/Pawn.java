package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Position pos, boolean isWhite) {
        super(PieceType.PAWN, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        int direction = isWhite ? -1 : 1;
        List<Position> possibleMoves = straightMoves(board, direction);
        possibleMoves.addAll(attackMoves(board, direction));
        return possibleMoves;
    }

    public List<Position> straightMoves(Board board, int direction) {
        List<Position> moves = new ArrayList<>();
        Position newPos = new Position(pos.r + direction, pos.c);

        if (board.isEmpty(newPos)) {
            moves.add(new Position(newPos));

            newPos.r = pos.r + 2 * direction;
            if (board.isEmpty(newPos) && pos.r == (isWhite() ? Board.BOARD_SIZE -2 : 1)) {
                moves.add(newPos);
            }
        }
        return moves;
    }

    public List<Position> attackMoves(Board board, int direction) {
        List<Position> moves = new ArrayList<>();
        int r = pos.r, c = pos.c;

        Position[] diagonals = {
                new Position(r + direction, c - 1),
                new Position(r + direction, c + 1)
        };
        Position[] sides = {
                new Position(pos.r, pos.c - 1), // Diagonal-Left
                new Position(pos.r, pos.c + 1) // Diagonal-Right
        };

        for (Position diagonal : diagonals) {
            if (board.hasEnemy(pos, diagonal)) {
                moves.add(diagonal);
            }
        }
        for (Position side : sides) {
            if (board.hasEnemy(pos, side) && board.getPiece(side).getType() == PieceType.PAWN) {
                Position to = new Position(side.r + direction, side.c);
                if (board.isEmpty(to))
                    moves.add(to);
            }
        }

        return moves;
    }
}
