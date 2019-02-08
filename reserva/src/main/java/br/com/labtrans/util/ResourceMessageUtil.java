/*
 * ResourceMessageUtil.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Classe utilitária que provê os métodos para manipulação do arquivo *.properties  com as mensagens e descrições apresentadas no sistema. 
 *
 * @author Ludemeula Fernandes
 */
public class ResourceMessageUtil {
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("reservas/messages");

	/**
	 * Retorna a mensagem associada a chave informada.
	 * 
	 * @param chave
	 * @return
	 */
	public static String getDescricao(final String chave) {
		if (chave == null || chave.length() == 0) {
			throw new IllegalArgumentException("A chave do atributo informado é inválida.");
		}
		return RESOURCE_BUNDLE.getString(chave);
	}
	
	/**
	 * Retorna a mensagem associada a chave informada.
	 * 
	 * @param code
	 * @return
	 */
	public static String getDescricao(final Object code) {
		return getDescricao(code.toString());
	}
	
	/**
	 * Retorna a mensagem de erro, formatada com a descrição do {@link Throwable} informado.
	 * 
	 * @param e
	 * @return
	 */
	public static String getDescricaoErro(final String chave, final Throwable e) {
		String descricao = getDescricao(chave);		
		return MessageFormat.format(descricao, e);
	}
	
	
	/**
	 * Retorna a mensagem de erro, formatada com a descrição do {@link Throwable} informado.
	 * 
	 * @param chave
	 * @param e
	 * @return
	 */
	public static String getDescricaoErro(final Object chave, final Throwable e) {	
		return getDescricaoErro(chave.toString(), e);
	}
}