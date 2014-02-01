/**
 * 
 */

	$(document).ready(function() {
		$.ajax({
			url : "http://172.245.128.152:8080/xg/one"
		}).then(function(data) {
			$('.addy').append(data.addy);			
			$('.name').append(data.name);			
		});
	});