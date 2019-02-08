/*
 * CidadeBusiness.java
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

import br.com.labtrans.entities.Cidade;
import br.com.labtrans.entities.Estado;
import br.com.labtrans.exception.CidadeException;
import br.com.labtrans.persistence.CidadeDAO;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por implementar as regras de negócio relativas à classe {@link Cidade}.
 *
 * @author Ludemeula Fernandes
 */
public class CidadeBusiness {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private CidadeDAO cidadeDAO;

	/**
	 * Obtém todas as {@link Cidade}s ordenadas pela descrição segundo o {@link Estado} informado.
	 * 
	 * @param estado
	 * @return
	 * @throws CidadeException
	 */
	public List<Cidade> getAll(Estado estado) throws CidadeException {
		try {
			return cidadeDAO.getAll(estado);
		} catch (DAOException e) {
			String msg = "Falha ao obter todoas as cidades ordenadas pela descrição segundo o estado informado.";
			logger.error(msg, e);
			throw new CidadeException(msg, e);
		}
	}
}
