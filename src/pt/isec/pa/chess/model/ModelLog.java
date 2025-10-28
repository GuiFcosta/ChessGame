package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sistema de registo de eventos singleton para o jogo de xadrez.
 * Implementa Observer pattern para notificar UI de novos logs.
 * Mantém histórico centralizador de todas as ações do jogo.
 */

public class ModelLog {
    /**
     * Instância única da classe (padrão Singleton).
     */
    private static ModelLog instance;

    /**
     * Lista que armazena todas as mensagens de log do sistema.
     * Contém histórico completo das ações realizadas no jogo.
     */
    private final List<String> logs;

    /**
     * Suporte para Property Change Pattern - notifica observadores de novos logs.
     */
    private final PropertyChangeSupport pcs;


    /**
     * Construtor privado para implementar padrão Singleton.
     * Inicializa lista de logs e sistema de propriedades.
     */
    private ModelLog() {
        logs = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Obtém a instância única da classe ModelLog.
     * Cria instância se ainda não existir (lazy initialization).
     * @return instância singleton de ModelLog
     */
    public static ModelLog getInstance() {
        if (instance == null) {
            instance = new ModelLog();
        }
        return instance;
    }

    /**
     * Adiciona listener para receber notificações de novos logs.
     * @param listener callback a executar quando novos logs são adicionados
     */
    public void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Adiciona nova mensagem ao sistema de logs.
     * Notifica todos os observadores registados sobre o novo log.
     * @param message mensagem a adicionar ao histórico de logs
     */
    public void addLog(String message) {
        logs.add(message);
        pcs.firePropertyChange("logAdded", null, message);
    }

    /**
     * Obtém lista imutável de todos os logs registados.
     * Retorna cópia read-only para prevenir modificações externas.
     * @return lista não modificável com todos os logs
     */
    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    /**
     * Limpa todos os logs do sistema.
     * Remove todas as mensagens e notifica observadores da limpeza.
     */
    public void clearLogs(){
        logs.clear();
        pcs.firePropertyChange("logsCleared",null,null);
    }
}