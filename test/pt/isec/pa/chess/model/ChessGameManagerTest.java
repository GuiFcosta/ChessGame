package pt.isec.pa.chess.model;

import org.junit.Test;
import static org.junit.Assert.*;

import pt.isec.pa.chess.model.data.board.Position;

public class ChessGameManagerTest {
    @Test
    public void testChessGameManagerOperations() {
        ChessGameManager manager = new ChessGameManager();

        manager.resetGame(); // Inicializa o jogo
        assertFalse(manager.cr.hasUndo());
        assertFalse(manager.cr.hasRedo());

        // Teste de undo
        manager.resetGame();
        manager.makeMove(new Position(6, 4), new Position(4, 4)); // Peão branco avança
        manager.undo(); // Desfaz movimento
        assertEquals("",manager.getPiece(new Position(4, 4))); // Posição destino deve estar vazia
        assertEquals("WHITE", manager.getCurrentPlayer()); // Turno deve voltar para branco

        // Teste de redo
        manager.redo(); // Refaz movimento
        assertEquals("Pe4", manager.getPiece(new Position(4, 4))); // Peça deve estar na posição destino
        assertEquals("BLACK", manager.getCurrentPlayer()); // Turno deve alternar para preto
    }
}