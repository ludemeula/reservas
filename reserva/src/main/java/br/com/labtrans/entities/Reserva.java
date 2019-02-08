/*
 * Reserva.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entidade que representa a tabela <b>Reserva</b>.
 *
 * @author Ludemeula Fernandes
 */
@Entity
@Table(name = "reserva")
public class Reserva implements Entidade {
	private static final long serialVersionUID = -8934415494177829496L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "dataInicio")
	private Date dataInicio;

	@Column(name = "dataFim")
	private Date dataFim;

	@Column(name = "incluirCafe")
	private boolean incluirCafe;

	@Column(name = "quantidadePessoas")
	private Long quantidadePessoas;
	
	@Column(name = "descricao")
	private String descricao;

	@Column(name = "responsavel")
	private String responsavel;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sala_id")
	private Sala sala;

	@Transient
	private Local local;
	
	@Transient
	private Cidade cidade;
	
	@Transient
	private Estado estado;

	/**
	 * @see br.com.labtrans.entities.Entidade#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the incluirCafe
	 */
	public boolean isIncluirCafe() {
		return incluirCafe;
	}

	/**
	 * @param incluirCafe the incluirCafe to set
	 */
	public void setIncluirCafe(boolean incluirCafe) {
		this.incluirCafe = incluirCafe;
	}

	/**
	 * @return the quantidadePessoas
	 */
	public Long getQuantidadePessoas() {
		return quantidadePessoas;
	}

	/**
	 * @param quantidadePessoas the quantidadePessoas to set
	 */
	public void setQuantidadePessoas(Long quantidadePessoas) {
		this.quantidadePessoas = quantidadePessoas;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the responsavel
	 */
	public String getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the sala
	 */
	public Sala getSala() {
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(Sala sala) {
		this.sala = sala;
	}

	/**
	 * @return the local
	 */
	public Local getLocal() {
		return local;
	}

	/**
	 * @param local the local to set
	 */
	public void setLocal(Local local) {
		this.local = local;
	}

	/**
	 * @return the cidade
	 */
	public Cidade getCidade() {
		return cidade;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the estado
	 */
	public Estado getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}