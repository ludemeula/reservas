/* 
 * security.js
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */

var security = angular.module('security',['ngCookies']);

/** 
 * Implementação de 'provider' de configuração do modulo 'security'.  
 */
security.provider('$securityConfig',[function() {

	  var interceptUrls = {};
	  var notAuthorizedUrl = undefined;
	  var notAuthenticatedUrl = undefined;
	  var credentialNameStored = undefined;

	  var redirectAuthenticatedUrl = true;

	  var authHttpEvents = {
		   401: 'notAuthenticated', 
		   403: 'notAuthorized',
		   419: 'sessionTimeout', 
		   440: 'sessionTimeout'
	  }

	  /**
	   * Adiciona a url a ser interceptada associando com suas respectivas permissões de acesso.
	   * 
	   * @param url
	   * @param params { urls: define a URL ou o conjunto de URLs que deverão ser interceptadas., 
	   * 				 accessRoles: define quais os privilégios que o usuário deverá possuir para acessar a url informada. }
	   */
	  this.addInterceptUrl = function(url, params) {

		  if (angular.isDefined(params) && angular.isDefined(params.urls)) {
				  if(angular.isArray(params.urls)) {

					  angular.forEach(params.urls, function(url) {
						  setInterceptUrls(url, params.accessRoles);
					  });

				  } else {
					  setInterceptUrls(params.urls, params.accessRoles);
				  }

		  } else {
			  var accessRoles = angular.isDefined(params) ? params.accessRoles : undefined;
			  setInterceptUrls(url, accessRoles);
		  }

		  return url;
	  }

	  /** 
	   * @param url the interceptUrls to set
	   * @param accessRoles the accessRoles to set in interceptUrls map
	   */
	  var setInterceptUrls = function(url, accessRoles) {
		  interceptUrls[url] = angular.isDefined(accessRoles) ? accessRoles : [];
	  };

	  /** 
	   * @param value the redirectAuthenticatedUrl to set
	   */
	  this.setRedirectAuthenticatedUrl = function(value) {
		  redirectAuthenticatedUrl = value;
	  };

	  /** 
	   * @returns the interceptUrl
	   */
	  this.isRedirectAuthenticatedUrl = function() {
		  return redirectAuthenticatedUrl;
	  };

	  /** 
	   * @param url the notAuthenticatedUrl to set
	   */
	  this.setNotAuthenticatedUrl = function(url) {
		  notAuthenticatedUrl = url;
		  return this;
	  }

	  /** 
       * @returns the notAuthenticatedUrl
	   */
	  this.getNotAuthenticatedUrl = function() {
		  return notAuthenticatedUrl;
	  }

	  /**
	   * @param url the notAuthenticatedUrl to set
	   */
	  this.setNotAuthorizedUrl = function(url) {
		  notAuthorizedUrl = url;
		  return this;
	  }

	  /** 
       * @returns the notAuthenticatedUrl
	   */
	  this.getNotAuthorizedUrl = function() {
		  return notAuthorizedUrl;
	  }

	  /** 
	   * @param options the authHttpEvents to set
       * @returns {*}
	   */
	  this.setAuthHttpEvents = function(options) {
		  angular.extend(authHttpEvents, options);
		  return this;
	  }

	  /** 
       * @returns the authHttpEvents
	   */
	  this.getAuthHttpEvents = function() {
		   return authHttpEvents;
	  }

	  /** 
	   * @param name the credentialNameStored to set
       * @returns {*}
	   */
	  this.setCredentialNameStored = function(name) {
		  credentialNameStored = name;
		  return this;
	  }

	  /** 
       * @returns the credentialNameStored
	   */
	  this.getCredentialNameStored = function() {
		  return credentialNameStored;
	  }

	  /** 
	   * @returns the interceptUrls
	   */
	  this.getInterceptUrls = function() {
		  return interceptUrls;
	  }

	  /** 
	   * Método obrigatório na implementação do 'provider'. 
	   */
	  this.$get = function() {
	    return this;
	  };

}]);

 /** 
  * Configuração do módulo de Segurança. 
  */
security.config(['$httpProvider', function ($httpProvider) {
	 	/*
	 	 * Injeta a instância de 'AutorizacaoInterceptor' ao '$httpProvider', 
	 	 * interceptando todas as requisições http realizadas na aplicação.
	 	 */
	    $httpProvider.interceptors.push(['$injector', function ($injector) {
	            return $injector.get('AutorizacaoInterceptor');
	        }
	    ]);
 }]);

/** Ponto de inicialização do módulo de Segurança. */
security.run(['$rootScope', '$location', '$securityConfig', '$q', 'Credential', function($rootScope, $location, $securityConfig, $q, credential) {

	/** 
	 * Verifica se a url solicitada tem acesso Limitado a Usuários Autenticados na aplicação. 
	 */
	$rootScope.$on('$locationChangeStart', function (event, newUrl, oldUrl) {	
    	var newPath = $location.path();
    	var search  = $location.search();

		var $deferValidUser = $q.defer();

    	/* O evento 'validUser' é invocado verificando se o Usuário está autenticado na aplicação. */
    	$rootScope.$broadcast('validUser', $deferValidUser);

    	$deferValidUser.promise.then(function() {
    		if(angular.isDefined($securityConfig.getInterceptUrls()[$location.path()])) {
    			/* O evento 'findUserRoles' é invocado caso os papeis do Usuário não esteja disponível. */
        		if(credential.getUserRoles().length == 0) {
        			$rootScope.$broadcast('findUserRoles', credential);
        		}

        		var urlRoles = $securityConfig.getInterceptUrls()[$location.path()];

        		/* O evento 'validAccess' é invocado para verificar se o Usuário tem permissão 
        		 * para acessar a 'url' solicitada, segundo os Roles especificado. */
            	if(angular.isDefined(urlRoles) && urlRoles.length > 0) {

            		var $deferValidAccess = $q.defer();
            		$rootScope.$broadcast('validAccess', $deferValidAccess, angular.copy($securityConfig.getInterceptUrls()[$location.path()]));
            		$deferValidAccess.promise.then(function() {}, function() {
            			/* Caso o Usuário não tenha permissão a página será redirecionada. */
            			$location.path($securityConfig.getNotAuthorizedUrl());
                	});
            	}
    		}

    	}, function() {
    		/**
    		 * Verifica que a url requisitada é diferente da URL configurada para usuários não autenticados. 
    		 * Caso verdadeiro a aplicação será redirecionada para a URL $securityConfig.notAuthenticatedUrl, juntamente com todos os parâmetros
    		 * existentes na URL atual.
    		 **/
    		if (!angular.equals($securityConfig.getNotAuthenticatedUrl(), newPath)) {
    			var params = {};
        		if ($securityConfig.isRedirectAuthenticatedUrl()) {
        			params['url'] = newPath;

    	    		if (angular.isDefined(search)) {
    	    			angular.forEach(search, function(value, key) {
    	    				params[key] = value;
    	    			});
    	    		}
        		}

        		/* Caso o Usuário não esteja Autenticado na aplicação, a 'Credencial' será inativada. */
        		credential.invalidate();

        		/* Caso a Url solicitada tenha permissões especificas, a página será redirecionada. */
        		$location.path($securityConfig.getNotAuthenticatedUrl()).search(params);
    		}
    	});
    });

 }]);
 
 /** 
  * Implementação de 'interceptor' referente a requisições Http. 
  */
security.factory('AutorizacaoInterceptor', ['$rootScope','$q', '$securityConfig', 'Credential', function ($rootScope, $q, $securityConfig, credential) {
	    return {
	    	request : function (config) {	    		
	             config.headers['Authorization'] = credential.getToken();
	             return config || $q.when(config);
	         },

	    	responseError : function (response) {
	    		/**
	    		 * Intercepta todas as erros de que possuem códigos relacionados com segurança.
	    		 * 
	    		 * @param data
	    		 * @param status
	    		 * @param credential
	    		 * 
	    		 */
	    		$rootScope.$broadcast($securityConfig.getAuthHttpEvents()[response.status], credential);

	    		/** 
	    		 * 	Caso os status sejam relacionados com segurança, o data receberá 
	    		 *  undefined, para que o mecanismo de mensagem não o aprensete. 
	    		 */
	    		if($securityConfig.getAuthHttpEvents()[response.status]) {
			        response.data = undefined;
	    		}

		        return $q.reject(response);
	        }
	    };
 }]);

/** 
 * Implementação 'service', que representa a Credential do Usuário no Sistema. 
 */
security.service('Credential', ['$rootScope', '$cookieStore', '$securityConfig', function ($rootScope, $cookieStore, $securityConfig) {
	    var credential = this;
	    var credentialDetail = undefined;
	    var credentialStored = $cookieStore.get($securityConfig.getCredentialNameStored());

	    if(angular.isDefined(credentialStored)) {
	    	credentialDetail = credentialStored;
	    }

	    /** 
	     * 	Inicializa a credencial com os dados informado.
	     *
	     * 	@param userName
	     *  @param token
	     *  
	     */
	    this.init = function (userName, token) {
	    	credential.init(userName, token, undefined);
	    };

	    /** 
	     * 	Inicializa a credencial com os dados informado.
	     *
	     * 	@param userName
	     *  @param token
	     *  @param userRoles
	     *  
	     */
	    this.init = function (userName, token, userRoles) {
	    	credentialDetail = {};
	    	credentialDetail.token = token;
	    	credentialDetail.userName = userName;

	    	/* Adiciona o 'credentialDetail' no Cookie sem os papeis, 
	    	 * para evitar o estouro do limite de memória do cookie */
	        $cookieStore.put($securityConfig.getCredentialNameStored(), credentialDetail);

	        /* Caso os papeis do Usuário sejam informados, serão adicionados ao 'credentialDetail'. 
	         * Caso os papeis não sejam informados o evento 'findUserRoles' será disparado. */
	        if(angular.isDefined(userRoles) && userRoles.length > 0) {
	        	credentialDetail.userRoles = userRoles;
	        } else {
	        	$rootScope.$broadcast("findUserRoles", credential);
	        }

	        /* O evento 'initCredential' será disparado sempre que a credência for inicializada. */
	        $rootScope.$broadcast("initCredential", credential);
	    };

	    /** 
	     * Invalida a credencial do Usuário. 
	     */
	    this.invalidate = function () {
	    	credentialDetail = undefined;
	        $cookieStore.remove($securityConfig.getCredentialNameStored());

	        /* O evento 'invalidateCredential' será disparado sempre que a credência for invalidada. */
	        $rootScope.$broadcast("invalidateCredential");
	    };

	    /** 
		 * @returns the active
		 */
	    this.isActive = function() {
	    	return angular.isDefined(credentialDetail);
	    }

	    /** 
		 * @returns the token
		 */
	    this.getToken = function() {
	    	return credential.isActive() && angular.isDefined(credentialDetail.token) ? credentialDetail.token : '' ; 
	    }

	    /** 
	     * @param userRoles the userRoles to set
	     */
	    this.setUserRoles = function(userRoles) {
	    	credentialDetail.userRoles = angular.extend([], userRoles);
	    }

	    /** 
		 * @returns the userRoles
		 */
	    this.getUserRoles = function() {
	    	return credential.isActive() && angular.isDefined(credentialDetail.userRoles) ? credentialDetail.userRoles : []; 
	    }

	    /** 
		 * @returns the userName
		 */
	    this.getUserName = function() {
	    	return credential.isActive() && angular.isDefined(credentialDetail.userName) ? credentialDetail.userName : ''; 
	    }

	    return credential;
	}]);

/**
 * Implementação 'service', que proverá informações referente a Credêncial do Usuário, 
 * e a gestão de Permissão do Usuário.
 */
security.service('$security', ['Credential', function(credential){

	/**
	 * Verifica se o usuário tem algum dos papeis informados, caso um o mais
	 * papeis sejam encontrados o retorno será verdadeiro.
	 * 
	 * @param roles
	 * 
	 */
	this.hasRoles = function(roles) {
		 var authorize = false;
		 
		 /* Caso o atributo 'roles' sejam um array.  */
		 if(angular.isArray(roles) && roles.length > 0) {
			 angular.forEach(roles, function(role){			 
				 if(credential.getUserRoles().indexOf(role) != -1) {
					 authorize = true ;
				 }
			 });
		 } else {
			 /* Caso o atributo 'roles' seja um objeto.  */
			 authorize = credential.getUserRoles().indexOf(roles) != -1;
		 }
		 return authorize;
	 };

	 /**
	  * Verifica se tem Usuário válido.
	  */
	 this.hasUserValid = function() {
		return credential.isActive();
	 }

	 /**
	  * Retorna o nome do Usuário vigente.
	  */
	 this.currentUserName = function() {
		 return credential.getUserName();
	 }

}]);