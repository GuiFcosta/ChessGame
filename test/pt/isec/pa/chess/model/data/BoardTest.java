package pt.isec.pa.chess.model.data;

import org.junit.Test;
import static org.junit.Assert.*;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.pieces.*;

public class BoardTest {
    @Test
    public void testBoardOperations() {
        Board board = new Board();

        // Teste inicial: tabuleiro vazio
        assertNull(board.getPiece(new Position(0, 0)));

        // Adicionar uma peça ao tabuleiro
        Piece king = new King(new Position(0, 0), true);
        board.addPiece(king, new Position(0, 0));
        assertEquals(king, board.getPiece(new Position(0, 0)));

        // Remover a peça do tabuleiro
        board.removePiece(new Position(0, 0));
        assertNull(board.getPiece(new Position(0, 0)));

        // Teste de movimento de peça
        board.addPiece(king, new Position(0, 0));
        boolean moved = board.movePiece(new Position(0, 0), new Position(1, 1));
        assertTrue(moved);
        assertNull(board.getPiece(new Position(0, 0)));
        assertEquals(king, board.getPiece(new Position(1, 1)));

        // Teste de promoção de peão
        Piece pawn = new Pawn(new Position(6, 0), true);
        board.addPiece(pawn, new Position(6, 0));
        board.promotePawn(new Position(6, 0), PieceType.QUEEN);
        Piece promotedPiece = board.getPiece(new Position(6, 0));
        assertTrue(promotedPiece instanceof Queen);
        assertEquals(PieceType.QUEEN, promotedPiece.getType());

        // Teste de xeque
        Piece enemyQueen = new Queen(new Position(7, 1), false);
        board.addPiece(enemyQueen, new Position(7, 1));
        assertTrue(board.isCheck(PieceType.KING, true));

        // Teste de xeque-mate
        board.clearBoard();
        board.addPiece(new King(new Position(0, 0), true), new Position(0, 0));
        board.addPiece(new Queen(new Position(1, 1), false), new Position(1, 1));
        assertFalse(board.isCheckmate(true));
    }
}