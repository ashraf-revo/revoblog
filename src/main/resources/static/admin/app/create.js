var revo = angular.module("revo", []);
revo.controller("partCtrl", ["$rootScope", "$http", "$window", function ($scope, $http, $window) {
    $scope.location=document.location.host;

    $http.get(window.location.origin + '/csrf').success(function (data) {

        $http.defaults.headers.common['X-Csrf-Token'] = data.token;

    });
    $scope.submit = function () {
        var formData = new FormData($("#postId")[0]);
        $.ajax({
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $http.defaults.headers.common['X-Csrf-Token']
                )
            },
            url: 'CloneRemote',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            enctype: 'multipart/form-data',
            processData: false,
            success: function (data) {
                if (data != -1)
                    $window.location.href = 'edit.html?post=' + data;
                else  $window.location.href = 'index.html';

            }
        });
    }

}]);
