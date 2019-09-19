/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var stringQuery=new Array();
$(function(){
    if(stringQuery.length===0){
        if(window.location.search.split('?').length>1){
            var params=window.location.search.split('?')[1].split('&');
            for(var i=0;i<params.length;i++){
              var keyparam=params[i].split('=')[0];
              var valueparam=decodeURIComponent(params[i].split('=')[1]);
              stringQuery[keyparam]=valueparam;
            }
            
        }
    }
    if(stringQuery["prenom"]!==null && stringQuery["nom"]!==null){
        var data="<b> Bonjour : "+stringQuery["prenom"]+" "+stringQuery["nom"]+"</b>";
        $("#lblUser").html(data);
    }
});