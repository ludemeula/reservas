/*
 * DAOException.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.persistence.exception;

/**
 * Classe padrão de exceções da camada de persistência.
 *
 * @author Ludemeula Fernandes
 */
public class DAOException extends Exception {
	private static final long serialVersionUID = -5760688563412765063L;

	/**
     * Construtor da classe.
     *
     * @param message
     * @param e
     */
    public DAOException(String message, Throwable e) {
        super(message, e);
    }
    
    /**
     * Construtor da classe.
     *
     * @param message
     */
    public DAOException(String message) {
        super(message);
    }
}