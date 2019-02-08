/*
 * ReservaDAO.java
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

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.labtrans.entities.Reserva;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe responsável por realizar a persistência de objetos da classe {@link Reserva}.
 *
 * @author Ludemeula Fernandes
 */
@Named
@Dependent
public class ReservaDAO extends ReservaGenericDAO<Reserva> {

	private static final String GET_ALL = " SELECT reserva FROM br.com.labtrans.entities.Reserva reserva "
			+ " INNER JOIN FETCH reserva.sala sala "
			+ " INNER JOIN FETCH sala.local local "
			+ " INNER JOIN FETCH local.cidade cidade "
			+ " INNER JOIN FETCH cidade.estado "
			+ " ORDER BY reserva.dataInicio ";
	
	private static final String GET_ALL_BY_RESERVA = " SELECT reserva FROM br.com.labtrans.entities.Reserva reserva "
			+ " WHERE reserva.id != :reserva "
			+ " AND reserva.sala.id = :sala "
			+ " AND (reserva.dataInicio >= :dataInicio AND reserva.dataFim <= :dataFim) ";

	/**
	 * Obtém todas as {@link Reserva}s ordenadas pela data de início.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<Reserva> getAll() throws DAOException {
		try {
			TypedQuery<Reserva> query = getEntityManager().createQuery(GET_ALL, Reserva.class);
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todos as reservas ordenadas pela data de início.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
	
	/**
	 * Obtém todas as {@link Reserva}s segundo a sala, dataInicio e dataFim informadas.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<Reserva> getAll(Reserva reserva) throws DAOException {
		try {
			TypedQuery<Reserva> query = getEntityManager().createQuery(GET_ALL_BY_RESERVA, Reserva.class);
			query.setParameter("reserva", reserva.getId());
			query.setParameter("sala", reserva.getSala().getId());
			query.setParameter("dataInicio", reserva.getDataInicio());
			query.setParameter("dataFim", reserva.getDataFim());
			return query.getResultList();
		} catch (PersistenceException e) {
			String msg = "Falha ao obter todos as reservas segundo a sala, dataInicio e dataFim informadas.";
			logger.error(msg, e);
			throw new DAOException(msg, e);
		}
	}
}