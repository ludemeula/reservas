/*
 * Entidade.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.entities;

import java.io.Serializable;

/**
 * Interface padrão para as entidades do sistema.
 * 
 * @author Ludemeula Fernandes
 */
public interface Entidade extends Serializable {
	
	/**
	 * @return the id
	 */
	public Object getId();
}