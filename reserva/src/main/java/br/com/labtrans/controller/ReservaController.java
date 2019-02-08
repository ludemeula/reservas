/*
 * ReservaController.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.view.Results;
import br.com.labtrans.business.CidadeBusiness;
import br.com.labtrans.business.EstadoBusiness;
import br.com.labtrans.business.LocalBusiness;
import br.com.labtrans.business.ReservaBusiness;
import br.com.labtrans.business.SalaBusiness;
import br.com.labtrans.entities.Cidade;
import br.com.labtrans.entities.Estado;
import br.com.labtrans.entities.Local;
import br.com.labtrans.entities.Reserva;
import br.com.labtrans.entities.Sala;
import br.com.labtrans.exception.CidadeException;
import br.com.labtrans.exception.EstadoException;
import br.com.labtrans.exception.LocalException;
import br.com.labtrans.exception.ReservaException;
import br.com.labtrans.exception.ReservaMessageCode;
import br.com.labtrans.exception.SalaException;
import br.com.labtrans.util.ResourceMessageUtil;

/**
 * Controlador referente as ações de Reserva.
 *
 * @author Ludemeula Fernandes
 */
@Controller
public class ReservaController extends AbstractController {
	private static final long serialVersionUID = -6625866515858840098L;

	@Inject
	private ReservaBusiness reservaBusiness;
	
	@Inject
	private EstadoBusiness estadoBusiness;
	
	@Inject
	private CidadeBusiness cidadeBusiness;
	
	@Inject
	private LocalBusiness localBusiness;

	@Inject
	private SalaBusiness salaBusiness;

	/**
	 * Salva a {@link Reserva} informada.
	 * 
	 * @param estado
	 * @param cidade
	 * @param local
	 * @param reserva
	 */
	@Post
	@Consumes
	@Path("reserva/salvar")
	public void salvar(Reserva reserva, Estado estado, Cidade cidade, Local local) {
		try {
			reservaBusiness.salvar(reserva, estado, cidade, local);
			result.use(Results.http()).body(ResourceMessageUtil.getDescricao(ReservaMessageCode.RESERVA_SALVA_SUCESSO));
		} catch (ReservaException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}
	
	/**
	 * Exclui a {@link Reserva} informada.
	 * 
	 * @param incentivo
	 */
	@Post
	@Path("reserva/excluir")
	@Consumes("application/json")  
	public void excluir(final Reserva reserva) {
		try {
			reservaBusiness.excluir(reserva);
			result.use(Results.http()).body(ResourceMessageUtil.getDescricao(ReservaMessageCode.RESERVA_EXCLUIDA_SUCESSO));
		} catch (ReservaException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}
	
	/**
	 * Obtém todos as {@link Reservas} ordenados pela data de início.
	 * 
	 */
	@Post
	@Path("reserva/getReservas")
	public void getReservas() {
		try {
			List<Reserva> reservas = reservaBusiness.getAll();
			result.use(Results.json()).withoutRoot().from(reservas).include("sala", "local", "cidade", "estado").serialize();
		} catch (EstadoException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}
	
	/**
	 * Obtém todos os {@link Estado} ordenados pela descrição.
	 * 
	 */
	@Post
	@Path("reserva/getEstados")
	public void getEstados() {
		try {
			List<Estado> estados = estadoBusiness.getAll();
			result.use(Results.json()).withoutRoot().from(estados).serialize();
		} catch (EstadoException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}

	/**
	 * Obtém todas as {@link Cidade}s ordenadas pela descrição segundo o {@link Estado} informado.
	 * 
	 * @param estado
	 */
	@Post
	@Consumes
	@Path("reserva/getCidades")
	public void getCidades(Estado estado) {
		try {
			List<Cidade> cidades = cidadeBusiness.getAll(estado);
			result.use(Results.json()).withoutRoot().from(cidades).serialize();
		} catch (CidadeException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}

	/**
	 * Obtém todos os {@link Local}s ordenados pela descrição segundo a {@link Cidade} informada.
	 * 
	 * @param cidade
	 */
	@Post
	@Consumes
	@Path("reserva/getLocais")
	public void getLocais(Cidade cidade) {
		try {
			List<Local> locais = localBusiness.getAll(cidade);
			result.use(Results.json()).withoutRoot().from(locais).serialize();
		} catch (LocalException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}

	/**
	 * Obtém todas as {@link Sala}s ordenadas pela descrição segundo o {@link Local} informado.
	 * 
	 * @param local
	 */
	@Post
	@Consumes
	@Path("reserva/getSalas")
	public void getSalas(Local local) {
		try {
			List<Sala> salas = salaBusiness.getAll(local);
			result.use(Results.json()).withoutRoot().from(salas).serialize();
		} catch (SalaException e) {
			adicionarMensagemErroNegocio(e);
		} catch (Exception e) {
			adicionarMensagemErroInesperado(e);
		}
	}
}