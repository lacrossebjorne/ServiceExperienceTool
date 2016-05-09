'use strict';

angular.module('menu')

.factory('MenuService', ['$resource', 'app.paths',
  function($resource, paths) {
    return $resource(paths.local+"menu/:menuId", {menuId: '@menuId'}, {
    	query: {
            method: 'GET',
            params: {},
            isArray: true
          }
    });
  }
]);

angular.module('menu').factory('Category', ['$resource', 'app.paths',
  function($resource, paths) {
    return $resource(paths.local+"menu/:menuId/category/:categoryId", 
    		{menuId: '@menuId', categoryId: '@categoryId'}, {
    });
  }
]);

angular.module('menu').factory('MenuItem', ['$resource', 'app.paths',
  function($resource, paths) {
	var MenuItem = $resource(paths.local+"menu/:menuId/item/:itemId", 
			{menuId: '@menuId', categoryId: '@itemId'}, {
			});
	return MenuItem;
}
]);