/*
 * message.js
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */

var message = angular.module('message', []);

/** Inicializa o módulo de Mensagem adicionando ao DOM o container. */
message.run(['$message', function($message){
	$message.addContainer();
}]);

/** Configurações padrões associada ao mecanismo de mensagem. */
message.constant('MessageSettings', {container: 'body', showDelay: 300, slideDown: 500, hideDelay: 6000, slideUp: 700 });

/** Configurações padrões associada ao mecanismo de confirmação. */
message.constant('ConfirmSettings', { width: '400px', actionOk: null, labelOk : 'Ok', actionYes: null, labelYes : 'Sim', actionNo: null, labelNo : 'Não', labelConfirm : 'Confirmação', msg : '' });

/** Resource de descrições que serão apresentadas na aplicação. */
message.constant('Resource', {});

/** Configurações padrão de mascaras que serão utilizadas na aplicação. */
message.constant('Mask', {maskChar: '#', protocolo : '#############-#'});

/** Implementação de 'provider' de configuração do modulo 'message'.  */
message.provider('$messageConfig',['Resource', 'MessageSettings', 'ConfirmSettings', function(resource, messageSettings, confirmSettings) {

	  /** 
	   * Recebe o valor novo resource de mensagem. 
	   * 
	   * @param newResource
	   */
	  this.setResource = function(newResource) {
		  angular.copy(newResource, resource);
	  };

	  /** 
	   * Recebe as configurações globais de aprensetação de mensagens associada a aplicação.
	   * 
	   * @param {container: 'body'(default), '#id' or '.seletor',
	   * 		   showDelay: 300, 
	   * 		   slideDown: 500, 
	   * 		   hideDelay: 6000, 
	   * 		   slideUp: 700 }
	   */
	  this.setMessageSettings = function(options) {
		  messageSettings = angular.extend(messageSettings, options);
	  };

	  /** 
	   * Recebe as configurações globais de aprensetação de mensagens associada a aplicação.
	   * 
	   * @param { width: '400px',
	   * 		  actionOk: null, 
	   * 		  labelOk : 'Ok', 
	   * 		  actionYes: null, 
	   * 		  labelYes : 'Sim',
	   * 		  actionNo: null, 
	   * 		  labelNo : 'Não', 
	   * 		  labelConfirm : 'Confirmação', 
	   * 		  msg : '' }
	   */
	  this.setConfirmSettings = function(options) {
		  confirmSettings = angular.extend(confirmSettings, options);
	  };

	  /** 
	   * Método obrigatório na implementação do 'provider'. 
	   */
	  this.$get = function() {
	    return this;
	  };

}]);

/** 
 * Implementação de 'service' para prover as mensagens de interação com o Usuário da aplicação. 
 */
message.service('$message', ['$filter', 'MessageSettings', function($filter, messageSettings){
	var msgCurrents = [];

	/** Adiciona o Container necessário para apresentação de mensagens ao Body da aplicação. */
	this.addContainer = function() {
		$(messageSettings.container).append('<div class="message" ng-messages ></div>');
	}

	/** 
	 * Adiciona mensagem de Sucesso. 
	 * 
	 * @param msg or {msg, params, htmlBinding}
	 */
	this.addMsgSuccess = function(msg) {
		addMsg(msg, 'alert-success');
	};

	/** 
	 * Adiciona mensagem de Informação. 
	 * 
	 * @param msg or {msg, params, htmlBinding}
	 */
	this.addMsgInf = function(msg) {
		addMsg(msg, 'alert-info')
	};

	/** 
	 * Adiciona mensagem de Alerta. 
	 * 
	 * @param msg or {msg, params, htmlBinding}
	 */
	this.addMsgWarning = function(msg) {
		addMsg(msg, 'alert-warning');
	};

	/** 
	 * Adiciona mensagem de Erro. 
	 * 
	 * @param msg or {msg, params, htmlBinding}
	 */
	this.addMsgDanger = function(msg) {
		addMsg(msg, 'alert-danger');
	};

	/** 
	 * Gera o elemento que apresentará a mensagem, e adiciona o elemento ao container. 
	 *
	 * @param msg or {msg, params, htmlBinding}
	 * @param type
	 */
	var gerarMsg = function(msg, type) {
		msgCurrents.push(msg);

		var id = 'msg_' + Math.floor(Math.random() * 7000);
		var html = '<div id="' + id + '" class="message-content alert '+ type + ' ">'+ msg + '</div>';

		$('div[ng-messages]').append(html);
		$('#' + id).delay(messageSettings.showDelay).slideDown(messageSettings.slideDown);
		$('#' + id).delay(messageSettings.hideDelay).slideUp(messageSettings.slideUp, function(){
			msgCurrents.splice(msgCurrents.indexOf($(this).text()), 1);
			$(this).remove();
	    });
	}

	/** 
	 * Adiciona a mensagem segundo o type (alert-success, alert-info, alert-warning e alert-danger), informado.  
	 * 
	 * @param msg
	 * @param type
	 */
	var addMsg = function(msg, type) {

		if(msg) {
			var valor = undefined;

			/* Caso o parâmetro 'msg' seja uma String. */
			if(angular.isString(msg)) {
				valor = $filter('i18n')(msg);
				valor = valor != undefined ? valor : msg;

			} else {
				/* Caso o parâmetro 'msg' esteja parametrizado. */
				valor = $filter('i18n')(msg.msg, msg.params, msg.htmlBinding);
				valor = valor = valor != undefined ? valor : msg.msg;
			}

			if(msgCurrents.indexOf(valor) == -1) {
				gerarMsg(valor, type);
			}

		}
	};	

	return this;

}]);

/** Implementação de 'factory' para prover os modais de confirmação de interação com o Usuário da aplicação. */
message.factory('$confirm', ['$filter', 'ConfirmSettings', function($filter, confirmSettings){

	/**
	 * 	Classe javascript de implementação de '$confirm'. 
	 * 
	 * 	@constructor options
	 */
	var $confirm = function(options) {
		var settings = angular.extend(confirmSettings, options);

		/** 
		 * Adiciona o componente 'Confirm' ao DOM. Segundo as especificações informadas na assinatura do método. 
		 * 
		 * @param options
		 */
		this.addConfirm = function(options) {
			var optionsConfirm = angular.extend(settings, options);
			var id = 'confirm_' + Math.floor(Math.random() * 7000);
			var valor = undefined;

			/* Caso o parâmetro 'msg' seja uma String. */
			if(angular.isString(optionsConfirm.msg)) {
				valor = $filter('i18n')(optionsConfirm.msg);
				valor = valor != undefined ? valor : optionsConfirm.msg ;

			} else {
				/* Caso o parâmetro 'msg' esteja parametrizado. */
				valor = $filter('i18n')(optionsConfirm.msg.msg, optionsConfirm.msg.params, optionsConfirm.msg.htmlBinding);
				valor = valor = valor != undefined ? valor :  msg.msg;
			}

			generateConfirm(id, optionsConfirm, valor);
			addClickAction(id, optionsConfirm);

			$('#' + id).modal({ backdrop: 'static', keyboard: false });
		}

		/** 
		 * Gera a estrutura html do componente 'Confirm'.
		 * 
		 *  @param id
		 *  @param options
		 *  @param msg
		 */
		var generateConfirm = function(id, options, msg) {

			var width = $(window).width() < options.width.substring(0, options.width.length - 2) ? '94%' : options.width;

			var html = '<div id="' + id + '" class="modal fade" ><div class="confirm-dialog modal-dialog" style="width:' + width + '" >';
			html+= '<div class="modal-content"><div class="modal-header"><h5 class="modal-title">' + options.labelConfirm + '</h5></div><div class="modal-body">';
			html+= '<div class="row" ><div class="col-lg-12 text-center" ><span>' + msg +'</span></div></div>';
			html+= '<div class="row row-mg-2" ><div class="col-lg-12  text-center" >';

			if(options.actionOk) {
				html+= '<a class="btn btn-default btn-ok" role="button"  ><span>' + options.labelOk + '</span></a>';
			} else {
				html+= '<a class="btn btn-default btn-yes" role="button" style="margin-right: 10px;"  ><span>' + options.labelYes + '</span></a>';
				html+= '<a class="btn btn-default btn-no" role="button" style="margin-left: 10px;" ><span>' + options.labelNo + '</span></a>';
			}
			html+= '</div></div></div></div></div></div>';
			$('body').append(html);
		};

		/** 
		 * Adiciona a ação 'Click' aos botões apresentados no componente 'Confirm'. 
		 * 
		 * @param id
		 * @param options
		 */
		var addClickAction = function(id, options) {
			var element = $('#' + id);
			if(options.actionOk) {
				element.find('.btn-ok').click(executCallback(element, options.actionOk));
			} else {
				element.find('.btn-yes').click(executCallback(element, options.actionYes));
				element.find('.btn-no').click(executCallback(element, options.actionNo));
			}
		};

		/** 
		 * Execulta o método Callback informado e remove o componente de 'Confirm' do DOM. 
		 * 
		 * @param element
		 * @param callback
		 */
		var executCallback = function(element, callback) {
			 return function () {

				 if(callback) { callback(); }
				 $(element).modal('hide').remove();

				 // Solução encontrada para bug do 'twitter bootstrap 3' ao remover o Confirm da DOM.
				 $('body').removeAttr('style');
				 $('body').removeClass();
			 };
		};
	};

	return  $confirm;
}]);

/** 
 * Implementação de 'filter' customizado para prover o i18n na aplicação. 
 */
message.filter('i18n', [ '$sce', 'Resource', function($sce, resource) {

	 /**
	  * Retorna a descrição segundo os parâmetros informados.
	  * 
	  * @param chave
	  * @param params
	  * @param htmlBinding
	  * 
	  */
	 return function (chave, params, htmlBinding) {
		 var description = resource[chave];
		 if(params){
			 if(angular.isArray(params) && params.length > 0){
				 for(var i = 0; i < params.length; i++) {
					 description = description.replace(new RegExp('\\{' + i + '\\}', 'g'), params[i]);
				 }
			 } else if(params.toString().trim().length > 0) {
				 description = description.replace(new RegExp('\\{0}', 'g'), params);
			 }
		 }

		 return htmlBinding ? $sce.trustAsHtml(description) : description;
	 };
}]);

/**
 * Implementação de 'filter' customizado para formatação de valores que possuem máscaras para apresentação.
 * Observação: A máscaras foi construida para substituir qualquer caracter, tanto números ou letras, 
 * exceto o caracter especial '#', ele é utilizado como parâmetro na mascara.
 * 
 */
message.filter("maskValue", ['Mask', function(Mask) {

	 /**
	  * Retorna o valor formatado conforme a mascara informada.
	  * 
	  * @param value
	  * @param mascara
	  * 
	  */
	  return function (value, mascara) {
			var valorFormatado = String(value);

			if (angular.isDefined(valorFormatado) && angular.isDefined(mascara)) {
				for (var i=0; i <= mascara.length; i++) {

					/* Se o caracter atual da mascara informada for diferente do caracter Mask.char, ele é adicionada ao valor formatado. */
					if (!angular.equals(Mask.maskChar, mascara.charAt(i))) {
						if (i > 0 ) {
							valorFormatado = valorFormatado.substring(0, i) + mascara.charAt(i) + valorFormatado.substring(i, valorFormatado.length +1);
						} else {
							valorFormatado = mascara.charAt(i) + valorFormatado.substring(i, valorFormatado.length +1);
						}
					}
				}
			}

			return valorFormatado;
		}
}]);