(function () {
	'use strict';

	angular
		.module('acmeApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('jhi-health', {
			parent: 'admin',
			url: '/health',
			data: {
				roles: ['ADMIN'],
				pageTitle: 'Health Checks'
			},
			views: {
				'content@': {
					templateUrl: 'app/admin/health/health.html',
					controller: 'JhiHealthCheckController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
