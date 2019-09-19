/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
              console.log("Starting progs");
                var myLatlng = new google.maps.LatLng(-1.293586666, 29.34688566111);
                var myOptions = {
                    zoom: 13,
                    center: myLatlng,
                    mapTypeId: google.maps.MapTypeId.HYBRID
                };
                var map = new google.maps.Map(document.getElementById('map'), myOptions);
                var INTERV=3000;
                var markerStore={};
                
                function getMarkers(){
                    $.get("http://8a370932a26a.sn.mynetname.net:8087/services/providing/kiosques/locations/more/infos",{},
                    function(res,resp){
                        for( var i=0, len=res.length; i<len; i++){
                           if(markerStore.hasOwnProperty(res[i].id)){
                               markerStore[res[i].id].setPosition(new google.maps.LatLng(res[i].position.lat,res[i].position.long));
                           } else{
                               var marker=new google.maps.Marker({
                                   position:new google.maps.LatLng(res[i].position.lat,res[i].position.long),
                                   title:res[i].name,
                                   map:map
                               });
                               markerStore[res[i].id]=marker;
                           }
                        }
                        window.setTimeout(getMarkers(),INTERV);
                    },"json");
                }
            }
 );