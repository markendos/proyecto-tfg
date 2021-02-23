const minSize = 576;
$(document).ready(() => {
    window.addEventListener('resize', reportWindowSize);

    if(window.innerWidth < minSize) {
        $('#sidebar').hide();
    }

    $('#sidebarCollapse').on('click', function () {
        const isCollapsed = $('#sidebar').hasClass('active');
        $('#sidebar').toggleClass('active');
        if(window.innerWidth < minSize && !isCollapsed) {
            $('#content').addClass('fixedMenu');
            $('#sidebar').addClass('fixed');
            $('#sidebar').show();
        } else if (window.innerWidth < minSize) {
            $('#sidebar').hide();
            $('#content').removeClass('fixedMenu');
            $('#sidebar').removeClass('fixed');
        }
    });
});

function reportWindowSize() {
    const isCollapsed = $('#sidebar').hasClass('active');
    if(window.innerWidth < minSize && !isCollapsed) {
        $('#content').addClass('fixedMenu');
        $('#sidebar').addClass('active fixed');
        $('#sidebar').show();
    } else if(window.innerWidth >= minSize) {
        $('#sidebar').removeClass('active fixed');
        $('#content').removeClass('fixedMenu');
    }
}
