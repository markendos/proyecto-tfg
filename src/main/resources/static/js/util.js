$(document).ready(function () {
    const requiredFields = $(":input[required]");
    let requiredLabels = $(requiredFields).parent().prev('label');
    if (requiredFields.length > requiredLabels.length) {
        requiredLabels = $(requiredFields).prev('label');
    }
    $(requiredLabels).append("<span class='text-danger'>*</span>");
    if (requiredFields.length > 0) {
        $($(requiredFields).get(-1)).after("<small>(<strong class='text-danger'>*</strong>)" + requiredMessage + "</small>");
    }
    $(".alert").append($("<i onclick='closeAlert(this)' class='fas fa-window-close'></i>"));
});
$(document).ajaxStart(function () {
    showSpinner();
});
$(document).ajaxStop(function () {
    hideSpinner();
    $(".alert").append($("<i onclick='closeAlert(this)' class='fas fa-window-close'></i>"));
});

// Function to hide the Loading Wrapper
function hideSpinner() {
    $('#load-wrapper').addClass("d-none");
}

// Function to show the Loading Wrapper
function showSpinner() {
    $('#load-wrapper').removeClass("d-none");
}

function closeAlert(element) {
    $(element).closest('.alert').fadeOut('fast');
}


