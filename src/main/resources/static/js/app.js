var revo = angular.module("revo", []);
revo.run(["$rootScope", "$http", function ($scope, $http) {
    $scope.name = 'revo';
    $http.get('http://localhost:8080/person')
        .success(function (persons) {
            $scope.persons = persons;
        });
    $scope.count = function () {
        var c = 0;
        angular.forEach($scope.persons, function (value) {
            if (!value.online) {
                c++;
            }
        })
        return c;
    }
}]);

