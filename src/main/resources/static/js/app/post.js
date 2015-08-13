var revo = angular.module("revo", []);
revo.controller("partCtrl", ["$rootScope", "$http","$window", function ($scope, $http,$window) {
    $scope.location=document.location.host;
    var message = window.location.search.replace('?', '').replace("post=", '');
    var post = 1;
    if (!isNaN(message)) {
        post = parseInt(message);
    }
    if (isNaN(post))post = 1;

    $http.get('post/view/' + post)
        .success(function (parts) {
            var postinfo;
            if (parts.length > 0) {

                if(  parts[0].post==undefined){
                    $window.location.href = 'index.html';

                }

                postinfo = {
                    'id': parts[0].post.id,
                    'title': parts[0].post.title,
                    'url': parts[0].post.url,
                    'path': parts[0].post.path,
                    'publish': parts[0].post.publish,
                    'description': parts[0].post.description,
                    'date': parts[0].post.date
                };
            if(parts[0].id==null){
                parts=[];
            }
            }
            console.log(postinfo);
            $scope.postinfo = postinfo;

            $scope.parts = parts;
        });

}]);
