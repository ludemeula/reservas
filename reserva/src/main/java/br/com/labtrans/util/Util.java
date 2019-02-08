/*
 * Util.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.util;

import java.util.Collection;
import java.util.Map;

/**
 * Classe de utilitários do sistema.
 *
 * @author Ludemeula Fernandes
 */
public class Util {

	/**
     * Verifica se a {@link String} informada é nula, ou vazia.
     * 
     * @param valor
     * @return
     */
    public static boolean isEmpty(String valor) {
        return valor == null || "".equals(valor);
    }
      
    /**
     * Verifica se a {@link Collection} informada é nula, ou vazia.
     * 
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Verifica se o {@link Map} informado é nulo ou vazio.
     * 
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }
	
    /** 
     * Verifica se a {@link String} informada não esta vazia, desconsiderando espaços no momento da validação.
     * 
     * @param valor
     * @return
     */
    public static boolean isBlank(final String valor) {
    	return isEmpty(valor) || valor.trim().length() == 0;
    }
}