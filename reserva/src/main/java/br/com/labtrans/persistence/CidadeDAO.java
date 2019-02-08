/*
 * CidadeDAO.java
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
import br.com.labtrans.entities.Estado;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por realizar a persistência de objetos da classe {@link Cidade}.
 *
 * @author Ludemeula Fernandes
 */
public class CidadeDAO extends ReservaGenericDAO<Cidade> {

	private static final String GET_ALL_BY_ESTADO = " SELECT cidade FROM br.com.labtrans.entities.Cidade cidade "
			+ " WHERE cidade.estado.id = :estado "
			+ " ORDER BY cidade.descricao ";

	/**
	 * Obtém todas as {@link Cidade}s ordenadas pela descrição segundo o {@link Estado} informado.
	 * 
	 * @param estado
	 * @return
	 * @throws DAOException
	 */
	public List<Cidade> getAll(Estado estado) throws DAOException {
		try {
			TypedQuery<Cidade> query = getEntityManager().createQuery(GET_ALL_BY_ESTADO, Cidade.class);
			query.setParameter("estado", estado.getId());
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todas as cidades ordenadas pela descrição segundo o estado informado.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
}
