package pt.isec.pa.guifcosta.model.data.piece;

import pt.isec.pa.guifcosta.model.data.board.Board;
import pt.isec.pa.guifcosta.model.data.board.Position;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;
import pt.isec.pa.guifcosta.model.data.piece.tools.Type;

import java.io.Serial;
import java.io.Serializable;

public abstract class Piece implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final Color color;
    protected Position position;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isValidMove(Board board, Position newPosition);

    @Override
    public String toString() {
        // TODO: return has to include the piece type
        return color + " " + "type" + " at " + position;
    }
}
