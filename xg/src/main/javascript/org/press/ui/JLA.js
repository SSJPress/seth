/**
 * 
 */

$(document).ready(function() {
    $.ajax({
        url: "localhost:8080/xg/json"
    }).then(function(data) {
       $('.name').append(data.name);
       $('.addy').append(data.addy);
    });
});