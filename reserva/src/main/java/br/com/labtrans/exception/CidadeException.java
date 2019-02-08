/*
 * EstadoException.java.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.exception;

import br.com.labtrans.constantes.Constantes;
import br.com.labtrans.entities.Cidade;

/**
 * Exceção a ser lançada na ocorrência de falhas e validações de negócio referentes a entidade {@link Cidade}.
 * 
 * @author Ludemeula Fernandes.
 */
public class CidadeException extends BusinessException {
	private static final long serialVersionUID = -4409113775323836639L;
	
	/**
	 * Construtor da classe.
	 *
	 * @param e
	 */
	public CidadeException(Throwable e) {
		super(e);
	}

	/**
	 * Construtor da classe.
	 *
	 * @param message
	 */
	public CidadeException(String message) {
		super(message);
	}

	/**
	 * Construtor da classe.
	 *
	 * @param message
	 * @param e
	 */
	public CidadeException(String message, Throwable e) {
		super(message, e);
	}
	
	/**
	 * Construtor da classe.
	 *
	 * @param code
	 * @param parametros
	 */
	public CidadeException(ReservaMessageCode code, Object... parametros ) {
		super(code, parametros);
	}

	/**
	 * @see br.gov.cvm.comum.exceptions.BusinessException#getMessageProperties()
	 */
	@Override
	protected String getMessageProperties() {
		return Constantes.MESSAGE_PROPERTIES;
	}
}