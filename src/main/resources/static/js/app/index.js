var revo = angular.module("revo", []);
revo.controller("postCtrl", ["$rootScope", "$http", function ($scope, $http) {
    $scope.location=document.location.host;
    $http.get(document.location.origin + '/post/count')
        .success(function (count) {
            var current = 1;
            var message = window.location.search.replace('?', '').replace("page=", '');
            if (!isNaN(message)) {
                current = parseInt(message);
            }
            if (current > count)current = count;
            if (current < 1)current = 1;
            if (isNaN(current))current = 1;

            $http.get(document.location.origin + '/post/view?page=' + current)
                .success(function (post) {
                    $scope.post = post;
                });


            $scope.count = count;
            var limit = 4;
            var pages = Math.ceil(count / limit);

            var apper = 5;
            var fp = parseInt(apper / 2), tp = parseInt(apper / 2);
            var pagesNum = [];
            if (current <= fp) {
                if (pages > apper) {
                    tp += fp - current + 1;
                    if (current == 1) fp = 0;
                    else fp = apper - tp - 1;
                } else {
                    tp = pages - current;
                    fp = pages - tp - 1;
                }
            } else if (pages - (parseInt(apper / 2)) < current) {

                tp = pages - current;
                if (pages > apper)
                    fp = apper - tp - 1;
                else {
                    fp = pages - tp - 1;
                }
            }
            for (var i = current - fp; i < tp + current + 1; i++) {
                if (i == current) {
                    pagesNum.push({num: i, currentpage: true});
                }
                else  pagesNum.push({num: i, currentpage: false});
            }
            $scope.pagesNum = pagesNum;
            $scope.pagesNumFirst = 1;
            $scope.pagesNumLast = pages;


        });


}]);
