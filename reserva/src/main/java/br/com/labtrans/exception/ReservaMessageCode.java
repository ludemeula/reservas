/*
 * ReservaMessageCode.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.exception;

/**
 * Enum com os código de exceções/mensagens de negócio.
 *
 * @author Ludemeula Fernandes
 */
public enum ReservaMessageCode {
	ERRO_INESPERADO("MSG-001"),
	CAMPOS_OBRIGATORIOS_NAO_INFORMADOS("MSG-002"),
	RESERVA_SALVA_SUCESSO("MSG-003"),
	RESERVA_EXCLUIDA_SUCESSO("MSG-004"),
	DATA_INICIO_MENOR_DATA_ATUAL("MSG-006"),
	DATA_FIM_MENOR_DATA_INICIO("MSG-007"),
	RESERVA_EXISTENTE_PERIODO("MSG-008");
	
	private final String chave;
	
	/**
	 * Construtor da classe.
	 *
	 * @param chave
	 */
	private ReservaMessageCode(final String chave) {
		this.chave = chave;
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return chave;
	}
}