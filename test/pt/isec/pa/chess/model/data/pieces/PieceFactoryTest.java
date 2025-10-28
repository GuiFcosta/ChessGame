package pt.isec.pa.chess.model.data.pieces;

import org.junit.Test;
import pt.isec.pa.chess.model.data.board.Position;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceFactoryTest {
    @Test
    public void testCreatePiece() {
        // Testando criação de peça com parâmetros explícitos
        Piece piece1 = PieceFactory.createPiece(PieceType.KING, new Position(4, 4), true);
        assertEquals(PieceType.KING, piece1.getType());
        assertTrue(piece1.isWhite());
        assertEquals(new Position(4, 4), piece1.getPosition());
        assertFalse(piece1.hasMoved());

        // Testando criação de peça a partir de string
        Piece piece2 = PieceFactory.createPiece("Rb5*");
        assertEquals(PieceType.ROOK, piece2.getType());
        assertTrue(piece2.isWhite());
        assertEquals(new Position(3, 1), piece2.getPosition());
        assertFalse(piece2.hasMoved());

        Piece piece3 = PieceFactory.createPiece("bg2");
        assertEquals(PieceType.BISHOP, piece3.getType());
        assertFalse(piece3.isWhite());
        assertEquals(new Position(6, 6), piece3.getPosition());
        assertFalse(piece3.hasMoved());
    }
}
