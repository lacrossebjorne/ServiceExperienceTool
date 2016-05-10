/*
'use strict';

angular.module('newspublish')

.factory('NewsPublishService', ['$resource', 'app.paths', function($resource, paths) {
  return $resource(paths.api + "newsServlet", { action: 'publishNews' }, {
    save: { method: 'POST',
            transformRequest: formDataObject,
            headers: {'Content-Type': undefined, enctype:'multipart/form-data' },
            timeout: 2000
    }
  });

  function formDataObject (data) {
    var fd = new FormData();
    angular.forEach(data, function(value, key) {
        fd.append(key, value);
    });
    return fd;
}
}]);
*/
