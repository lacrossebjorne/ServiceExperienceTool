// Self invoking function
// Injects jQuery
(function ($) {

    $.extend(window, {
        "Calendar":
        {
            "init": initCalendar,
            "getResources": getResources,
            "getEvents": getEvents
        }
    });

    function initCalendar(initOptions) {

        $(initOptions.elementId).fullCalendar({
            weekNumbers: true,
            defaultView: 'timelineDay',
            firstDay: 1,
            minTime: "09:00:00",
            maxTime: "23:00:00",
            schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
            header:
            {
                left: 'title',
                center: 'timelineDay, timelineWeek, month',
                right: 'today prev,next'
            },

            events: getEvents(initOptions.eventLoadUrl),

            resourceOrder: '-type1,type2',
            resources: getResources(initOptions.resourceLoadUrl),
            eventClick: function (event, jsEvent, view) {
                console.log('eventClick');
                console.log('Event: ', event);
                console.log('jsEvent: ', jsEvent);
                console.log('View: ', view);

                // change the border color just for fun
                $(this).css('border-color', 'red');

                // if (event.url) {
                //     window.open(event.url);
                //     return false;
                // }
            }
        });
    }
    function getResources(url) {
        // $.ajax({
        //  url: 'http://www.w3schools.com/website/customers_mysql.php',
        //  type: 'GET',
        //  success: function(data){
        //   console.log(JSON.parse(data));
        //  }    
        // });
        return [
            {
                id: 1,
                title: 'Kött',
                type1: 10,
                type2: 55,
                children: [
                    { id: '1a', title: 'Arne "the butcher" Anka' },
                    { id: '1b', title: 'Erik "peppar" Svensson' },
                    { id: '1c', title: 'Berit Bergsåkerfjäderfjärt?' },
                ]
            },
            {
                id: 2,
                title: 'Varmkök',
                type1: 10,
                type2: 55,
                children: [
                    { id: '2a', title: 'Niklas Eriksson' },
                    { id: '2b', title: 'Annika Dahl' },
                    { id: '2c', title: 'Pernilla Eriksson' },
                ]
            },
            {
                id: 3,
                title: 'Kallis',
                type1: 10,
                type2: 55,
                children: [
                    { id: '3a', title: 'Hannes Göransson' },
                    { id: '3b', title: 'Erik "peppar" Svensson' },
                    { id: '3c', title: 'Angelica Näsman' },
                ]
            },
            {
                id: 4,
                title: 'Servis',
                type1: 10,
                type2: 55,
                children: [
                    { id: '4a', title: 'Anna Hansson' },
                    { id: '4b', title: 'Lisa Gullbrandsen' },
                    { id: '4c', title: 'Erika Olofsdotter' },
                ]
            },
        ];
    }
    function getEvents(url) {
        return [
            { id: 1, title: 'Tjena', start: '2016-05-13T10:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '1a' },
            { id: 2, title: 'Tjena', start: '2016-05-13T10:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '1b' },
            { id: 3, title: 'Tjena', start: '2016-05-13T10:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '1c' },

            { id: 4, title: 'Tjena', start: '2016-05-13T09:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '2a' },
            { id: 5, title: 'Tjena', start: '2016-05-13T14:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '2b' },
            { id: 6, title: 'Tjena', start: '2016-05-13T14:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '2c' },

            { id: 7, title: 'Tjena', start: '2016-05-13T10:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '3a' },
            { id: 8, title: 'Tjena', start: '2016-05-13T12:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '3b' },
            { id: 9, title: 'Tjena', start: '2016-05-13T12:00:00Z', end: '2016-05-13T21:00:00Z', resourceId: '3c' },

            { id: 10, title: 'Tjena', start: '2016-05-13T13:00:00Z', end: '2016-05-13T23:00:00Z', resourceId: '4a' },
            { id: 11, title: 'Tjena', start: '2016-05-13T13:00:00Z', end: '2016-05-13T23:00:00Z', resourceId: '4b' },
            { id: 12, title: 'Tjena', start: '2016-05-13T13:00:00Z', end: '2016-05-13T23:00:00Z', resourceId: '4c' },
        ];
    }


})(jQuery);
Calendar.init({
    elementId: '#calendar',
    resourceLoadUrl: '',
    eventLoadUrl: ''
});