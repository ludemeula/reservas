/*
 * LocalBusiness.java
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
import br.com.labtrans.entities.Local;
import br.com.labtrans.exception.LocalException;
import br.com.labtrans.persistence.LocalDAO;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por implementar as regras de negócio relativas à classe {@link Local}.
 *
 * @author Ludemeula Fernandes
 */
public class LocalBusiness {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private LocalDAO localDAO;

	/**
	 * Obtém todas os {@link Local}s ordenados pela descrição segundo a {@link Cidade} informado.
	 * 
	 * @param cidade
	 * @return
	 * @throws LocalException
	 */
	public List<Local> getAll(Cidade cidade) throws LocalException {
		try {
			return localDAO.getAll(cidade);
		} catch (DAOException e) {
			String msg = "Falha ao obter todos as locais ordenados pela descrição segundo a cidade informada.";
			logger.error(msg, e);
			throw new LocalException(msg, e);
		}
	}
}