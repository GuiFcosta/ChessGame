package pt.isec.pa.guifcosta.model.data;

import pt.isec.pa.guifcosta.model.data.board.Board;
import pt.isec.pa.guifcosta.model.data.board.Position;
import pt.isec.pa.guifcosta.model.data.piece.PieceFactory;
import pt.isec.pa.guifcosta.model.data.piece.tools.Color;
import pt.isec.pa.guifcosta.model.data.piece.tools.Type;

import java.io.Serial;
import java.io.Serializable;

public class ChessGame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChessGame(Board board) {

    }

    public void createNewBoard() {
        // TODO: inicialize new board
    }
}
