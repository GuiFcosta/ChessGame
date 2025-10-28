package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.board.Position;
import pt.isec.pa.chess.model.memento.CareTaker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

/**
 * Manager que coordena o jogo de xadrez e comunica com a interface de usuário.
 * Implementa o padrão Observer através de PropertyChangeSupport para notificar mudanças.
 * Gere o estado do jogo, histórico (undo/redo), persistência e logging.
 * Atua como fachada entre a UI e o modelo ChessGame.
 */
public class ChessGameManager {
    private ChessGame chessGame;
    private PropertyChangeSupport pcs;
    CareTaker cr;

    /**
     * Propriedade disparada quando o estado do tabuleiro muda.
     * Usado para atualizar a visualização do tabuleiro na UI.
     */
    public static final String PROP_BOARD_STATE = "boardState";

    /**
     * Propriedade disparada quando o jogador atual muda de turno.
     * Usado para atualizar indicadores de turno na UI.
     */
    public static final String PROP_CURRENT_PLAYER = "currentPlayer";

    /**
     * Propriedade disparada quando um jogo é carregado de arquivo.
     * Usado para notificar a UI sobre carregamento bem-sucedido.
     */
    public static final String PROP_GAME_LOADED = "gameLoaded";

    /**
     * Propriedade disparada quando um movimento falha na validação.
     * Usado para fornecer feedback visual de erro na UI.
     */
    public static final String PROP_MOVE_FAILED = "moveFailed";

    /**
     * Propriedade disparada quando o modo de aprendizagem é alterado.
     * Usado para ativar/desativar dicas visuais na UI.
     */
    public static final String PROP_LEARNING_MODE = "learningModeEnabled";

    /**
     * Propriedade disparada quando um movimento é executado com sucesso.
     * Usado para atualizar animações e efeitos visuais na UI.
     */
    public static final String PROP_PLAYER_MOVE = "move";

    /**
     * Propriedade disparada quando o jogo é inicializado/reiniciado.
     * Usado para resetar a UI para o estado inicial.
     */
    public static final String PROP_PLAYER_INIT = "init";

    /**
     * Propriedade disparada quando o jogo termina (xeque-mate, empate).
     * Usado para mostrar dialogs de fim de jogo na UI.
     */
    public static final String PROP_END = "endState";

    /**
     * Inicializa o manager com um novo jogo de xadrez.
     * Configura o sistema de propriedades, histórico e logging.
     * Registra o início do jogo no log.
     */
    public ChessGameManager() {
        chessGame = new ChessGame();
        pcs = new PropertyChangeSupport(this);
        cr = new CareTaker(chessGame);
        ModelLog.getInstance().addLog("Jogo iniciado.");
    }

    /**
     * Adiciona um listener para uma propriedade específica.
     * Permite que a UI se inscreva para receber notificações de mudanças.
     * @param property nome da propriedade a observar
     * @param listener objeto que será notificado das mudanças
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    /**
     * Dispara uma notificação de mudança de propriedade.
     * Notifica todos os listeners registrados para a propriedade.
     * @param propertyName nome da propriedade que mudou
     * @param oldValue valor anterior da propriedade
     * @param newValue novo valor da propriedade
     */
    public void fireChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Verifica se um movimento é válido sem executá-lo.
     * Útil para validação prévia antes de tentar mover.
     * @param from posição de origem da peça
     * @param to posição de destino da peça
     * @return true se movimento for válido, false caso contrário
     */
    public boolean canMakeMove(Position from, Position to) {
        return chessGame.canMakeMove(from, to);
    }

    /**
     * Executa um movimento no jogo.
     * Valida o movimento, salva estado para undo, executa e notifica observers.
     * Registra o movimento no log e dispara eventos apropriados.
     * @param from posição de origem da peça
     * @param to posição de destino da peça
     * @return true se movimento foi executado, false se inválido
     */
    public Boolean makeMove(Position from, Position to) {
        if (!canMakeMove(from, to)) {
            ModelLog.getInstance().addLog("Movimento inválido: [" + from.r + "," + from.c + "] → [" + to.r + "," + to.c + "]");
            fireChange(PROP_MOVE_FAILED, from, to);
            return false;
        }

        cr.save();
        chessGame.makeMove(from, to);
        ModelLog.getInstance().addLog("Movimento feito: [" + from.r + "," + from.c + "] → [" + to.r + "," + to.c + "]");

        fireChange(PROP_PLAYER_MOVE, from, to);
        fireChange(PROP_CURRENT_PLAYER, null, null);
        return true;
    }


    /**
     * Reinicia o jogo para o estado inicial.
     * Limpa histórico, inicializa tabuleiro e notifica mudanças.
     * Registra a ação no log.
     */
    public void resetGame() {
        cr.reset();

        String oldGame = chessGame.exportGame();
        chessGame.initializeBoard();
        String newGame = chessGame.exportGame();

        ModelLog.getInstance().addLog("Novo jogo iniciado (reset).");
        fireChange(PROP_BOARD_STATE, oldGame, newGame);
        fireChange(PROP_PLAYER_INIT, null, null);
    }

    /**
     * Desfaz o último movimento realizado.
     * Usa o padrão Memento para restaurar estado anterior.
     * Registra no log se não há mais movimentos para desfazer.
     */
    public void undo() {
        if (!cr.hasUndo())
            ModelLog.getInstance().addLog("No more undo's available");

        cr.undo();
        pcs.firePropertyChange(PROP_BOARD_STATE, null, null);
    }

    /**
     * Refaz um movimento previamente desfeito.
     * Usa o padrão Memento para avançar no histórico.
     * Registra no log se não há mais movimentos para refazer.
     */
    public void redo() {
        if (!cr.hasRedo())
            ModelLog.getInstance().addLog("No more redo's available");

        cr.redo();
        pcs.firePropertyChange(PROP_BOARD_STATE, null, null);
    }


    /**
     * Obtém o jogador atual (quem deve jogar).
     * @return "WHITE" ou "BLACK" dependendo do turno
     */
    public String getCurrentPlayer() {
        return chessGame.getCurrentPlayer();
    }


    /**
     * Salva o jogo atual em arquivo.
     * Usa serialização para persistir o estado completo.
     * @param filePath caminho do arquivo onde salvar
     * @throws ChessException se falhar ao salvar o arquivo
     */
    public void saveGame(String filePath) throws ChessException {
        try {
            ChessGameSerialization.exportGame(chessGame, filePath);
            ModelLog.getInstance().addLog("Jogo salvo para: " + filePath);
        } catch (IOException e) {
            throw new ChessException("Falha ao salvar: " + e.getMessage());
        }
    }

    /**
     * Carrega um jogo de arquivo.
     * Deserializa o estado e notifica mudanças na UI.
     * @param filePath caminho do arquivo a carregar
     * @throws ChessException se falhar ao carregar o arquivo
     */
    public void loadGame(String filePath) throws ChessException {
        try {
            String oldGame = chessGame.exportGame();
            this.chessGame = ChessGameSerialization.importGame(filePath);
            String newGame = chessGame.exportGame();
            ModelLog.getInstance().addLog("Jogo carregado de: " + filePath);
            fireChange(PROP_GAME_LOADED, oldGame, newGame);
        } catch (IOException | ClassNotFoundException e) {
            throw new ChessException("Falha ao carregar: " + e.getMessage());
        }
    }

    /**
     * Importa estado do jogo a partir de string formatada.
     * Útil para carregar jogos de texto ou clipboard.
     * @param data string contendo estado do jogo no formato de exportação
     */
    public void importGame(String data) {
        String oldGame = chessGame.exportGame();
        chessGame.importGame(data);
        ModelLog.getInstance().addLog("Jogo importado via texto.");
        fireChange(PROP_BOARD_STATE, oldGame, data);
    }

    /**
     * Exporta o estado atual do jogo para string.
     * Formato compatível com importGame().
     * @return string representando estado completo do jogo
     */
    public String exportGame() {
        return chessGame.exportGame();
    }


    /**
     * Obtém representação da peça numa posição.
     * @param pos posição a consultar
     * @return string representando a peça ou vazia se não houver peça
     */
    public String getPiece(Position pos) {
        return chessGame.getPiece(pos);
    }

    /**
     * Define os nomes dos jogadores.
     * Registra a configuração no log.
     * @param whiteName nome do jogador das peças brancas
     * @param blackName nome do jogador das peças pretas
     */
    public void setPlayerNames(String whiteName, String blackName) {
        chessGame.setWhitePlayerName(whiteName);
        chessGame.setBlackPlayerName(blackName);
        ModelLog.getInstance().addLog("Jogadores definidos: " + whiteName + " (Brancas), " + blackName + " (Pretas)");
    }


    /**
     * Verifica se é a vez das peças brancas.
     * @return true se for turno das brancas, false para pretas
     */
    public boolean isWhiteToMove() {
        return chessGame.isWhiteToMove();
    }

    /**
     * Verifica se uma peça pertence ao jogador atual.
     * @param pos posição da peça a verificar
     * @param isWhiteTurn se true verifica peças brancas, false para pretas
     * @return true se peça for da cor especificada
     */
    public boolean isPieceSameColor(Position pos, boolean isWhiteTurn) {
        return chessGame.isPieceSameColor(pos, isWhiteTurn);
    }

    /**
     * Obtém todos os movimentos possíveis para uma peça.
     * @param pos posição da peça a analisar
     * @return lista de posições válidas para movimento
     */
    public List<Position> getMoves(Position pos) {
        return chessGame.getPossibleMoves(pos);
    }

    /**
     * Obtém o tamanho do tabuleiro.
     * @return tamanho do tabuleiro (8 para xadrez padrão)
     */
    public int getBoardSize() {
        return chessGame.getBoardSize();
    }

    /**
     * Obtém o nome do jogador das peças brancas.
     * @return nome do jogador branco
     */
    public String getWhitePlayerName() {
        return chessGame.getPlayerWhite();
    }

    /**
     * Obtém o nome do jogador das peças pretas.
     * @return nome do jogador preto
     */
    public String getBlackPlayerName() {
        return chessGame.getPlayerBlack();
    }

    /**
     * Determina o vencedor ou estado do jogo.
     * @return código numérico: 2/-2 vitória branca/preta, 1/-1 xeque branco/preto, 0 normal
     */
    public int getWinner() {
        return chessGame.getWinner();
    }

    /**
     * Verifica se uma posição está vazia.
     * @param p posição a verificar
     * @return true se posição estiver vazia
     */
    public boolean isEmpty(Position p) {
        return chessGame.isEmpty(p);
    }

    /**
     * Obtém descrição textual do estado final do jogo.
     * Registra no log e dispara evento se jogo terminou.
     * @return string descrevendo estado final ou null se jogo continua
     */
    public String getEndState() {
        String endState = chessGame.getEndState();

        if (endState == null)
            return null;

        ModelLog.getInstance().addLog("End state: " + endState);
        pcs.firePropertyChange(PROP_END, null, endState);
        return endState;
    }

    /**
     * Identifica o tipo do último movimento realizado.
     * @return "Capture" se houve captura, null para movimento normal
     */
    public String getPieceMoveType() {
        return chessGame.getPieceMoveType();
    }

    /**
     * Obtém informações sobre movimentos especiais disponíveis.
     * @return string com tipo de movimento especial (Castle, Promotion, etc.)
     */
    public String getSpecialMove() {
        return chessGame.getSpecialMoves();
    }

    /**
     * Ativa ou desativa o modo de aprendizagem.
     * Dispara evento para atualizar UI e registra no log.
     * @param mode true para ativar, false para desativar
     */
    public void setLearningMode(boolean mode) {
        boolean oldMode = chessGame.isLearningMode();
        chessGame.setLearningMode(mode);
        fireChange(PROP_LEARNING_MODE, oldMode, mode);
        ModelLog.getInstance().addLog("Modo de aprendizagem " + (mode ? "ativado" : "desativado") + ".");
    }

    /**
     * Verifica se o modo de aprendizagem está ativo.
     * @return true se modo de aprendizagem estiver ativo
     */
    public boolean isLearningMode() {
        return chessGame.isLearningMode();
    }

    /**
     * Exceção específica para erros relacionados ao jogo de xadrez.
     * Usada para encapsular erros de I/O e outras operações do jogo.
     */
    public static class ChessException extends Exception {
        public ChessException(String message) {
            super(message);
        }
    }
}
