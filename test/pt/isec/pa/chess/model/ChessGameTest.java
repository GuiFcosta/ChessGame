package pt.isec.pa.chess.model;

import org.junit.Test;
import static org.junit.Assert.*;

import pt.isec.pa.chess.model.data.board.Position;

public class ChessGameTest {
    @Test
    public void testChessGameOperations() {
        ChessGame game = new ChessGame();

        // Teste inicial: nomes dos jogadores
        assertEquals("Player1", game.getPlayerWhite());
        assertEquals("Player2", game.getPlayerBlack());

        // Teste de inicialização do tabuleiro
        game.initializeBoard();
        assertEquals("WHITE", game.getCurrentPlayer());
        assertEquals("Ke1*", game.getPiece(new Position(7, 4))); // Rei branco na posição inicial

        // Teste de exportação e importação de estado
        String exportedState = game.exportGame();
        ChessGame importedGame = new ChessGame(exportedState);
        assertEquals(exportedState, importedGame.exportGame());

        // Teste de xeque
        game.initializeBoard();
        game.makeMove(new Position(6, 5), new Position(4, 5)); // Peão branco avança
        game.makeMove(new Position(1, 4), new Position(3, 4)); // Peão preto avança
        game.makeMove(new Position(7, 3), new Position(3, 7)); // Rainha branca não coloca o rei preto em xeque
        assertEquals(0, game.getWinner()); // Xeque branco
    }
}