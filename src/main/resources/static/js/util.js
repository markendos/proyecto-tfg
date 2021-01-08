$(document).ready(function () {
    var requiredFields = $(":input[required]");
    var requiredLabels = $(requiredFields).parent().prev('label');
    if (requiredFields.length > requiredLabels.length) {
        requiredLabels = $(requiredFields).prev('label');
    }
    $(requiredLabels).prepend("<span class='text-danger'>*</span>");
    if (requiredFields.length > 0) {
        $($(requiredFields).get(-1)).after("<small>(<strong class='text-danger'>*</strong>)" + requiredMessage + "</small>");
    }
});
$(document).ajaxStart(function () {
    showSpinner();
});
$(document).ajaxStop(function () {
    hideSpinner();
});

// Function to hide the Loading Wrapper
function hideSpinner() {
    $('#load-wrapper').addClass("d-none");
}

// Function to show the Loading Wrapper
function showSpinner() {
    $('#load-wrapper').removeClass("d-none");
}
