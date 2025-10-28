package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;

import java.util.List;

public class King extends Piece {
    public King(Position pos, boolean isWhite) {
        super(PieceType.KING, pos, isWhite);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> moves = straightMoves(board, true);
        moves.addAll(diagonalMoves(board, true));

        // Castling
        if (!hasMoved()) {
            addCastlingMoves(board, moves);
        }

        return moves;
    }

    private void addCastlingMoves(Board board, List<Position> moves) {
        int row = pos.r;
        // Kingside (pequeno roque)
        if (canCastle(board, pos, new Position(row, 7), true)) {
            moves.add(new Position(row, pos.c + 2));
        }
        // Queenside (grande roque)
        if (canCastle(board, pos, new Position(row, 0), false)) {
            moves.add(new Position(row, pos.c - 2));
        }
    }

    private boolean canCastle(Board board, Position kingPos, Position rookPos, boolean kingSide) {
        Piece rook = board.getPiece(rookPos);

        if (!(rook instanceof Rook) || rook.hasMoved())
            return false;

        int step = kingSide ? 1 : -1;
        int col = kingPos.c + step;
        // Verificar se as casas entre rei e torre estão vazias
        while ((kingSide && col < rookPos.c) || (!kingSide && col > rookPos.c)) {
            if (!board.isEmpty(new Position(kingPos.r, col)))
                return false;
            col += step;
        }
        // O rei não pode estar, passar ou terminar em xeque
        for (int i = 0; i <= 2; i++) {
            Position testPos = new Position(kingPos.r, kingPos.c + (kingSide ? i : -i));
        }

        return true;
    }
}

