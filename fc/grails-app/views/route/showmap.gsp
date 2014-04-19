<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>${message(code:'fc.route.map')}</title>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
        <meta charset="utf-8">
        <style>
            html, body, #map-canvas {
                height: 100%;
                margin: 0px;
                padding: 0px
            }
            .labels {
                 color: red;
                 font-family: "Arial";
                 font-size: 12px;
                 font-weight: bold;
                 text-align: center;
                 width: 80px;     
                 white-space: nowrap;
            }
        </style>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script src="${createLinkTo(dir:'js',file:'markerwithlabel.js')}"></script>
        <script>
            var map;
            function initialize() {
              var map_options = {
            	mapTypeId: google.maps.MapTypeId.TERRAIN,
                zoom: 10,
                center: new google.maps.LatLng(${routeInstance.CenterLatitudeMath()},${routeInstance.CenterLongitudeMath()})
              };
              map = new google.maps.Map(document.getElementById('map-canvas'),map_options);
              
              <g:set var="last_coordroute_instance" value="${null}"/>
              <g:each var="coordroute_instance" in="${CoordRoute.findAllByRoute(routeInstance,[sort:'id'])}" status="i">
                  <g:if test="${last_coordroute_instance}">
                      var coords = [
                        new google.maps.LatLng(${last_coordroute_instance.latMath()},${last_coordroute_instance.lonMath()}),
                        new google.maps.LatLng(${coordroute_instance.latMath()},${coordroute_instance.lonMath()})
                      ];
                      var gate_symbol_start = {
                        path: 'M -5,0 5,0',
                        strokeColor: '#FF0000',
                        strokeWeight: 2
                      };
                      var gate_symbol = {
                        path: 'M -5,0 5,0',
                        <g:if test="${coordroute_instance.type == CoordType.SECRET}">
                          strokeColor: '#0000FF',
                        </g:if>
                        <g:else>
                          strokeColor: '#FF0000',
                        </g:else>
                        strokeWeight: 2
                      };
                      var legs = new google.maps.Polyline({
                        path: coords,
                        icons: [
                          <g:if test="${last_coordroute_instance.type == CoordType.TO}">
                            {
                              icon: gate_symbol_start,
                              offset: '0%'
                            }
                          </g:if>
                          <g:if test="${last_coordroute_instance.type == CoordType.SP}">
	                        {
	                          icon: gate_symbol_start,
	                          offset: '0%'
	                        },
	                      </g:if>
	                        <g:if test="${last_coordroute_instance.type != CoordType.TO}">
	                          {
	                            icon: gate_symbol,
	                            offset: '100%'
	                          }
                          </g:if>
                        ],
                        geodesic: true,
                        strokeColor: '#FF0000',
                        strokeOpacity: 1.0,
                        strokeWeight: 2
                      });
                      legs.setMap(map);
                  </g:if>
                  var marker = new MarkerWithLabel({
                      position: new google.maps.LatLng(${coordroute_instance.latMath()},${coordroute_instance.lonMath()}),
                      draggable: false,
                      raiseOnDrag: false,
                      map: map,
                      title: '${coordroute_instance.titleMap()}',
                      labelContent: "${coordroute_instance.titleShortMap()}",
                      labelAnchor: new google.maps.Point(40, 60),
                      labelClass: "labels", // the CSS class for the label
                      labelStyle: {opacity: 0.75}
                  });
                  <g:set var="last_coordroute_instance" value="${coordroute_instance}"/>
              </g:each>
              
            }
            google.maps.event.addDomListener(window, 'load', initialize);
        </script>
    </head>
    <body>
        <div id="map-canvas"/>
    </body>
</html>