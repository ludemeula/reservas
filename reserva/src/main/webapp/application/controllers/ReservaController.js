/*
 * ReservaController.js
 * 
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
angular.module('app').controller('ReservaController', ['$scope','$http', '$message', '$table', '$acaoSistema', '$confirm' , function($scope, $http, $message, $table, $acaoSistema, $confirm) {	
	$scope.$tableReservas = new $table();
	
	/* Inicializa o atributo ação. */
	$scope.acao = new $acaoSistema();
	
	/* Inicializa o $modelConfirm. */
	var $modelConfirm = new $confirm();
	
	/**
	 * Obtém todos os {@link Estado} ordenados pela descrição.
	 * 
	 */
	var obterEstados = function() {
		$http.post('reserva/getEstados').success(function(data) {
			$scope.estados = data;
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}
	
	/**
	 * Obtém todas as reservas ordenandas pela data de início.
	 * 
	 */
	var listar = function() {
		$http.post('reserva/getReservas').success(function(data) {
			$scope.$tableReservas.reload(data);
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}
	
	/**
	 * Inicializa as ações padrões do caso de uso.
	 * 
	 */
	var init = function() {
		$scope.acao.acaoListar();
		listar();
	}
	
	init();
	
	/**
	 * Altera a ação do sistema para 'inclusão'.
	 * 
	 */
	$scope.novaReserva = function() {
		obterEstados();
		$scope.acao.acaoIncluir();
		$scope.reserva = {};
		$scope.reserva.incluirCafe = false;
	}
	
	/**
	 * Cancela as ações do usuário e envia para a tela principal.
	 * 
	 */
	$scope.cancelar = function() {
		init();
	}

	/**
	 * Salva a reserva informada.
	 * 
	 */
	$scope.salvar = function() {
		$http.post('reserva/salvar', {reserva : $scope.reserva, estado : $scope.reserva.estado, cidade : $scope.reserva.cidade, local : $scope.reserva.local}).success(function(data) {
			init();
			$message.addMsgInf(data);
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}

	/**
	 * Altera a ação do sistema para 'alterar'.
	 * 
	 * @param reserva
	 */
	$scope.alterar = function(reserva) {
		$scope.reserva = angular.copy(reserva);
		$scope.acao.acaoAlterar();
		
		obterEstados();
		$scope.carregarCidade();
		$scope.carregarLocal();
		$scope.carregarSala();
	}
	
	/**
	 * Exclui a reserva selecionada.
	 * 
	 * @param incentivo
	 */
	$scope.excluir = function(reserva) {
		$modelConfirm.addConfirm({msg : 'MSG-005', actionYes : function() {
			$http.post('reserva/excluir', reserva).success(function(data) {
				init();
				$message.addMsgInf(data);
			}).error(function(data) {
				$message.addMsgDanger(data);
			});
			$scope.$apply();
		}});
	};
	
	/**
	 * Carrega as cidades segundo o estado selecionado.
	 * 
	 */
	$scope.carregarCidade = function() {
		$http.post('reserva/getCidades', $scope.reserva.estado).success(function(data) {
			$scope.cidades = data;
			$scope.locais = {};
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}

	/**
	 * Carrega os locais segundo a cidade selecionada.
	 * 
	 */
	$scope.carregarLocal = function() {
		$http.post('reserva/getLocais', $scope.reserva.cidade).success(function(data) {
			$scope.locais = data;
			$scope.salas = {};
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}
	
	/**
	 * Carrega as salas segundo o local selecionado.
	 * 
	 */
	$scope.carregarSala = function() {
		$http.post('reserva/getSalas', $scope.reserva.local).success(function(data) {
			$scope.salas = data;
		}).error(function(data){
			$message.addMsgDanger(data);
		});
	}
}]);