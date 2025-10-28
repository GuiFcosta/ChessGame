package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final PieceType type;
    protected Position pos;
    protected final boolean isWhite;
    protected boolean hasMoved;

    public Piece(PieceType type, Position pos, boolean isWhite) {
        this.type = type;
        this.pos = pos;
        this.isWhite = isWhite;
        this.hasMoved = false;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Position getPosition(){
        return pos;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    public boolean isSameColor(boolean isWhite) {
        return this.isWhite == isWhite;
    }

    public boolean isSameColor(Piece piece) {
        return isSameColor(piece.isWhite);
    }

    private boolean isMoveLegal(int r, int c, Board board) {
        Piece target = board.getPiece(new Position(r, c));
        if (target == null) {
            return true;
        }
        return !target.isSameColor(this);
    }

    public abstract List<Position> getPossibleMoves(Board board);

    public List<Position> movePieces(Board board, Position[] directions, boolean isOneMove) {
        List<Position> moves = new ArrayList<>();

        for (Position direction : directions) {
            Position to = new Position(pos);
            do {
                to.add(direction);
                if (to.isValidPosition() && isMoveLegal(to.r, to.c, board)) {
                    moves.add(new Position(to));
                    if (board.getPiece(to) != null) {
                        break;
                    }
                } else {
                    break;
                }
            } while (!isOneMove && !board.hasEnemy(pos, to));
        }
        return moves;
    }

    public List<Position> diagonalMoves(Board board, boolean isOneMove) {
        Position[] directions = {
                new Position(1, 1),
                new Position(1, -1),
                new Position(-1, 1),
                new Position(-1, -1)
        };
        return movePieces(board, directions, isOneMove);
    }

    public List<Position> straightMoves(Board board, boolean isOneMove) {
        Position[] directions = {
                new Position(1, 0),
                new Position(-1, 0),
                new Position(0, 1),
                new Position(0, -1)
        };
        return movePieces(board, directions, isOneMove);
    }

    public void removeCheckMoves(Board board, List<Position> moves) {
        moves.removeIf(move -> board.isNextMoveCheck(pos, move));
    }

    @Override
    public String toString() {
        String typeChar = type.getIcon();
        typeChar = isWhite ? typeChar.toUpperCase() : typeChar;

        char col = (char) ('a' + pos.c);
        char row = (char) ('0' + Board.BOARD_SIZE - pos.r);

        boolean hasAsterisk = (!hasMoved && (type == PieceType.KING || type == PieceType.ROOK));

        return typeChar + col + row + (hasAsterisk ? "*" : "");
    }
}

