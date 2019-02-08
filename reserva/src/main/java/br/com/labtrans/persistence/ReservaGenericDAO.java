/*
 * ReservaGenericDAO.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.labtrans.entities.Entidade;

/**
 * Classe mãe de todas as classes DAO.
 *
 * @author Ludemeula Fernandes
 */
public class ReservaGenericDAO<T extends Entidade> extends GenericDAO<T> {
	
	@PersistenceContext(unitName = "reserva")
	private EntityManager entityManager;

	/**
	 * @see br.com.labtrans.persistence.GenericDAO#getEntityManager()
	 */
	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}