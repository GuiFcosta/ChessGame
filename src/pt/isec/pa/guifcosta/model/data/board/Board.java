package pt.isec.pa.guifcosta.model.data.board;

import pt.isec.pa.guifcosta.model.data.piece.Piece;

import java.io.Serial;
import java.io.Serializable;

public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final int SIZE = 8;

    private final Piece[][] board;

    public Board() {
        board = new Piece[SIZE][SIZE];
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(Position position) {
        if(!position.isValid())
            return null;
        return board[position.row][position.column];
    }

    public void setPiece(Position position, Piece piece) {
        if(position.isValid())
            board[position.row][position.column] = piece;
    }

    public void removePiece(Position position) {
        if(position.isValid())
            board[position.row][position.column] = null;
    }

    public void initializeBoard() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                board[i][j] = null;
            }
        }
    }

}
