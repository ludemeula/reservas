/*
 * app.js
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */

var app = angular.module('app',['ngRoute', 'ngCookies', 'message', 'security', 'directives', 'validation' ]);

/**
 * Definições de rotas de acesso as páginas da aplicação. 
 */
app.config(['$routeProvider', '$logProvider', '$messageConfigProvider', '$validationProvider', '$securityConfigProvider', function($routeProvider, $logProvider, $messageConfigProvider, $validationProvider, $securityConfigProvider){	
   $logProvider.debugEnabled(false);

   /** Security Config */
   $securityConfigProvider.setCredentialNameStored('recCredentialStored');
   $securityConfigProvider.setNotAuthenticatedUrl('/');
   $securityConfigProvider.setNotAuthorizedUrl('/');

   /** Regra de navegação associada a aplicação. */
   $routeProvider.when($securityConfigProvider.addInterceptUrl('/'), {
       templateUrl: 'partials/principal.html',
   }).when($securityConfigProvider.addInterceptUrl('/agendar'), {
       templateUrl: 'partials/reserva.html',
       controller: 'ReservaController'
   }).otherwise({redirectTo:'/'});


   /** Regras de Validações de Campos */
   $validationProvider.showSuccessMessage = false;

   $validationProvider.setExpression({  
		required: function(value, scope, element, attrs) {

			/* Validação pontual para o componente Checkbox. */
			if (angular.equals(element[0].type, 'checkbox')){
				return element.parent().parent().find('[type=checkbox]').is(':checked');
			}

			/* Validação para campos String. Caso a String não esteja 
			 * undefined mas seu valor seja vazio o retorno será falso. 
			 * 
			 * Obs: Os elementos com mascara geradas pela api jquery.maskedinput, 
			 * quando estão vazios retornam o valor 'und.efi.ned-', gerando inconsistência na validação
			 * de campos obrigatórios, como solução, a validação !angular.equals('und.efi.ned-', value.trim()) foi adicionada.   
			 */
			if(angular.isDefined(value) && angular.isString(value)) {
				return value.trim().length > 0 && !angular.equals('und.efi.ned-', value.trim());
			}

			/* Validação para campos String. Caso a String não esteja 
			 * undefined mas seu valor seja vazio o retorno será falso. */
			if(angular.isDefined(value) && angular.isString(value)) {
				return value.trim().length > 0;
			}

			return angular.isDefined(value);
		}
   });

   $validationProvider.setDefaultMsg({  
	   required: {error: 'Campo de preenchimento obrigat\u00F3rio.', success: '' }
   });

   /** Resource i18n associado a aplicação. */
   $messageConfigProvider.setResource({
	   'LABEL_CAMPOS_OBRIGATORIOS' : '* Campos Obrigatórios',
	   'LABEL_VOLTAR' : 'Voltar',
	   'LABEL_SALVAR' : 'Salvar',
	   'LABEL_AVANCAR' : 'Avançar',
	   'LABEL_INCLUIR' : 'Incluir',
	   'LABEL_CANCELAR' : 'Cancelar',
	   'LABEL_NAO' : 'Não',
	   'LABEL_SIM' : 'Sim',
	   'LABEL_SELECIONE' : 'Selecione',
	   'LABEL_ACOES' : 'Ações',
	   
	   'LABEL_ESTADO' : 'Estado',
	   'LABEL_CIDADE' : 'Cidade',
	   'LABEL_LOCAL' : 'Local',
	   'LABEL_SALA' : 'Sala',
	   'LABEL_RESPONSAVEL' : 'Responsável',
	   'LABEL_PERIODO' : 'Período',
	   'LABEL_DATA_INICIO' : 'Data de Início',
	   'LABEL_DATA_FIM' : 'Data de Fim',
	   'LABEL_A' : 'à',
	   'LABEL_INCLUIR_CAFE' : 'Incluir Café?',
	   'LABEL_QTD_PESSOAS' : 'Quantidade de Pessoas',
	   'LABEL_DESCRICAO' : 'Descrição',
	   'LABEL_EXCLUIR_RESERVA' : 'Excluir Reserva',
	   'LABEL_ALTERAR_RESERVA' : 'Alterar Reserva',
	   
	   'TITLE_LISTA_RESERVAS' : 'Reservas Agendadas',
	   'TITLE_CADASTRO_RESERVAS' : 'Cadastro de Reservas',
	   'TITLE_ALTERACAO_RESERVAS' : 'Alteração de Reservas',
	   
	   'MSG-005' : 'Deseja realmente excluir a reserva selecionada?'
   });
}]);

/** Ponto de inicialização do módulo do modulo Principal. */
app.run(['$rootScope', '$security', '$location', '$http', function($rootScope, $security, $location, $http){

	/* Torna a instância de '$security' publica, permitindo o acesso a informações sobre credencial do Usuário e 
	 * o a gestão de papeis relevante na personalização de view.  */
	$rootScope.$security = $security;


	/** 
	 * Método a será executado quando o evento 'notAuthenticated' for invocado.  
	 * Caso o Usuário não esteja autenticado na aplicação, será redirecionado para página inicial.
	 */
	$rootScope.$on('notAuthenticated', function(event, credential){		
		credential.invalidate();
		$location.path('/');
		
		console.log('aqui')
	});

	/** 
	 * Método a será executado quando o evento 'validAccess' for invocado. Verifica se o Usuário está autenticado e tem permissão para acessar a url informada.
	 */
	$rootScope.$on('validAccess', function(event, $defer, urlRoles){
	
	});

	/** 
	 * Método que será executado quando o evento 'findUserRoles' for invocado. 
	 */
	$rootScope.$on('findUserRoles', function(event, credential){
	
	});

}]);

/**
 * Constantes que representa as possíveis interações do Usuário com o Sistema.
 */
app.constant('ACAO_SISTEMA', {
	   INCLUIR: 'INCLUIR',
	   ALTERAR: 'ALTERAR',
	    LISTAR: 'LISTAR',
	VISUALIZAR: 'VISUALIZAR',
});

/** Implementação de 'factory' para manipulação das interações entre o Usuário e o Sistema. */
app.factory('$acaoSistema', ['ACAO_SISTEMA', function (ACAO_SISTEMA) {

	var acaoSistema = function() {

		var acaoVigente = null;

		/** Altera o valor da ação para Incluir. */
		this.acaoIncluir = function() {
			acaoVigente = ACAO_SISTEMA.INCLUIR;
		};

		/** Altera o valor da ação para Alterar. */
		this.acaoAlterar = function() {
			acaoVigente = ACAO_SISTEMA.ALTERAR;
		}

		/** Altera o valor da ação para Listar. */
		this.acaoListar = function() {
			acaoVigente = ACAO_SISTEMA.LISTAR;
		}

		/** Verifica se ação é referente a 'Incluir'. */
		this.isAcaoIncluir = function() {
			return angular.equals(ACAO_SISTEMA.INCLUIR, acaoVigente); 
		};

		/** Verifica se ação é referente a 'Alterar'. */
		this.isAcaoAlterar = function() {
			return angular.equals(ACAO_SISTEMA.ALTERAR, acaoVigente); 
		}

		/** Verifica se ação é referente a 'Listar'. */
		this.isAcaoListar = function() {
			return angular.equals(ACAO_SISTEMA.LISTAR, acaoVigente);
		}

		this.getAcaoSistema = function() {
			return acaoVigente;
		}

		this.setAcaoSistema = function(acao) {
			acaoVigente = acao;
		}
	}

	return acaoSistema;
}]);

/** Implementação de 'service' de representação de Sessão. */
app.service('Session',['$location', '$routeParams', function($location, $routeParams){

	var sessionMap = {};

	/**
	 * Adiciona o valor informado na sessão e retorna a chave criptografada em base 64.
	 * 
	 * @param value
	 */
	this.put = function(value) {
		var key  = Math.floor(Math.random() * 7000);
		sessionMap[key] = value;
		return $.base64.encode(key);
	}

	/**
	 * Retorna o valor segundo a chave informada.
	 * 
	 * @param key
	 */
	this.get = function(key) {
		var key = $.base64.decode(key);
		return sessionMap[key];
	}

	/**
	 * Verifica se a chave informada existe no session map.
	 * 
	 * @param key
	 */
	this.containsKey = function(key) {
		var key = $.base64.decode(key);
		return angular.isDefined(sessionMap[key]);
	}

	/**
	 * Retorna os parâmetros informados na url segundo as chaves informadas.
	 * 
	 * Exemplo:
	 * 
	 * chaves: ['chave', 'origem'] retorno: {chave: 'abc123', origem: '/oferta'}
	 * 
	 * @param keys
	 */
	this.getUrlParams = function(keys) {
		var params = {};

		if (angular.isDefined(keys)) {
			angular.forEach(keys, function(key){
				params[key] = $location.search()[key];
			});
		} else {
			params = $location.search();
		}

		return params;
	}

	/**
	 * @param params the $location.search to set
	 */
	this.setUrlParams = function(params) {
		$location.search(params)
	}

	/**
	 * Retorna os parâmetros informados de rotas segundo as chaves informadas.
	 * 
	 * Exemplo:
	 * 
	 * chaves: ['chave', 'origem'] retorno: {chave: 'abc123', origem: '/oferta'}
	 * 
	 * @param keys
	 */
	this.getRouteParams = function(keys) {
		var params = {};

		angular.forEach(keys, function(key){
			params[key] = $routeParams[key];
		});

		return params;
	}

	/**
	 * Navega de pagina de acordo com a url informada.
	 * 
	 * @param url
	 * @param params
	 */
	this.navigateTo = function(url, params) {
		if(angular.isDefined(params)) {
			$location.path(url).search(params);
		} else {
			$location.path(url);
		}
	}

	/**
	 * Retorna a url atual.
	 * 
	 * Exemplo:
	 * 
	 * url absoluta: http://example.com/#/some/path?foo=bar&baz=xoxo
	 * retorno: "/some/path?foo=bar&baz=xoxo"
	 * 
	 */
	this.getUrl = function() {
		return $location.path();
	}
}]);