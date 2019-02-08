/*
 * EstadoBusiness.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.business;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.labtrans.entities.Estado;
import br.com.labtrans.exception.EstadoException;
import br.com.labtrans.persistence.EstadoDAO;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por implementar as regras de negócio relativas à classe {@link Estado}.
 *
 * @author Ludemeula Fernandes
 */
public class EstadoBusiness {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private EstadoDAO estadoDAO;

	/**
	 * Obtém todos os {@link Estado} ordenados pela descrição.
	 * 
	 * @throws EstadoException
	 */
	public List<Estado> getAll() throws EstadoException {
		try {
			return estadoDAO.getAll();
		} catch (DAOException e) {
			String msg = "Falha ao obter todos os estados ordenados pela descrição.";
			logger.error(msg, e);
			throw new EstadoException(msg, e);
		}
	}
}