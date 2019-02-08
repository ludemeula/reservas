/*
 * EstadoDAO.java
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

import br.com.labtrans.entities.Estado;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por realizar a persistência de objetos da classe {@link Estado}.
 *
 * @author Ludemeula Fernandes
 */
public class EstadoDAO extends ReservaGenericDAO<Estado> {

	private static final String GET_ALL = " SELECT estado FROM br.com.labtrans.entities.Estado estado "
			+ " ORDER BY estado.descricao ";

	/**
	 * Obtém todos os {@link Estado} ordenados pela descrição.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<Estado> getAll() throws DAOException {
		try {
			TypedQuery<Estado> query = getEntityManager().createQuery(GET_ALL, Estado.class);
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todos os estados ordenados pela descrição.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
}
