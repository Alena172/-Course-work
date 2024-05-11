var log = document.getElementById("form-log");
var reg = document.getElementById("form-reg");

document.getElementById("registerNow").addEventListener("click", function(event) {
    event.preventDefault();
    log.style.display = "none";
    reg.style.display = "block";
});

document.getElementById("loginNow").addEventListener("click", function(event) {
    event.preventDefault();
    log.style.display = "block";
    reg.style.display = "none";
});

function clearLoginForm() {
    document.getElementById('email-input').value = '';
    document.getElementById('password-input').value = '';
}

function clearRegistrationForm() {
    document.getElementById('first-name').value = '';
    document.getElementById('second-name').value = '';
    document.getElementById('email').value = '';
    document.getElementById('password').value = '';
}
