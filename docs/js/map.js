//intialize the map
function initialize() {
  var mapOptions = {
    zoom: 13,
    center: new google.maps.LatLng(39.29000, -76.5000)
};

var map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);


// MARKERS
/****************************************************************/

//add a marker1
var marker = new google.maps.Marker({
    position: map.getCenter(),
    map: map,
    icon: 'img/estate/pin.png'
});

//add a marker2
var marker2 = new google.maps.Marker({
    position: new google.maps.LatLng(39.2833, -76.5267),
    map: map,
    icon: 'img/estate/pin.png'
});

//add a marker3
var marker3 = new google.maps.Marker({
    position: new google.maps.LatLng(39.2833, -76.5567),
    map: map,
    icon: 'img/estate/pin.png'
});

//add a marker4
var marker4 = new google.maps.Marker({
    position: new google.maps.LatLng(39.3133, -76.5597),
    map: map,
    icon: 'img/estate/pin.png'
});

//add a marker5
var marker5 = new google.maps.Marker({
    position: new google.maps.LatLng(39.2933, -76.5650),
    map: map,
    icon: 'img/estate/pin.png'
});

//add a marker6
var marker6 = new google.maps.Marker({
    position: new google.maps.LatLng(39.2723, -76.5400),
    map: map,
    icon: 'img/estate/pin.png'
});



// INFO BOXES
/****************************************************************/

//show info box for marker1
var contentString = '<div class="info-box"><img src="img/estate/img-3.jpg" style="max-width:100%; margin-bottom:10px;" alt="" /><h4>587 Smith Avenue</h4><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque in ultrices metus' + 
                    ' sit amet [...]</p><a href="r-property-detail.html" class="btn btn-red">VIEW DETAILS</a><br/></div>';

var infowindow = new google.maps.InfoWindow({ content: contentString });

google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(map,marker);
  });


//show info box for marker2
google.maps.event.addListener(marker2, 'click', function() {
    infowindow.open(map,marker2);
  });

//show info box for marker3
google.maps.event.addListener(marker3, 'click', function() {
    infowindow.open(map,marker3);
  });

//show info box for marker4
google.maps.event.addListener(marker4, 'click', function() {
    infowindow.open(map,marker4);
  });

//show info box for marker5
google.maps.event.addListener(marker5, 'click', function() {
    infowindow.open(map,marker5);
  });

//show info box for marker6
google.maps.event.addListener(marker6, 'click', function() {
    infowindow.open(map,marker6);
  });

}

google.maps.event.addDomListener(window, 'load', initialize);