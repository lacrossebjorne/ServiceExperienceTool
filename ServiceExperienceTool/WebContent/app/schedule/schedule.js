'use strict';
angular.module('schedule', ['ngRoute', 'ngResource', 'ngMaterial'])

	.config(['$routeProvider', function ($routeProvider) {
		$routeProvider.when('/schedule', {
			templateUrl: 'app/schedule/schema.html',
			controller: 'ScheduleController'
			// kan ladda in data också som objekt till controllern.
			//resolve innan routingen.
		});
	}])

	.config(function ($provide) {
		$provide.value('schedule.data', [{
			events: getEvents,
			resources: getResources,
			options: getOptions
		}])
	});
function getResources() {
	return [{
		id: 1,
		title: 'Kött',
		type1: 10,
		type2: 55,
		children: [{
			id: '1a',
			title: 'Arne Anka'
		}, {
				id: '1b',
				title: 'Erik Svensson'
			}, {
				id: '1c',
				title: 'Berit Berg'
			},]
	}, {
			id: 2,
			title: 'Varmkök',
			type1: 10,
			type2: 55,
			children: [{
				id: '2a',
				title: 'Niklas Eriksson'
			}, {
					id: '2b',
					title: 'Annika Dahl'
				}, {
					id: '2c',
					title: 'Pernilla Eriksson'
				},]
		}, {
			id: 3,
			title: 'Kallis',
			type1: 10,
			type2: 55,
			children: [{
				id: '3a',
				title: 'Hannes Göransson'
			}, {
					id: '3b',
					title: 'Erik Svensson'
				}, {
					id: '3c',
					title: 'Angelica Näsman'
				},]
		}, {
			id: 4,
			title: 'Servis',
			type1: 10,
			type2: 55,
			children: [{
				id: '4a',
				title: 'Anna Hansson'
			}, {
					id: '4b',
					title: 'Lisa Gullbrandsen'
				}, {
					id: '4c',
					title: 'Erika Olofsdotter'
				},]
		},];
}
function getEvents() {
	return [
		{
			id: 1,
			title: 'Arne',
			start: '2016-05-27T10:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '1a'
		}, {
			id: 2,
			title: 'Erik Svensson',
			start: '2016-05-27T10:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '1b'
		}, {
			id: 3,
			title: 'Berit Berg',
			start: '2016-05-27T10:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '1c'
		}, {
			id: 4,
			title: 'Niklas Eriksson',
			start: '2016-05-27T09:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '2a'
		}, {
			id: 5,
			title: 'Annika Dahl',
			start: '2016-05-27T14:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '2b'
		}, {
			id: 6,
			title: 'Pernilla Eriksson',
			start: '2016-05-27T14:00:00Z',
			end: '2016-05-27T21:00:00Z',
			resourceId: '2c'
		},

		{
			id: 7,
			title: 'Hannes Göransson',
			start: '2016-05-26T10:00:00Z',
			end: '2016-05-26T21:00:00Z',
			resourceId: '3a'
		}, {
			id: 8,
			title: 'Erik Svensson',
			start: '2016-05-26T12:00:00Z',
			end: '2016-05-26T21:00:00Z',
			resourceId: '3b'
		}, {
			id: 9,
			title: 'Angelica Näsman',
			start: '2016-05-26T12:00:00Z',
			end: '2016-05-26T21:00:00Z',
			resourceId: '3c'
		}, {
			id: 10,
			title: 'Anna Hansson',
			start: '2016-05-27T13:00:00Z',
			end: '2016-05-27T23:00:00Z',
			resourceId: '4a'
		}, {
			id: 11,
			title: 'Lisa Gullbrandsen',
			start: '2016-05-27T13:00:00Z',
			end: '2016-05-27T23:00:00Z',
			resourceId: '4b'
		}, {
			id: 12,
			title: 'Erika Olofsdotter',
			start: '2016-05-27T13:00:00Z',
			end: '2016-05-27T23:00:00Z',
			resourceId: '4c'
		},];
}
function getOptions() {
	return [{
		weekNumbers: true,
		defaultView: 'timelineDay',
		firstDay: 1,
		aspectRatio: 1,
		resourceAreaWidth: "20%",
		minTime: "06:00:00",
		maxTime: "23:00:00",
		events: getEvents(),
		resources: getResources(),
		schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
		header: {
			left: 'title',
			center: 'timelineDay, agendaWeek, month',
			right: 'today prev,next'
		},
	}];
}

// $.ajax({
//  url: 'http://www.w3schools.com/website/customers_mysql.php',
//  type: 'GET',
//  success: function(data){
//   console.log(JSON.parse(data));
//  }    
// });
