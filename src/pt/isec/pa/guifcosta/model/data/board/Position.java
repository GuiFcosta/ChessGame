package pt.isec.pa.guifcosta.model.data.board;

import java.io.Serial;
import java.io.Serializable;

public class Position implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public int row;
    public int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position pos) {
        this(pos.row, pos.column);
    }

    public boolean isValid() {
        return row >= 0 && row < Board.SIZE && column >= 0 && column < Board.SIZE;
    }

    @Override
    public String toString() {
        return "[row=" + row + ", column=" + column + "]";
    }
}
