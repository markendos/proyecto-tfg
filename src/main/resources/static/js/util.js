let loadingData = false;

$(document).ready(function () {
    markRequiredFields();

    $(".alert").append($("<i onclick='closeAlert(this)' class='fas fa-window-close'></i>"));
});
$(document).ajaxStart(function () {
    const openModals = $("div.modal[aria-modal='true']");
    if (openModals.length === 0 && loadingData === false) {
        showSpinner();
    }
});
$(document).ajaxStop(function () {
    hideSpinner();
    const alerts = $(".alert");
    for (let i = 0; i < alerts.length; i++) {
        if ($(alerts[i]).find($('.fa-window-close')).length === 0) {
            $(".alert").append($("<i onclick='closeAlert(this)' class='fas fa-window-close'></i>"));
        }
    }
});

function markRequiredFields() {
    const requiredFields = $(":input[required]");
    let requiredLabels = $(requiredFields).parent().prev('label');
    $(requiredLabels).append("<span class='text-danger'>*</span>");
    requiredLabels = $(requiredFields).prev('label');
    $(requiredLabels).append("<span class='text-danger'>*</span>");

    if (requiredFields.length > 0) {
        $($(requiredFields).get(-1)).after("<small>(<strong class='text-danger'>*</strong>)" + requiredMessage + "</small>");
    }
}

function utf8_to_b64(str) {
    return window.btoa(unescape(encodeURIComponent(str)));
}

function b64_to_utf8(str) {
    return decodeURIComponent(escape(window.atob(str)));
}

// Function to hide the Loading Wrapper
function hideSpinner() {
    $('#load-wrapper').addClass("d-none");
}

// Function to show the Loading Wrapper
function showSpinner() {
    $('#load-wrapper').removeClass("d-none");
}

// Function to close alert banners
function closeAlert(element) {
    $(element).closest('.alert').fadeOut('fast');
}

// Function to decode and unescape html symbols in JS
function htmlDecode(input) {
    const doc = new DOMParser().parseFromString(input, "text/html");
    return doc.documentElement.textContent;
}

function getRandomColor() {
    const available = ['primary', 'secondary', 'success', 'danger', 'warning', 'info', 'light', 'dark'];
    const index = getRandomInt(0, available.length);
    return available[index];
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

function startLoad() {
    loadingData = true;
    if ($("#dataTable_wrapper")) {
        $("#dataTable_wrapper").hide();
    }
    $("#tableSpinner").show();
}

function endLoad() {
    $("#tableSpinner").hide();
    $("#dataTable_wrapper").show();
    loadingData = false;
}
