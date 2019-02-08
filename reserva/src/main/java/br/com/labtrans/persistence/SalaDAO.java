/*
 * SalaDAO.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.labtrans.entities.Local;
import br.com.labtrans.entities.Sala;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por realizar a persistência de objetos da classe {@link Sala}. 
 *
 * @author Ludemeula Fernandes
 */
public class SalaDAO extends ReservaGenericDAO<Sala> {

	private static final String GET_ALL_BY_LOCAL = " SELECT sala FROM br.com.labtrans.entities.Sala sala "
			+ " WHERE sala.local.id = :local "
			+ " ORDER BY sala.descricao ";

	/**
	 * Obtém todas as {@link Sala}s ordenadas pela descrição segundo o {@link Local} informado.
	 * 
	 * @param local
	 * @return
	 * @throws DAOException
	 */
	public List<Sala> getAll(Local local) throws DAOException {
		try {
			TypedQuery<Sala> query = getEntityManager().createQuery(GET_ALL_BY_LOCAL, Sala.class);
			query.setParameter("local", local.getId());
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todos as salas ordenadas pela descrição segundo o local informado.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
}