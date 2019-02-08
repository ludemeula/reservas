/*
 * ReservaBusiness.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.labtrans.entities.Cidade;
import br.com.labtrans.entities.Estado;
import br.com.labtrans.entities.Local;
import br.com.labtrans.entities.Reserva;
import br.com.labtrans.exception.EstadoException;
import br.com.labtrans.exception.ReservaException;
import br.com.labtrans.exception.ReservaLabelCode;
import br.com.labtrans.exception.ReservaMessageCode;
import br.com.labtrans.persistence.ReservaDAO;
import br.com.labtrans.persistence.exception.DAOException;
import br.com.labtrans.util.Util;

/**
 * Classe responsável por implementar as regras de negócio relativas à classe {@link Reserva}.
 *
 * @author Ludemeula Fernandes
 */
@Named
@Dependent
@Transactional(rollbackOn = ReservaException.class)
public class ReservaBusiness {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private ReservaDAO reservaDAO;

	/**
	 * Obtém todas as {@link Reserva}s ordenadas pela data de início.
	 * 
	 * @return
	 * @throws EstadoException
	 */
	public List<Reserva> getAll() throws EstadoException {
		try {
			List<Reserva> reservas = reservaDAO.getAll();
			
			if (!Util.isEmpty(reservas)) {
				for (Reserva reserva : reservas) {
					reserva.setLocal(reserva.getSala().getLocal());
					reserva.setCidade(reserva.getSala().getLocal().getCidade());
					reserva.setEstado(reserva.getSala().getLocal().getCidade().getEstado());
				}
			}
			
			return reservas;
		} catch (DAOException e) {
			String msg = "Falha ao obter todos as reservas ordenadas pela data de início.";
			logger.error(msg, e);
			throw new EstadoException(msg, e);
		}
	}
	
	/**
	 * Salva a {@link Reserva} informada.
	 * 
	 * @param reserva
	 * @param estado
	 * @param cidade
	 * @param local
	 * @throws ReservaException
	 */
	public void salvar(Reserva reserva, Estado estado, Cidade cidade, Local local) throws ReservaException {
		try {
			validarCamposObrigatorios(reserva, estado, cidade, local);
			validarData(reserva);
			validarReserva(reserva);
			reservaDAO.persistir(reserva);
		} catch (DAOException e) {
			String msg = "Falha ao salvar a reserva informada.";
			logger.error(msg, e);
			throw new ReservaException(msg, e);
		}
	}
	
	/**
	 * Exclui a {@link Reserva} informada.
	 * 
	 * @param reserva
	 * @throws ReservaException
	 */
	public void excluir(Reserva reserva) throws ReservaException {
		try {
			reservaDAO.excluir(reserva);
		} catch (DAOException e) {
			String msg = "Falha ao excluir a reserva informada.";
			logger.error(msg, e);
			throw new ReservaException(msg, e);
		}
	}

	/**
	 * Valida se os campos definidos como obrigatórios foram informados.
	 * 
	 * @param reserva
	 * @param estado
	 * @param cidade
	 * @param local
	 * @throws ReservaException
	 */
	private void validarCamposObrigatorios(Reserva reserva, Estado estado, Cidade cidade, Local local) throws ReservaException {
		List<ReservaLabelCode> campos = new ArrayList<ReservaLabelCode>();

		if (estado == null) {
			campos.add(ReservaLabelCode.LABEL_ESTADO);
		} if (cidade == null) {
			campos.add(ReservaLabelCode.LABEL_CIDADE);
		} if (local == null) {
			campos.add(ReservaLabelCode.LABEL_LOCAL);
		} if (reserva.getSala() == null) {
			campos.add(ReservaLabelCode.LABEL_SALA);
		} if (reserva.getDataInicio() == null) {
			campos.add(ReservaLabelCode.LABEL_DATA_INICIO);
		} if (reserva.getDataFim() == null) {
			campos.add(ReservaLabelCode.LABEL_DATA_FIM);
		} if (reserva.isIncluirCafe() && reserva.getQuantidadePessoas() == null) {
			campos.add(ReservaLabelCode.LABEL_QTD_PESSOAS);
		} if (Util.isEmpty(reserva.getResponsavel())) {
			campos.add(ReservaLabelCode.LABEL_RESPONSAVEL);
		}

		if (campos.size() > 0) {
			throw new ReservaException(ReservaMessageCode.CAMPOS_OBRIGATORIOS_NAO_INFORMADOS, campos);
		}
	}
	
	/**
	 * Valida se a data de início é maior ou igual a data atual,
	 * e se a data de fim é maior ou igual que a data de fim.
	 * 
	 * @param reserva
	 * @throws ReservaException
	 */
	private void validarData(Reserva reserva) throws ReservaException {
		if (reserva.getDataInicio().before(new Date())) {
			throw new ReservaException(ReservaMessageCode.DATA_INICIO_MENOR_DATA_ATUAL);
		}
		
		if (reserva.getDataFim().before(reserva.getDataInicio())) {
			throw new ReservaException(ReservaMessageCode.DATA_FIM_MENOR_DATA_INICIO);
		}
	}
	
	/**
	 * Verifica se não há choque de horários na mesma sala.
	 * 
	 * @param reserva
	 * @throws ReservaException
	 * @throws DAOException 
	 */
	private void validarReserva(Reserva reserva) throws ReservaException, DAOException {
		List<Reserva> reservas = reservaDAO.getAll(reserva);
		if (!Util.isEmpty(reservas)) {
			throw new ReservaException(ReservaMessageCode.RESERVA_EXISTENTE_PERIODO);
		}
	}
}