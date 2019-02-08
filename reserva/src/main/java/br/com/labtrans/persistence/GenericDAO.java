/*
 * Sala.java
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
import javax.persistence.GeneratedValue;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.labtrans.entities.Entidade;
import br.com.labtrans.persistence.exception.DAOException;

/**
 * Classe padrão dos DAOs da aplicação.
 * 
 * @author Ludemeula Fernandes
 */
public abstract class GenericDAO<T extends Entidade> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public static final int DEFAULT_BATCH_MAX_SIZE = 50;
    
    /**
     * Para alterar o numero de instancias em memoria antes do 'flush' (persistencia em batch), sobrescreva esse metodo
     * A propriedade jdbc batch size deve estar alinhada com esse valor
     * @return O maximo de instancias em memoria antes de serem persistidas em batch
     */
    public int getBatchMaxSize() {
        return DEFAULT_BATCH_MAX_SIZE;
    }
    
    /**
     * Retorna o EntityManager injetado e associado a thread/transacao atual
     * @return EntityManager
     */
    protected abstract EntityManager getEntityManager();
    
    /**
     * Recupera uma Entidade segundo seu id.
     * 
     * @param entidade
     * @param id
     * @return
     * @throws DAOException
     */
    public T getById(Class<T> entidade, Object id) throws DAOException {
        if (entidade == null || id == null) {
            throw new DAOException("Argumento nulo: Classe ou ID nulos (Classe: " + entidade + "/Id: " + id + ")");
        }
        
        T resultado = null;
        try {
            resultado = getEntityManager().find(entidade, id);            
        } catch(PersistenceException e) {
            String msg = "Erro buscando entidade por ID [Entidade: " + entidade.getSimpleName()+"/ID: " + id + "]";
            logger.error(msg, e);
            throw new DAOException(msg, e);
        }
        
        return resultado;
    }

    /**
     * Persiste a entidade informada.
     * 
     * @param entidade
     * @return
     * @throws DAOException
     */
    public T persistir(T entidade) throws DAOException {        
        if(entidade == null) {
            throw new DAOException("Entidade nula, impossivel persistir!");
        } 
        
        try {
            logger.debug("Entidade sendo persistida: [" + entidade + "]");
            entidade = salvarOuAtualizar(entidade);
            getEntityManager().flush();
            logger.debug("Entidade [" + entidade + "] persistida com sucesso");
            return entidade;            
        } catch(PersistenceException e) {
            String msg = "Erro persistindo Entidade [" + entidade + "]";
            logger.error(msg, e);
            throw new DAOException(msg, e);
        }        
    }
    
    /**
     * Verifica se a entidade será inserida (persist) ou atualizada (merge) de acordo com os criterios da JPA.
     * 
     * @param entidade
     * @return
     */
    private T salvarOuAtualizar(T entidade) {
    	
    	boolean isNovaEntidade = false;
    	if(entidade.getId() != null) {
    		boolean isGeneratedValue = true;
			try {
				isGeneratedValue = entidade.getClass().getDeclaredMethod("getId").isAnnotationPresent(GeneratedValue.class);
			} catch (SecurityException | NoSuchMethodException e) {
				logger.error("Impossivel validar se @GeneratedValue esta presente no getId() da entidade [" + entidade + "], assumindo que ele esta presente (grande maioria dos casos)...", e);
			}
			if(!isGeneratedValue) {
				//Entidade com ID setada mas nao eh anotada com @GeneratedValue
				//Se o find nao achar entidade, ela eh nova.
				isNovaEntidade = (getEntityManager().find(entidade.getClass(), entidade.getId()) == null);
			}
    	} else {
    		isNovaEntidade = true;
    	}
    	
    	if(isNovaEntidade) {
            //Entidade sem ID com ou ID setado mas sem representacao no banco de dados
        	logger.debug("Entidade nova, inserindo...");
        	getEntityManager().persist(entidade);
        } else {
        	//Entidade com ID setado E (anotada com @GeneratedValue OU retornada pelo find, o que indica sua existencia na base) 
    		logger.debug("Entidade ja existente; atualizando...");
    		entidade = getEntityManager().merge(entidade);
        }
    	return entidade;
    }
    
    /**
     * Exclui a entidade informada.
     * 
     * @param entidade
     * @throws DAOException
     */
    public void excluir(T entidade) throws DAOException {
        if(entidade == null) {
            throw new DAOException("Entidade nula, impossivel exclui-la!");
        }
        
        try {
            EntityManager entityManager = getEntityManager();
            logger.debug("Entidade sendo removida: "+entidade);
            if(!entityManager.contains(entidade)) {
                entidade = entityManager.merge(entidade);
            }
            entityManager.remove(entidade);
        } catch(PersistenceException e) {
            String msg = "Erro removendo Entidade [" + entidade + "]";
            logger.error(msg, e);
            throw new DAOException(msg, e);
        }
    }
}