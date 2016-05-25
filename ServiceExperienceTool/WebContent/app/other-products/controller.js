'use strict';

angular.module('other-products')

.controller('OtherProductsController', ['$scope', '$location', 'products.data', 'pushService', function($scope, $location, productsData, pushService) {
    var imgIndex = 0;
    var imgUris = productsData.imgUris;
    var imageSwitch = function() {
        return productsData.imgUris[imgIndex++ % imgUris.length];
    }

    $scope.links = productsData.links;
    $scope.documents = productsData.docs;
    $scope.tabs = productsData.tabs;
    $scope.currentTab = $scope.tabs[0].url;
    $scope.imgA = imageSwitch();

    $scope.onClickTab = function(tab) {
        $scope.currentTab = tab.url;
        $scope.imgA = imageSwitch();
    }

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    }

    $scope.addLink = function(title, url) {
        $scope.links.push({
            title: title,
            url: url
        });
        pushService.broadcast({
            path: $location.absUrl(),
            message: 'Link added: ' + title
        });
        $scope.newLinkTitle = "";
        $scope.newLinkUrl = "";
    }

    $scope.removeLink = function(index) {
        $scope.links.splice(index, 1);
    }

    $scope.addDocument = function(title, url) {
        $scope.documents.push({
            title: title,
            icon: 'fa-file-text-o',
            url: url
        });
        pushService.broadcast({
            path: $location.absUrl(),
            message: 'Document added: ' + title
        });
        $scope.newDocTitle = "";
        $scope.upFile = "";
    }

    $scope.removeDocument = function(index) {
        $scope.documents.splice(index, 1);
    }

    $scope.setFile = function(element) {
        $scope.$apply(function($scope) {
            $scope.upFile = element.files[0].name;
            console.log(element.files[0].name);
        });
    };
}]);