package pt.isec.pa.chess.model.data.board;

import pt.isec.pa.chess.model.data.pieces.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Representa o tabuleiro de xadrez 8x8 com todas as peças e regras de movimento.
 * Implementa Serializable para permitir salvar/carregar o estado do tabuleiro.
 * Gere posicionamento de peças, movimentos especiais e validação de regras.
 */
public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Constante que define o tamanho padrão do tabuleiro (8x8).
     */
    public static final int BOARD_SIZE = 8;

    /**
     * Matriz bidimensional que representa o tabuleiro com as peças.
     * Índices [linha][coluna] onde [0][0] é a8 e [7][7] é h1.
     */
    private final Piece[][] board;

    /**
     * Posição alvo para captura en passant.
     * Null se não há en passant possível no turno atual.
     */
    private Position enPassantTarget;

    /**
     * String que armazena o tipo do último movimento especial realizado.
     * Pode ser "Castle", "Promotion" ou null para movimentos normais.
     */
    private String specialMoves; // Para armazenar movimentos especiais como promoção

    /**
     * Construtor que inicializa um tabuleiro vazio 8x8.
     * Todas as posições começam com null (sem peças).
     */
    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    /**
     * Limpa completamente o tabuleiro removendo todas as peças.
     * Define todas as posições como null.
     */
    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }

    /**
     * Obtém a peça numa posição específica do tabuleiro.
     * @param position posição a consultar
     * @return peça na posição ou null se estiver vazia
     */
    public Piece getPiece(Position position) {
        return board[position.r][position.c];
    }

    /**
     * Adiciona uma peça numa posição específica do tabuleiro.
     * Atualiza a posição da peça e marca como movida se aplicável.
     * @param piece peça a adicionar (pode ser null para remover)
     * @param pos posição onde colocar a peça
     */
    public void addPiece(Piece piece, Position pos) {
        board[pos.r][pos.c] = piece;
        if (piece == null) {
            return;
        }
        piece.setPosition(pos);
        if (piece.hasMoved())
            piece.setHasMoved();
    }

    /**
     * Remove uma peça de uma posição específica.
     * Define a posição como null.
     * @param pos posição da peça a remover
     */
    public void removePiece(Position pos) {
        board[pos.r][pos.c] = null;
    }

    /**
     * Move uma peça de uma posição para outra, aplicando todas as regras.
     * Valida o movimento, executa movimentos especiais (en passant, roque, promoção)
     * e atualiza o estado do tabuleiro.
     * @param from posição de origem da peça
     * @param to posição de destino da peça
     * @return true se movimento foi executado com sucesso, false caso contrário
     */
    public boolean movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        PieceType type = piece.getType();

        if (piece == null)
            return false;

        List<Position> moves = piece.getPossibleMoves(this);
        piece.removeCheckMoves(this, moves);

        if (!moves.contains(to))
            return false;

        // En Passant
        if (type == PieceType.PAWN) {
            int dir = piece.isWhite() ? 1 : -1;
            Position capturedPawnPos = new Position(to.r + dir, to.c);
            Piece pawn = getPiece(capturedPawnPos);
            if (pawn != null && pawn.getType() == PieceType.PAWN && !pawn.isSameColor(piece)) {
                removePiece(capturedPawnPos);
                specialMoves = "En Passant";
            }
        }

        removePiece(from);
        addPiece(piece, to);

        if (type == PieceType.KING && Math.abs(from.c - to.c) == 2) {
            // Castling
            int row = from.r;
            if (to.c == 6) { // kingside
                Piece rook = getPiece(new Position(row, 7));
                removePiece(new Position(row, 7));
                addPiece(rook, new Position(row, 5));
            } else if (to.c == 2) { // queenside
                Piece rook = getPiece(new Position(row, 0));
                removePiece(new Position(row, 0));
                addPiece(rook, new Position(row, 3));
            }
            specialMoves = "Castle"; // Adiciona o castling como movimento especial
        }

        // Verificar promoção
        if (type == PieceType.PAWN) {
            int promotionRow = piece.isWhite() ? 0 : BOARD_SIZE - 1;
            if (to.r == promotionRow) {
                // Por agora promove sempre a rainha
                promotePawn(to, PieceType.QUEEN);
                specialMoves = "Promotion"; // Adiciona a promoção como movimento especial
                // fazer a promoção parametrizada para UI depois?
            }
        }

        return true;
    }

    /**
     * Obtém informação sobre o último movimento especial realizado.
     * @return string com tipo do movimento especial ou null se não houve
     */
    public String getSpecialMoves() {
        return specialMoves;
    }

    /**
     * Promove um peão para outro tipo de peça.
     * Substitui o peão na posição pela nova peça do mesmo time.
     * @param pos posição do peão a promover
     * @param newType tipo da nova peça (QUEEN, ROOK, BISHOP, KNIGHT)
     */
    public void promotePawn(Position pos, PieceType newType) {
        Piece oldPawn = getPiece(pos);
        if (!(oldPawn instanceof Pawn))
            return;

        Piece newPiece = null;
        boolean isWhite = oldPawn.isWhite();

        switch (newType) {
            case QUEEN -> newPiece = new Queen(pos, isWhite);
            case ROOK -> newPiece = new Rook(pos, isWhite);
            case BISHOP -> newPiece = new Bishop(pos, isWhite);
            case KNIGHT -> newPiece = new Knight(pos, isWhite);
            default -> newPiece = new Queen(pos, isWhite); // Default promoção para rainha
        }

        addPiece(newPiece, pos);
    }

    /**
     * Verifica se uma posição está vazia.
     * @param to posição a verificar
     * @return true se posição for válida e estiver vazia, false caso contrário
     */
    public boolean isEmpty(Position to) {
        return to.isValidPosition() && getPiece(to) == null;
    }

    /**
     * Verifica se existe uma peça inimiga numa posição.
     * Compara as cores das peças nas posições de origem e destino.
     * @param from posição da peça de referência
     * @param to posição a verificar se tem inimigo
     * @return true se há peça inimiga na posição destino, false caso contrário
     */
    public boolean hasEnemy(Position from, Position to) {
        return to.isValidPosition() &&
                getPiece(to) != null &&
                !getPiece(from).isSameColor(getPiece(to));
    }

    /**
     * Encontra a posição de uma peça específica no tabuleiro.
     * @param type tipo da peça a procurar
     * @param isWhite cor da peça (true para branca, false para preta)
     * @return posição da peça ou null se não encontrada
     */
    private Position findPiece(PieceType type, boolean isWhite) {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position pos = new Position(i, j);

                Piece piece = getPiece(pos);
                if (piece != null && piece.getType() == type && piece.isWhite() == isWhite)
                    return pos;
            }

        return null;
    }

    /**
     * Verifica se uma peça específica está em xeque.
     * Analisa se alguma peça adversária pode capturar a peça alvo.
     * @param type tipo da peça a verificar (normalmente KING)
     * @param isWhite cor da peça a verificar
     * @return true se a peça estiver em xeque, false caso contrário
     */
    public boolean isCheck(PieceType type, boolean isWhite) {
        Position piecePos = new Position(findPiece(type, isWhite));
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position pos = new Position(i, j);
                Piece piece = getPiece(pos);
                if (piece == null || piece.isSameColor(isWhite))
                    continue;
                List<Position> moves = piece.getPossibleMoves(this);
                if (moves.contains(piecePos))
                    return true;
            }
        return false;
    }

    /**
     * Simula um movimento para verificar se resultaria em xeque.
     * Executa temporariamente o movimento e verifica se o rei fica em xeque.
     * @param from posição de origem do movimento
     * @param to posição de destino do movimento
     * @return true se o movimento resultar em xeque ao próprio rei, false caso contrário
     */
    public boolean isNextMoveCheck(Position from, Position to) {
        Piece fromPiece = getPiece(from);
        Piece toPiece = getPiece(to);

        removePiece(from);
        addPiece(fromPiece, to);

        boolean isCheck = isCheck(PieceType.KING, fromPiece.isWhite());

        addPiece(fromPiece, from);
        addPiece(toPiece, to);

        return isCheck;
    }

    /**
     * Verifica se um jogador está em xeque-mate.
     * Testa se todas as peças do jogador não têm movimentos válidos disponíveis.
     * @param isWhite cor do jogador a verificar
     * @return true se estiver em xeque-mate, false caso contrário
     */
    public boolean isCheckmate(boolean isWhite) {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                Position pos = new Position(i, j);
                Piece piece = getPiece(pos);
                if (piece == null || !piece.isSameColor(isWhite))
                    continue;
                List<Position> moves = piece.getPossibleMoves(this);
                piece.removeCheckMoves(this, moves);
                if (!moves.isEmpty())
                    return false;
            }

        return true;
    }

    /**
     * Verifica se o jogo está em empate (stalemate).
     * Ocorre quando ambos os jogadores estão em posição de xeque-mate simultaneamente.
     * @return true se estiver em empate, false caso contrário
     */
    public boolean isStalemate() {
        return isCheckmate(true) && isCheckmate(false);
    }

    /**
     * Obtém a posição alvo atual para captura en passant.
     * @return posição onde en passant é possível ou null se não aplicável
     */
    public Position getEnPassantTarget() {
        return enPassantTarget;
    }

    /**
     * Cria representação textual do tabuleiro completo.
     * Mostra todas as peças nas suas posições com formatação legível.
     * Peças que não se moveram (rei/torre) são marcadas com espaçamento especial.
     * @return string formatada representando o estado atual do tabuleiro
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                Piece piece = getPiece(pos);
                if (piece == null) {
                    sb.append("     ");
                    continue;
                }
                PieceType type = piece.getType();
                if ((type == PieceType.KING || type == PieceType.ROOK) && !piece.hasMoved())
                    sb.append(" " + piece + " ");
                else
                    sb.append(" " + piece + "  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
