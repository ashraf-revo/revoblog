var revo = angular.module("revo", ['fxpicklist']);
revo.controller("partCtrl", ["$rootScope", "$http", "$window", function ($scope, $http, $window) {
    $scope.location=document.location.host;

    $http.get(window.location.origin + '/csrf').success(function (data) {

        $http.defaults.headers.common['X-Csrf-Token'] = data.token;

    });

    var message = window.location.search.replace('?', '').replace("post=", '');
    var post = 1;
    if (!isNaN(message)) {
        post = parseInt(message);
    }
    if (isNaN(post))post = 1;
    $scope.post = post;
    $http.get('post/view/' + post)
        .success(function (parts) {
            var postinfo;
            if (parts.length > 0) {
                if (parts[0].post != undefined)postinfo = {
                    'id': parts[0].post.id,
                    'title': parts[0].post.title,
                    'url': parts[0].post.url,
                    'path': parts[0].post.path,
                    'publish': parts[0].post.publish,
                    'description': parts[0].post.description,
                    'postImg': parts[0].post.postImg,
                    'date': parts[0].post.date
                };

                if (parts[0].id == null) {
                    parts = [];
                }
            }
            $scope.postinfo = postinfo;

            $scope.parts = parts;
        });
    $scope.dataParts = [];

    $scope.submit = function () {
        var formData = new FormData($("#postId")[0]);
        $.ajax({
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $http.defaults.headers.common['X-Csrf-Token']
                )
            },
            url: 'post/edit/' + post,
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            enctype: 'multipart/form-data',
            processData: false,
            success: function () {
                location.reload();
            }
        });
    };
    $scope.delete = function () {
        $http.get('post/delete/' + post).success(function (data) {
            $window.location.href = 'index.html';
        });
    };

    $scope.clone = function () {

        $.ajax({
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $http.defaults.headers.common['X-Csrf-Token']
                )
            },
            url: 'clone/' + post,
            data: $("#postId").serialize(),
            success: function () {
                location.reload();
            }
        });
    }

}]);
revo.controller("picklistCtrl", ["$rootScope", "$http", "$window", function ($scope, $http, $window) {
    $scope.toptions = new Array();
    $scope.tselected = new Array();

    $scope.sublist=function(){

        var formData = new FormData();
        for(var i=0;i<$scope.tselected.length;i++)
        formData.append("parts",$scope.tselected[i].name);

        $.ajax({
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $http.defaults.headers.common['X-Csrf-Token']
                )
            },
            url: 'post/saveingParts/'+$scope.post,
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            enctype: 'multipart/form-data',
            processData: false,
            success: function () {
                location.reload();
            }
        });

    };
    $http.get('parts/displayAll/'+$scope.post ).success(function (data) {
        for (var i = 0; i < data.length; i++) {
            $scope.toptions.push({
                name: data[i].name,
                value: data[i].name,
                index: i
            });
        }


        $http.get('post/view/' + $scope.post).success(function (data) {
            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < $scope.toptions.length; j++) {
                    if ($scope.toptions[j].name == data[i].name) {
                        $scope.tselected.push($scope.toptions[j]);
                    }
                }
            }
        });
    });
}]);