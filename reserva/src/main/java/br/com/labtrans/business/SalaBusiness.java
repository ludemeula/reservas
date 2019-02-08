/*
 * SalaBusiness.java
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

import br.com.labtrans.entities.Local;
import br.com.labtrans.entities.Sala;
import br.com.labtrans.exception.SalaException;
import br.com.labtrans.persistence.SalaDAO;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por implementar as regras de negócio relativas à classe {@link Sala}.
 *
 * @author Ludemeula Fernandes
 */
public class SalaBusiness {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private SalaDAO salaDAO;

	/**
	 * Obtém todas as {@link Sala}s ordenadas pela descrição segundo o {@link Local} informado.
	 * 
	 * @param local
	 * @return
	 * @throws SalaException
	 */
	public List<Sala> getAll(Local local) throws SalaException {
		try {
			return salaDAO.getAll(local);
		} catch (DAOException e) {
			String msg = "Falha ao obter todos as salas ordenadas pela descrição segundo o local informado.";
			logger.error(msg, e);
			throw new SalaException(msg, e);
		}
	}
}
