'use strict';

var appInstalled = angular.module('myApp.createteam', ['ngRoute']);

appInstalled
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
        .when('/modifyTeam', {
            templateUrl: 'components/createteam/create-team.html',
            controller: 'CreateTeamCtrl'
        })
        .when('/modifyTeam/:id', {
             templateUrl: 'components/createteam/create-team.html',
             controller: 'CreateTeamCtrl'
        });
    }])

    .controller('CreateTeamCtrl', function (teamModifyFactory, $routeParams, $location) {
        var self = this;
        self.result = "";
        self.editmode = false;
        if($routeParams.id !== undefined){
            self.editmode = true;
            self.team = teamModifyFactory.getTeam($routeParams.id).then(
                function (resp){
                     self.team = resp.data;
            })
        }
        self.send = function () {
            teamModifyFactory.saveTeam(self.team.uuid, self.team.name)
                .then(function (data, status, headers, config) {
                    self.result = data;
                    $location.path( "/adminview" );
                })
        };
    });