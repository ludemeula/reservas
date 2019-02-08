/*
 * directives.js
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */

/** 
 * Solução para o warning: 'Deprecation warning: moment construction falls back to js Date.'. 
 */
moment.createFromInputFallback = function(config) { config._d = new Date(config._i); };

var directives = angular.module('directives',['ngTable']);

/** Configuração do módulo de diretivas da aplicação. */
directives.config(['$httpProvider', function ($httpProvider) {
	
	 /** 
	  * Implementação de 'interceptor' para obter a quantidades total de requisições 
	  * ocorridas nas iterações ocorridas na aplicação. 
	  * 
	  * Importante - O loader da aplicação não está usando a implementação de 
	  * 		     modal do bootstrap para evitar o bug de escroll, mas o 
	  * 			 estilo está sendo adotado e invocado com o Jquery.
	  */
	$httpProvider.interceptors.push(function ($q, $rootScope) {
		var count = 0;
		
        return {
            request: function (config) {
            	if(count <= 0) {
            		$('.loader').show();
            	}
            	++count;
                return config;
            },
            requestError: function (request) {
            	if(!(--count)) {
            		$('.loader').hide();
            	}
                return $q.reject(request);
            },
            response: function (response) {
            	if ((--count) === 0) {
            		$('.loader').hide();
            	}
                return response;
            },
            responseError: function (response) {
            	if(!(--count)) {
            		$('.loader').hide();
            	}
                return $q.reject(response);
            }
        };
    });
	
}]);

/** 
 * Realiza o carregamento dos templates das directivas. 
 */
directives.run(['$templateCache', function($templateCache) {

	/** Template referente Modal Loader. */
	$templateCache.put('modalLoader.html', '<div class="loader modal" >'
										 	+ '<div class="modal-backdrop  in" style="min-height: 100%;"></div>'
										 	+ '<div class="modal-dialog">'
										 		+ '<div class="modal-content">' 
										 			+ '<div class="modal-header text-center"><h5 class="modal-title">Aguarde</h5></div>'
										 			+ '<div class="modal-body">'
										 				+ '<div class="row row-mg-1 text-center"><img src="resources/img/ajax-loader.gif"></div>'
									 				+ '</div>'
								 				+ '</div>'
							 				+ '</div>'
						 				+ '</div>');
	
	$('body').append($templateCache.get('modalLoader.html'));
	
	/** Solução para manter o paginador da tabela dentro do tfoot. */
	$templateCache.put('emptyExternalPagination.html', '');
	
	/** Paginador personalizado para ngTable. */
	$templateCache.put('pagination.html', '<div class="pagination-control" ><ul class="pagination">'
					 + '<li data-ng-class="{disabled: !page.active}" data-ng-repeat="page in pages" data-ng-switch="page.type">'
					 + '<a data-ng-switch-when="prev" data-ng-click="params.page(page.number)" href="">&laquo;</a>'
					 + '<a data-ng-switch-when="first" data-ng-click="params.page(page.number)" href=""><span data-ng-bind="page.number"></span></a>'
					 + '<a data-ng-switch-when="page" data-ng-click="params.page(page.number)" href=""><span data-ng-bind="page.number"></span></a>'
					 + '<a data-ng-switch-when="more" data-ng-click="params.page(page.number)" href="">&#8230;</a>'
					 + '<a data-ng-switch-when="last" data-ng-click="params.page(page.number)" href=""><span data-ng-bind="page.number"></span></a>'
					 + '<a data-ng-switch-when="next" data-ng-click="params.page(page.number)" href="">&raquo;</a>'
					 + '</li></ul><div class="total-register" >Total Registros: {{params.total()}}</div></div>');

}]);

/**
 * Implementação de 'directive', para manipulação de mascaras em campos de CPF, 
 * com o apoio da biblioteca jQuery maskedinput.
 */
directives.directive('ngCpf',[function(){
	 return {
		 priority: 1,
		 restrinct: 'A',
		 require: 'ngModel',
		 link: function($scope, element, attrs, ngModel){
			// Adiciona a mascara de cpf ao elemento.
			$(element).mask('999.999.999-99');
	
			/** Sobrescrita do método $formatters referente a diretiva ngModel. */
			ngModel.$formatters.unshift(function(value) {
				var cpfFormatado = '';
				var cpf = '';
				
				// Calculo para definir o total de zeros a esquerda que deverão ser adicionados ao cpf.
				var numZeros = value != null && value.length > 0 ? 11 - value.length : 0;
				
				for(var i = 1; i <= numZeros; i++) {
					cpf+='0';
			    }
				
				cpf = cpf + value;
				
				// Adiciona a mascara ao cpf.
				for(var i = 0; i < cpf.length; i++){
					cpfFormatado += cpf.charAt(i);
					cpfFormatado += i == 2 || i == 5 ? '.' : '';
					cpfFormatado += i == 8 ? '-' : '';
				}
		        return cpfFormatado;
		    });
			
			/** Sobrescrita do método $parsers referente a diretiva ngModel. */
			ngModel.$parsers.unshift(function(value) {
				 var cpf = value.replace(/[^\d]+/g,'');
			     return cpf.trim().length == 0 ? undefined : cpf;
			});
		 }
	 }
}]);

/**
 * Implementação de 'directive', para manipulação de mascaras em campos de Telefone, 
 * com o apoio da biblioteca jQuery maskedinput.
 */
directives.directive('ngTelefone',[function(){
	 return {
		 priority: 1,
		 restrinct: 'A',
		 require: 'ngModel',
		 link: function($scope, element, attrs, ngModel){
			// Adiciona a mascara de cnpj ao elemento.
			$(element).mask('(99) 9999-9999?9');
			
			/** Sobrescrita do método $formatters referente a diretiva ngModel. */
			ngModel.$formatters.unshift(function(value) {
				var telefone = '';
				
				if(value && value.length > 0) {
					// Adiciona a mascara ao Telefone.
					for(var i = 0; i < value.length; i++){
						telefone += i == 0 ? '(' : '';
						telefone += value.charAt(i);
						telefone += i == 1 ? ') ' : '';
						telefone += i == 5 ? '-' : '';
					}
				}
		        return telefone;
		    });
			
			/** Sobrescrita do método $parsers referente a diretiva ngModel. */
			ngModel.$parsers.unshift(function(value) {
				 var telefone = value.replace(/[^\d]+/g,'');
			     return telefone.trim().length == 0 ? undefined : telefone;
			});
		 }
	 }
}]);

/**
 * Implementação de 'directive', para permitir apenas Números em campos de entrada.
 */
directives.directive('ngNumber',[function(){
	 return {
		 priority: 1,
		 restrinct: 'A',
		 require: 'ngModel',
		 link: function($scope, element, attrs, ngModel){
			element.keypress(function(event){
				/* Verifica se o código ANSSII da tecla pressionada está associado a alguma letra ou caracter especial, 
				 * caso seja verdadeiro o retorno será falso. */
				 if ((event.which < 48 || event.which > 57) && (event.which != 8 && event.which != 0)) {
				     return false;
				 }
				 return true;
			});
			
			/** Sobrescrita do método $parsers referente a diretiva ngModel. */
			ngModel.$parsers.unshift(function(value) {
			     return angular.isDefined(value) && angular.equals(value.trim().length, 0) ? undefined : value;
			});
		 }
	 }
}]);

/**
 * Implementação de 'directive', para a geração do componente ngCalendar.
 */
directives.directive('ngCalendar',['$compile', function($compile){
	 return {
		 restrinct: 'A',
		 template: '<div class="input-group date" >'
         		 + '<div data-ng-transclude ></div>'
         		 + '<span class="input-group-addon">'
         		 + '<span class="glyphicon glyphicon-calendar"></span>'
         		 + '</span></div>',
         replace:true,
		 transclude: true,
		 link: function($scope, element, attrs) {
		
			 $(element).datetimepicker({
				   	format: 'L',   
				   	locale: 'pt-br',
				   minDate:	'01/01/1753',
				useCurrent: false,
				 showClear: true,
			 disabledHours: false,
			 });
			
		 }
	 }
}]);

/**
 * Implementação de 'directive', para a geração do componente ngCalendar.
 */
directives.directive('ngDate',[function(){
	 return {
		 restrinct: 'A',
		 require: 'ngModel',
		 link: function($scope, element, attrs, ngModel) {
			 	var current = undefined;
			 
			 	$(element).mask("99/99/9999");
			 	
			 	/**
			 	 * Sobrescrita de método associado a diretiva 'ngModel'.
			 	 * 
			 	 * @param value 
			 	 */
			 	ngModel.$formatters.unshift(function(value) {
			 		
			 		if(angular.isDefined(ngModel.$modelValue) && angular.equals(moment().format('DD/MM/YYYY'), moment.utc(ngModel.$modelValue).format('DD/MM/YYYY'))) {
			 			ngModel.$viewValue = moment().format('DD/MM/YYYY');
			 			ngModel.$render();
			 		}

			  	    return value;
			 	});
			 
			 	/**
			 	 * Sobrescrita de método associado a diretiva 'ngModel'.
			 	 * 
			 	 * @param value 
			 	 */
			  	ngModel.$formatters.push(function(value) {

			  		if(!angular.isDefined(current)) {
			  			current = angular.copy(value);
			  		}

			  	    return moment.utc(value).format('DD/MM/YYYY');
			  	});
		
			 	/**
			 	 * Sobrescrita de método associado a diretiva 'ngModel'.
			 	 * 
			 	 * @param value 
			 	 */
			  	ngModel.$parsers.push(function(value) {
			  		
			  		var context = moment(value, ['DD/MM/YYYY', 'YYYY-MM-DD'], 'pt-br');      
			  		
			  		if(context.isValid()) {
			  			if(context.isAfter('01/01/1753')) {
			  				
			  				/** Insere a hora zerada na instância corrente. 
			  				 *  Caso a hora não seja zerada manualmente o moment considera a hora atual. */
			  				context.hour(00);
			  				context.minute(00);
			  				context.second(00);
			  				context.millisecond(000);
			  				
			  				current = context.toJSON();
			  			}
			  		}
			 
			  	    return current;
			  	});
			  	
			  	/**
			  	 * Solução para o probglema da seleção da corrente.
			  	 */
				$(element).on('blur', function(){
					
					/* 
					 * Caso o valor do element seja vazio o modelo receberá o valor "undefined". 
					 */
					if(element.val().trim().length == 0) {
						ngModel.$setViewValue(undefined);
					
					/* 
					 * Caso o valor do element seja igual a data vigente e o valor 
					 * corrente for "undefined" o modelo receberá a data vigente. 
					 */
					} else if(!angular.isDefined(current) && angular.equals(moment().format('DD/MM/YYYY'), element.val())) {
			  			$scope.$apply(function() {
					  		ngModel.$setViewValue(moment().format('YYYY-MM-DD'));
		                });
			  		}
			  	});
		
		 }
	 }
}]);

 /**
  * Implementação de 'factory', para o controle do componente ngPopup.
  */
directives.factory('$popup', [function(){
		 
		 /**
		  * Retorna o id formatado removendo os espaços e adicionando '#' caso não seja informado.
		  * 
		  * @param $id
		  */
		 var getIdFormatado = function($id) {
			 return $id.trim().substring(1,0) === '#' ? $id.trim() : '#' + $id.trim();
		 }
		 
		 /** 
		  * Abre o Popup por Id. 
		  * 
		  * @param $id
		  */
		 this.show = function($id) {
			 if($id){
				 var id = getIdFormatado($id);
				 $(id).modal({ backdrop: 'static', keyboard: false });
			  }
		 };
		 
		 /** 
		  * Fecha o Popup por Id. 
		  * 
		  * @param $id
		  */
		 this.hide = function($id) {
			 if($id){
				 var id = getIdFormatado($id);
				 $(id).modal('hide');
			 }
		 };
		 
		 /** 
		  * Realiza ação alternada, caso o modal esteja fechado será aberto, caso o modal esteja aberto será fechado.  
		  * 
		  * @param $id
		  */
		 this.toggle = function($id) {
			 if($id){
				 var id = getIdFormatado($id);
				 $(id).modal('toggle');
			 }
		 };
		 
		 return this;
 }]);
 
 /**
  * Implementação de 'directive', para a geração do componente ngPopup.
  */
directives.directive('ngPopup',[function(){
	 return {
		 restrinct: 'A',
		 template: function(element, attrs){
			 	var html = '<div class="modal fade" role="modal" ><div class="modal-dialog" ';
			 	if(attrs.width){
			 		var width = attrs.width.trim().substring(attrs.width.trim().length - 2, attrs.width.trim().length) === 'px' ? attrs.width : attrs.width + 'px';
			 		width = $(window).width() < width.substring(0, width.length - 2) ? '94%' : width;
			 		html = html + 'style="width:' + width + '"';
			 	}
		        return html + '><div class="modal-content" ng-transclude ></div></div></div>';
		 },
		 replace: true,
		 transclude: true
	 }
 }]);
 
 /**
  * Implementação de 'directive', para a geração do componente ngShowPopup.
  */
directives.directive('ngShowPopup',[function(){
	 return {
		 restrinct: 'A',
		 link: function(scope, element, attrs){
			if(attrs.ngShowPopup){
				 var $id =  attrs.ngShowPopup.trim().substring(1,0) === '#' ? attrs.ngShowPopup.trim() : '#' + attrs.ngShowPopup.trim(); 
				 $(element).on('click', function(){
					 $($id).modal({
						  backdrop: 'static',
						  keyboard: false
					 });
				 });
			}
		 }
	 }
 }]);

/**
 * Implementação de 'directive', para a geração do componente ngHidePopup.
 */
directives.directive('ngHidePopup',[function(){
	 return {
		 restrinct: 'A',
		 link: function(scope, element, attrs){
			if(attrs.ngHidePopup){
				 var $id =  attrs.ngHidePopup.trim().substring(1,0) === '#' ? attrs.ngHidePopup.trim() : '#' + attrs.ngHidePopup.trim(); 
				 $(element).on('click', function(){
					 $($id).modal('hide');
				 });
			};
		  }
	 }
}]);


/**
 * Implementação de 'directive', para a geração do componente ngPopover.
 * 
 * @param placement - left, right, top, bottom (default).
 * @param container - #id, .selector, tag (default).
 * 
 */
directives.directive('ngPopover',[function(){
	 return {
		 restrinct: 'A',
		 link: function(scope, element, attrs){
			 $(element).css('display','none');
		 }
	 }
}]);

/**
 * Implementação de 'factory' para manipular os parâmetros necessários na biblioteca ngTable.
 * 
 * === Tabela com Múltiplas seleções. ===
 * 
 * 1 - Criar nova <td> na <table> anotando com a directive 'ngTableCheckbox', informando os atributos (itemVar e itemValue). 
 * 	   Obs: Todos os atributos são obrigatórios. Segue exemplo:
 * 
 * 		<td data-ng-table-checkbox="$table" data-item-var="item" data-item-value="item.id" ></td>
 * 
 * 2 - Implementar métodos callback, para a manipulação dos registros selecionados.
 *     Obs: Todos os métodos devem ser implementados. Segue exemplo:
 * 
 * 		$scope.$tableTeste = new $table({getCheckAll: function(data, dataSelects, checkedAll) {
 * 			   angular.forEach(data, function(item) {
 * 				 dataSelects[item.id] = checkedAll;
 * 			   });
 * 		}, getCheckOne: function(item) {
 * 			  $scope.itemsSelect.push(item); 
 * 		}});
 * 
 */
directives.factory('$table', ['$rootScope', '$filter', 'ngTableParams', function($rootScope, $filter, ngTableParams){
	
	var $table = function(options) {
		var settings = angular.extend({page: 1, count: 20, data : [], total : 0, getCheckAll: undefined, getCheckOne: undefined }, options);
		
		this.selects = {};
		this.checkedAll = false;
		
		/** Realiza a invocação do método callback implementado pelo controller, 
		 * para atualização das referências segundo o estado vigente do checkbox.  */
		this.toggleCheckAll = function(checkedAll) {
			settings.getCheckAll(settings.data, this.selects, checkedAll);
		}

		/** Realiza a invocação do método callback implementado pelo controller, 
		 *  para atualizar a referência associado ao estado vigente do checkbox. */
		this.toggleCheckOne = function(value) {
			settings.getCheckOne(value);
		}

		/** 
		 * Verifica que foi definida uma regra para ordenação. 
		 * A ordenação acontecerá caso o Object sorting() estiver definido, e o atributo orderBy não for vazio.
		 * 
		 * @param params
		 */
		var isSorting = function(params) {
			return angular.isDefined(params.sorting()) && angular.isDefined(params.orderBy()) && params.orderBy().length > 0;
		}

		/** Retorna o array de objetos segundo as instaçoes do usuário com a bela (Ordenação e Paginação)*/
		var getData = function($defer, params) {
            if (angular.isArray(settings.data) && angular.isObject(params)) {

            	var data = isSorting(params) ? $filter('orderBy')(settings.data, params.orderBy()) : settings.data;
                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));

            } else {
                $defer.resolve([]);
            }

            return $defer.promise;
	    };
		
	    /** Cria a instância de ngTableParams */
		this.params = new ngTableParams({page: settings.page, count: settings.count}, 
										{$scope: $rootScope.$new(), total: settings.total,  getData: getData });
				
		/** Realiza a atualização da tabela segundo o array informado. */
		this.reload = function(data){
			if(data) {
				settings.data = data;
				this.params.page(1);
				this.params.total(data.length);
			}
			this.params.reload();
		};
		
		/** Adiciona o novo objeto informado no array. */
		this.add = function(object) {
			if(object) {
				settings.data.push(object);
				this.reload(settings.data);
			}
		}
		
		/** Remove o objeto informado no array. */
		this.remove = function(object) {
			if(object) {
				settings.data.splice(settings.data.indexOf(object), 1);
				this.reload(settings.data);
			}
		}
		
		/** Verifica se o checkbox referente a ação 'Todos' será renderizado. 
		 * O checkbox será renderizado caso o método callback 'getCheckAll' for implementado. */
		this.isRenderCheckAll = function() {
			return angular.isDefined(settings.getCheckAll);
		}
		
		/** Verifica se os checkboxes serão renderizados. 
		 * O checkboxes serão renderizados caso o método callback 'getCheckOne' for implementado. */
		this.isRenderCheckOne = function() {
			return angular.isDefined(settings.getCheckAll);
		}
	};

	return $table;

}]);