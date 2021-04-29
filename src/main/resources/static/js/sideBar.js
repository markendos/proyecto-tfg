const minSize = 576;
const maxSize = 768;
$(document).ready(() => {
    window.addEventListener('resize', reportWindowSize);

    $('#sidebarCollapse').on('click', function () {
        const isCollapsed = $('#sidebar').hasClass('collapsed');
        $('#sidebar').toggleClass('collapsed');
        if(isCollapsed) {
            $('#sidebar').show(500);
        } else {
            $('#sidebar').hide(500);
        }
    });
});

function reportWindowSize() {
    const isCollapsed = $('#sidebar').hasClass('collapsed');
    if(window.innerWidth < minSize && !isCollapsed) {
        $('#sidebar').addClass('collapsed');
        $('#sidebar').hide(500);
    } else if(window.innerWidth >= maxSize) {
        $('#sidebar').removeClass('collapsed');
        $('#sidebar').show(500);
    }
}
