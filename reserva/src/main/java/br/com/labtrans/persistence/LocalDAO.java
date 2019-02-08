/*
 * LocalDAO.java
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

import br.com.labtrans.entities.Cidade;
import br.com.labtrans.entities.Local;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por realizar a persistência de objetos da classe {@link Local}.
 *
 * @author Ludemeula Fernandes
 */
public class LocalDAO extends ReservaGenericDAO<Local> {

	private static final String GET_ALL_BY_CIDADE = " SELECT local FROM br.com.labtrans.entities.Local local "
			+ " WHERE local.cidade.id = :cidade "
			+ " ORDER BY local.descricao ";

	/**
	 * Obtém todas os {@link Local}s ordenados pela descrição segundo a {@link Cidade} informado.
	 * 
	 * @param cidade
	 * @return
	 * @throws DAOException
	 */
	public List<Local> getAll(Cidade cidade) throws DAOException {
		try {
			TypedQuery<Local> query = getEntityManager().createQuery(GET_ALL_BY_CIDADE, Local.class);
			query.setParameter("cidade", cidade.getId());
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todos as locais ordenados pela descrição segundo a cidade informada.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
}
