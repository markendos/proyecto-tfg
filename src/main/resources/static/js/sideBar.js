$(document).ready(() => {
    window.addEventListener('resize', reportWindowSize);
    $('#sidebarCollapse').on('click', function () {
        const isCollapsed = $('#sidebar').hasClass('active');
        $('#sidebar').toggleClass('active');
        if(window.innerWidth < 768 && !isCollapsed) {
            $('#content').addClass('fixedMenu');
        } else {
            $('#content').removeClass('fixedMenu');
        }
    });
});

function reportWindowSize() {
    const isCollapsed = $('#sidebar').hasClass('active');
    if(window.innerWidth < 768 && !isCollapsed) {
        $('#content').addClass('fixedMenu');
        $('#sidebar').addClass('active fixed');
        $('#sidebar').show();
    } else if(window.innerWidth >= 768) {
        if(isCollapsed) {
            $('#sidebar').removeClass('active fixed');
        }
        $('#content').removeClass('fixedMenu');
    }
}
