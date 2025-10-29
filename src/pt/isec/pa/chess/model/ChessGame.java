package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.data.piece.Piece;
import pt.isec.pa.chess.model.data.piece.tools.PieceFactory;
import pt.isec.pa.chess.model.data.piece.tools.PieceType;
import pt.isec.pa.chess.model.memento.IMemento;
import pt.isec.pa.chess.model.memento.IOriginator;
import pt.isec.pa.chess.model.memento.Memento;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Classe principal que representa um jogo de xadrez completo.
 * Implementa Serializable para permitir salvar/carregar jogos e IOriginator para o padrão Memento.
 * Gere o estado do jogo, movimentos, jogadores e regras do xadrez.
 */
public class ChessGame implements Serializable, IOriginator {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Tabuleiro de xadrez que contém todas as peças e suas posições.
     */
    private Board board;

    /**
     * Nome do jogador que joga com as peças brancas.
     */
    private String whitePlayerName;

    /**
     * Nome do jogador que joga com as peças pretas.
     */
    private String blackPlayerName;

    /**
     * Indica se é a vez do jogador branco jogar (true) ou do preto (false).
     */
    private boolean whiteToMove;

    /**
     * Modo de aprendizagem ativado para mostrar dicas e movimentos possíveis.
     */
    private boolean learningMode;

    /**
     * Última peça capturada no jogo, usada para efeitos sonoros e histórico.
     */
    private Piece lastCapturedPiece;

    /**
     * Construtor padrão que inicializa um novo jogo de xadrez.
     * Cria um tabuleiro vazio e define nomes padrão para os jogadores.
     */
    public ChessGame() {
        this.board = new Board();

        this.whitePlayerName = "Player1";
        this.blackPlayerName = "Player2";
    }

    /**
     * Construtor que inicializa o jogo com um estado específico.
     * @param state String representando o estado do jogo no formato exportado
     */
    public ChessGame(String state) {
        this();
        importGame(state);
    }


    /**
     * Retorna o tamanho do tabuleiro (8x8).
     * @return tamanho do tabuleiro
     */
    public int getBoardSize() {
        return Board.BOARD_SIZE;
    }

    /**
     * Obtém o nome do jogador branco.
     * @return nome do jogador branco
     */
    public String getPlayerWhite() {
        return whitePlayerName;
    }

    /**
     * Obtém o nome do jogador preto.
     * @return nome do jogador preto
     */
    public String getPlayerBlack() {
        return blackPlayerName;
    }

    /**
     * Define o nome do jogador branco.
     * @param name novo nome para o jogador branco
     */
    public void setWhitePlayerName(String name) {
        this.whitePlayerName = name;
    }

    /**
     * Define o nome do jogador preto.
     * @param name novo nome para o jogador preto
     */
    public void setBlackPlayerName(String name) {
        this.blackPlayerName = name;
    }

    /**
     * Importa o estado do jogo a partir de uma string formatada.
     * Limpa o tabuleiro atual e reconstroi baseado nos dados fornecidos.
     * @param data string contendo o estado completo do jogo
     */
    public void importGame(String data) {
        board.clearBoard();

        data = data.replaceAll("[\\n\\r\\s]", "");

        whiteToMove = data.toUpperCase().startsWith("WHITE");
        String[] parts = data.substring(data.indexOf(",") + 1).split(",");

        for (String pieceStr : parts) {
            Piece piece = PieceFactory.createPiece(pieceStr);
            board.addPiece(piece, piece.getPosition());
        }
    }

    /**
     * Exporta o estado atual do jogo para uma string.
     * Inclui a cor do jogador atual e todas as peças com suas posições.
     * @return string representando o estado completo do jogo
     */
    public String exportGame() {
        StringBuilder sb = new StringBuilder();
        sb.append(whiteToMove ? "WHITE" : "BLACK").append(",");

        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null) {
                    sb.append(piece).append(",");
                }
            }
        }

        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1); // Remove a última vírgula
        }

        return sb.toString();
    }

    /**
     * Inicializa o tabuleiro com a configuração padrão de início de jogo.
     * Coloca todas as peças nas suas posições iniciais regulamentares.
     */
    public void initializeBoard() {
        board.clearBoard();
        importGame("WHITE," +
                "ra8*,nb8,bc8,qd8,ke8*,bf8,ng8,rh8*," +
                "pa7,pb7,pc7,pd7,pe7,pf7,pg7,ph7," +
                "Pa2,Pb2,Pc2,Pd2,Pe2,Pf2,Pg2,Ph2," +
                "Ra1*,Nb1,Bc1,Qd1,Ke1*,Bf1,Ng1,Rh1*");
    }

    /**
     * Obtém a representação string da peça numa posição específica.
     * @param pos posição no tabuleiro a verificar
     * @return string representando a peça ou string vazia se não houver peça
     */
    public String getPiece(Position pos) {
        Piece piece = board.getPiece(pos);
        if (piece == null) {
            return "";
        }
        return piece.toString();
    }

    /**
     * Verifica se é a vez do jogador branco.
     * @return true se for a vez das brancas, false para as pretas
     */
    public boolean isWhiteToMove() {
        return whiteToMove;
    }


    /**
     * Obtém todos os movimentos possíveis para uma peça numa posição.
     * Remove movimentos que deixariam o rei em xeque.
     * @param pos posição da peça a analisar
     * @return lista de posições para onde a peça pode mover-se
     */
    public List<Position> getPossibleMoves(Position pos) {
        Piece piece = board.getPiece(pos);
        List<Position> possibleMoves = piece.getPossibleMoves(board);
        piece.removeCheckMoves(board, possibleMoves);

        return possibleMoves; // Retorna lista vazia se não houver peça
    }

    /**
     * Executa um movimento no tabuleiro.
     * Valida o movimento, atualiza o estado e troca o turno.
     * @param from posição de origem da peça
     * @param to posição de destino da peça
     * @return true se o movimento foi executado com sucesso, false caso contrário
     */
    public boolean makeMove(Position from, Position to) {
        if (!from.isValidPosition() || !to.isValidPosition()) {
            return false; // Movimento inválido
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false; // Movimento inválido
        }

        // nova adição para som, guardar peça capturada antes de mover
        lastCapturedPiece = board.getPiece(to);

        if (!board.movePiece(from, to)) {
            return false;
        }

        whiteToMove = !whiteToMove;
        return true;
    }

    /**
     * Determina o vencedor ou estado atual do jogo.
     * @return 2/-2 para vitória branca/preta, 1/-1 para xeque branco/preto, 0 para jogo normal
     */
    public int getWinner() {
        if (board.isCheckmate(whiteToMove))
            return !whiteToMove ? 2 : -2;
        else if (board.isCheck(PieceType.KING, whiteToMove))
            return whiteToMove ? 1 : -1;

        return 0;
    }

    /**
     * Verifica se uma posição está vazia.
     * @param pos posição a verificar
     * @return true se a posição estiver vazia, false caso contrário
     */
    public boolean isEmpty(Position pos) {
        return board.getPiece(pos) == null;
    }

    /**
     * Obtém o jogador atual em formato string.
     * @return "WHITE" ou "BLACK" dependendo de quem deve jogar
     */
    public String getCurrentPlayer() {
        return whiteToMove ? "WHITE" : "BLACK";
    }

    /**
     * Ativa ou desativa o modo de aprendizagem.
     * @param mode true para ativar, false para desativar
     */
    public void setLearningMode(boolean mode) {
        this.learningMode = mode;
    }

    /**
     * Verifica se o modo de aprendizagem está ativo.
     * @return true se modo de aprendizagem estiver ativo
     */
    public boolean isLearningMode() {
        return learningMode;
    }

    /**
     * Obtém uma descrição textual do estado final do jogo.
     * @return string descrevendo xeque-mate, empate, xeque ou null se jogo continua
     */
    public String getEndState() {
        if (board.isStalemate())
            return "Stalemate";
        else if (board.isCheckmate(whiteToMove))
            return (!whiteToMove ? "White" : "Black") + " Won";
        else if (board.isCheck(PieceType.KING, whiteToMove))
            return (whiteToMove ? "White" : "Black") + " is in Check";

        return null;
    }

    /**
     * Obtém a última peça capturada no jogo.
     * Usado para efeitos visuais e sonoros.
     * @return peça capturada ou null se nenhuma foi capturada
     */
    public Piece getLastCapturedPiece() {
        return lastCapturedPiece;
    }

    /**
     * Verifica se uma peça pertence ao jogador atual.
     * @param pos posição da peça a verificar
     * @param whiteToMove se true verifica peças brancas, se false verifica pretas
     * @return true se a peça for da cor especificada, false caso contrário
     */
    public boolean isPieceSameColor(Position pos, boolean whiteToMove) {
        if (!pos.isValidPosition() || board.getPiece(pos) == null) {
            return false; // Posição inválida ou sem peça
        }
        return board.getPiece(pos).isWhite() == whiteToMove;
    }

    /**
     * Identifica o tipo do último movimento realizado.
     * @return "Capture" se houve captura, null caso contrário
     */
    public String getPieceMoveType() {
        if (lastCapturedPiece == null) {
            return null;
        }
        return "Capture";
    }


    /**
     * Obtém informações sobre movimentos especiais disponíveis.
     * Inclui roque, en passant, etc.
     * @return string com movimentos especiais possíveis
     */
    public String getSpecialMoves(){
        return board.getSpecialMoves();
    }


    /**
     * Verifica se um movimento é válido sem executá-lo.
     * Útil para validação prévia de movimentos.
     * @param from posição de origem
     * @param to posição de destino
     * @return true se o movimento for válido, false caso contrário
     */
    public boolean canMakeMove(Position from, Position to) {
        if (!from.isValidPosition() || !to.isValidPosition()) {
            return false; // Movimento inválido
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false; // Não há peça na posição de origem
        }

        List<Position> possibleMoves = piece.getPossibleMoves(board);
        piece.removeCheckMoves(board, possibleMoves);

        return possibleMoves.contains(to); // Verifica se o movimento é possível
    }

    /**
     * Representação textual completa do estado do jogo.
     * Inclui nomes dos jogadores, turno atual e estado do tabuleiro.
     * @return string formatada com informações completas do jogo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(whitePlayerName).append(" vs ").append(blackPlayerName).append("\n");
        sb.append("Current turn: ").append(getCurrentPlayer()).append("\n");
        sb.append(board.toString());
        return sb.toString();
    }

    /**
     * Cria um memento com o estado atual do jogo.
     * Implementação do padrão Memento para undo/redo.
     */
    @Override
    public IMemento save() {
        return new Memento(this);
    }

    /**
     * Cria um memento com o estado atual do jogo.
     * Implementação do padrão Memento para undo/redo.
     */
    @Override
    public void restore(IMemento memento) {
        Object obj = memento.getSnapshot();

        if (obj instanceof ChessGame game) {
            this.whitePlayerName = game.getPlayerWhite();
            this.blackPlayerName = game.getPlayerBlack();
            this.whiteToMove = game.isWhiteToMove();
            this.learningMode = game.isLearningMode();
            this.board = game.board;
            this.lastCapturedPiece = game.getLastCapturedPiece();
        }
    }
}