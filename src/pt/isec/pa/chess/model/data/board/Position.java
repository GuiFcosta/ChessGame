package pt.isec.pa.chess.model.data.board;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 1L;

    public int r;
    public int c;

    public Position(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public Position(Position pos) {
        this(pos.r, pos.c);
    }

    public void add(Position pos) {
        this.r += pos.r;
        this.c += pos.c;
    }

    public boolean isValidPosition() {
        return r >= 0 && r < Board.BOARD_SIZE && c >= 0 && c < Board.BOARD_SIZE;
    }

    public static Position convert(String str) {
        if (str.length() != 2)
            return null;
        char row = str.charAt(1);
        char col = str.charAt(0);
        if (row < '1' || row > ('0' + Board.BOARD_SIZE) || col < 'a' || col > ('a' + Board.BOARD_SIZE))
            return null;
        return new Position((Board.BOARD_SIZE - 1) - (row - '1'), col - 'a');
    }

    public static String convert(Position pos) {
        if (pos == null)
            return null;

        if (pos.isValidPosition()) {
            char row = (char) ('1' + Board.BOARD_SIZE - pos.r - 1);
            char col = (char) ('a' + pos.c);

            return String.format("%c%c", col, row);
        }

        return null;
    }

    @Override
    public String toString() {
        return "[" + r + ", " + c + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != getClass())
            return false;

        Position object = (Position) obj;
        return r == object.r && c == object.c;
    }
}
