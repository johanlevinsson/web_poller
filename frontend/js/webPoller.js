var app = angular.module('webPoller', []);
app.controller('webPollerController', function($scope, $http, $interval) {
    const API_PATH = 'http://localhost:8080/api/'
    const SECONDS_BETWEEN_UPDATES = 10;
    $scope.entries = [];
    $scope.url = '';
    $scope.name = '';
    $scope.updatingEntry = {
        url: '',
        name: '',
        id: 0
    }
    
    $scope.getStatuses = function() {
        $http.get(API_PATH + "all").then(
          function (response) {
            $scope.entries = response.data;
          }
        );
    };

    $scope.add = function (url, name) {
        const urlObject = {
            "url": url,
            "name": name
        };

        $http.get(API_PATH + "add?urlObject=" + encodeURIComponent(JSON.stringify(urlObject))).then(
            function (response) {
              $scope.entries = response.data;
            }
        );

        $scope.url = '';
        $scope.name = '';
    }

    $scope.delete = function (entry) {
        $http.post(API_PATH + "remove/" + entry.id).then(
            function (response) {
              $scope.entries = response.data;
            }
        );
    }

    $scope.update = function (entry) {
        const updateUrlObject = {
            "url": entry.url,
            "name": entry.name,
            "id": entry.id
        };

        $http.get(API_PATH + "update?updateUrlObject=" + encodeURIComponent(JSON.stringify(updateUrlObject))).then(
            function (response) {
              $scope.entries = response.data;
            }
        );

        clearUpdatingParameters();
    }

    $scope.getCss = function (entry) {
        return entry.code === 200 ? "{'background-color':'lightgreen'}" : "{'background-color':'lightcoral'}";
    }

    $scope.getStatusText = function (entry) {
        return entry.code === 200 ? "OK" : "FAIL";
    }

    $scope.isValidUrl = function (url) {
        return (/^https?:\/\/[0-9a-z]+\.[-_0-9a-z]+\.[0-9a-z]+$/).test(url);
    }

    $scope.toggleUpdateDialog = function (entry) {
        if ($scope.updatingEntry.id === entry.id) {
            clearUpdatingParameters();
        } else {
            $scope.updatingEntry = {
                url: entry.url,
                name: entry.name,
                id: entry.id
            }
        }
    }

    function clearUpdatingParameters() {
        $scope.updatingEntry = {
            url: '',
            name: '',
            id: 0
        }
    }

    $scope.getStatuses();
    $interval($scope.getStatuses, SECONDS_BETWEEN_UPDATES * 1000);
});