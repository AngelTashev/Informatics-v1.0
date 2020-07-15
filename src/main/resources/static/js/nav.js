  $(window).scroll(function() {
    if($(this).scrollTop() > 300) {
        $('.fixed-top').addClass('opaque');
    } else {
        $('.fixed-top').removeClass('opaque');
    }
});

$(window).scroll(function() {
	if ($(this).scrollTop() > 500) {
		$('#img-header').addClass('lower');
	} else {
		$('#img-header').removeClass('lower');
	}
})
