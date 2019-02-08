/*
 * AbstractController.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.labtrans.exception.BusinessException;
import br.com.labtrans.exception.ReservaMessageCode;
import br.com.labtrans.util.ResourceMessageUtil;
import br.com.labtrans.util.Util;


/**
 * Classe padrão de controlador.
 *
 * @author Ludemeula Fernandes
 */
public class AbstractController  implements Serializable {
	private static final long serialVersionUID = -6155811957522056040L;

	@Inject
	protected Result result;
	
	/**
	 * Adiciona mensagem de erro associadio ao Negócio.
	 * 
	 * @param e
	 */
	protected void adicionarMensagemErroNegocio(Throwable e) {
		String msg = null;
		
		/* Caso a instância de Throwable seja de BusinessException. */
		if(e instanceof BusinessException) {
			BusinessException exception = (BusinessException) e;

			/* Caso a exceção esteja associada a validação de negócio. */
			if(exception.isValidacaoCamposObrigatorios() || !Util.isBlank(exception.getChave())) {
				msg = exception.getMessage();
			}
			
			/* Caso a causa da exceção esteja associada a validação de negócio. */
			if(exception.getCause() != null && exception.getCause() instanceof BusinessException) {
				BusinessException cause = (BusinessException) exception.getCause();
				
				if(cause.isValidacaoCamposObrigatorios() || !Util.isBlank(cause.getChave())) {
					msg = cause.getMessage();
				}
			}
		}
		
		/* Se a mensagem estiver vazia trata-se de uma exeção inesperada.*/
		if(Util.isBlank(msg)) {
			adicionarMensagemErroInesperado(e);
		} else {
			result.use(Results.http()).body(msg).setStatusCode(Status.BAD_REQUEST.getStatusCode());
		}
	}

	/**
	 * Adiciona mensagem de Erro Inesperado.
	 * 
	 * @param e
	 */
	protected void adicionarMensagemErroInesperado(Throwable e) {
		result.use(Results.http()).body(ResourceMessageUtil.getDescricaoErro(ReservaMessageCode.ERRO_INESPERADO, e)).setStatusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}
}