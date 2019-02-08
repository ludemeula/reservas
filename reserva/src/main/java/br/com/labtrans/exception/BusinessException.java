/*
 * BusinessException.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.exception;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import br.com.labtrans.util.Util;

/**
 * Classe base para exceções de negócio, fornecendo métodos úteis para lidar com arquivo de properties com mensagens padrões de erro.
 *
 * @author Ludemeula Fernandes
 */
public abstract class BusinessException extends Exception {

private static final long serialVersionUID = 1L;
	
	private String chave;
	private boolean concatenaParametros;
	private List<?> parametros;
	
	/**
	 * Verifica se exceção de negócio se refere a validação de campos obrigatórios.
	 * 
	 * @return
	 */
	public boolean isValidacaoCamposObrigatorios(){
		return !Util.isEmpty(chave) || parametros != null && parametros.size() > 0;
	}
		
	/**
	 * Retrona o arquivo *.properties, onde estão descritas as mensagens de validação e negócio.
	 * 
	 * @return
	 */
	protected abstract String getMessageProperties();
		
	/**
	 * Construtor da classe.
	 *
	 * @param message
	 * @param e
	 */
	public BusinessException(String message, Throwable e) {
		super(message, e);
	}

	/**
	 * Construtor da classe.
	 *
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
	}
	
	/**
	 * Construtor da classe.
	 *
	 * @param e
	 */
	public BusinessException(Throwable e){
		super(e);
	}
	
	/**
	 * Construtor da classe.
	 *
	 * @param code
	 */
	public <T> BusinessException(T code) {
		this.chave = code.toString();
	}

	/**
	 * Construtor da classe.
	 *
	 * @param chave
	 * @param concatenaParametros
	 * @param parametros
	 */
	public <E, T> BusinessException(T chave, boolean concatenaParametros, List<E> parametros) {
		this.chave = chave.toString();
		this.concatenaParametros = concatenaParametros;
		this.parametros = parametros;
	}
	
	/**
	 * Construtor da classe.
	 * 
	 * @param <T>
	 *
	 * @param chave
	 * @param parametros
	 */
	public <E, T> BusinessException(T chave, List<E> parametros) {
		this(chave, Boolean.TRUE, parametros);
	}
	

	/**
	 * Construtor da classe.
	 *
	 * @param chave
	 * @param concatenaParametros
	 * @param parametros
	 */
	public <T> BusinessException(T chave, boolean concatenaParametros, Object... parametros) {
		this.chave = chave.toString();
		this.concatenaParametros = concatenaParametros;
		
		if(parametros != null){
			this.parametros = Arrays.asList(parametros);
		}
	}
		
	/**
	 * Construtor da classe.
	 *
	 * @param chave
	 * @param parametros
	 */
	public <T> BusinessException(T chave, Object... parametros) {
		this(chave, Boolean.TRUE, parametros);
	}
	
	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		String mensagem = super.getMessage();
		if(mensagem == null){
			mensagem = getMensagemChave();
			
			if(parametros != null) {
				if(concatenaParametros) {
					mensagem = getMensagemComParametrosConcactenados(mensagem);
				} else {
					mensagem = getMensagemComParametros(mensagem);
				}
			}
		}
		return mensagem;
	}
	
	/**
	 * Inclui os parâmetros Concatenados na mensagem informada.
	 * 
	 * @param mensagem
	 * @return
	 */
	private String getMensagemComParametrosConcactenados(String mensagem) {
		String mensagemFormatada = mensagem;
		
		String parametrosConcatenados = getParametrosConcatenados();
		if(parametrosConcatenados.trim().length() > 0){
			mensagemFormatada = getMensagemFormatada(mensagem, parametrosConcatenados);
		} 
		return mensagemFormatada;
	}
	
	/**
	 * Inclui os parâmetros a mensagem informada.
	 * 
	 * @param mensagem
	 * @return
	 */
	private String getMensagemComParametros(String mensagem) {
		return getMensagemFormatada(mensagem, parametros.toArray());
	}
	
	/**
	 * Retorna a mensagem formatada com os parâmetros informados.
	 * 
	 * @param mensagem
	 * @param parametros
	 * @return
	 */
	private String getMensagemFormatada(String mensagem, Object... parametros){
		return MessageFormat.format(mensagem, parametros);
	}
		
	/**
	 * Retorna a mensagem obtida segundo o código da chave.
	 * 
	 * @return
	 */
	private String getMensagemChave(){
		return getResourceBundle().getString(chave);
	}
	
	/**
	 * Retorna a instância de {@link ResourceBundle}.
	 * 
	 * @return
	 */
	private ResourceBundle getResourceBundle(){
		return ResourceBundle.getBundle(getMessageProperties());
	}
	
	/**
	 * Retorna a {@link List} de parâmetros separados por virgula concatenados em uma {@link String}.
	 * 
	 * @param paramsCode
	 * @return
	 */
	private  String getParametrosConcatenados(){
		StringBuilder build = new StringBuilder();
	
		Iterator<?> iterator =  parametros.iterator();
		
		while(iterator.hasNext()){
			Object valor = iterator.next().toString();
			
			if(getResourceBundle().containsKey(valor.toString())){
				build.append(getResourceBundle().getString(valor.toString()));
			} else {
				build.append(valor.toString());
			}
			
			if(iterator.hasNext()){
				build.append(", ");
			}
		}
		return build.toString();
	}

	/**
	 * @return the chave
	 */
	public String getChave() {
		return chave;
	}

	/**
	 * @return the parametros
	 */
	public List<?> getParametros() {
		return parametros;
	}
}