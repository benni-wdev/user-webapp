var root = null;
var useHash = true;
var hash = '#!';
var router = new Navigo(root, useHash, hash);

router
      .on({
        'activation/:activationToken': function (params) {
            activate(params.activationToken);
        },
        'profile': function () {
            loadProfile();
        },
        'register': function () {
            loadRegister();
        },
        'login': function () {
            loadLogin();
        },
        'logout': function () {
            loadLogout();
        },
        'goodbye': function () {
            loadGoodBye();
        },
        'userrecovery': function () {
            loadUserRecoveryInitiation();
        },
        'userrecovery/:recoveryToken': function (params) {
            loadUserRecoveryExecution(params.recoveryToken);
        },
        'profile/changepassword': function () {
            loadChangePassword();
        },
        'profile/changeemail': function () {
            loadChangeEmail();
        },
        '*': function () {
            loadEntry();
        }
      }).resolve();

$(document).ready(function() {
	if(localStorage.loginId !== null && typeof localStorage.loginId !== "undefined" ) {
		loadSessionNavbar();
	}
	else {
		loadPublicNavbar();
	}
});

function loadEntry() {
	$('#content').load('html/entry.html');
}
function loadRegister() {
	$('#content').load('html/register.html');
}
function loadLogin() {
	$('#content').load('html/login.html');
}
function loadGoodBye() {
	$('#content').load('html/goodbye.html');
}
function loadLogout() {
    localStorage.removeItem("loginId");
	logOut();
}
function loadProfile() {
	$('#content').load('html/userprofile.html');
	renderProfile();
}
function loadUserRecoveryInitiation() {
	$('#content').load('html/userrecoveryinitiation.html');
}
function loadUserRecoveryExecution(recoveryToken) {
	$('#content').load('html/userrecoveryexecution.html',function() {
	    document.getElementById("passwordrecoverytoken").value = recoveryToken;
	});
}
function loadChangePassword() {
	$('#content').load('html/changepassword.html');
}
function loadChangeEmail() {
	$('#content').load('html/changeemail.html');
}

function redirectToLogin() {
     var messageText = 'Please authenticate';
     setMessage(messageText);
     window.location = '/#!/login';
}

function logOut() {
    $.ajax({
         type: "POST",
         url: "rest/user/logout",
         success: function (data, status, jqXHR) {
             setMessage('Logout successful');
             window.location = '/#!/goodbye';
             loadNavbar();
         },
         error: function (jqXHR, status) {
            handleGeneralError(jqXHR);
         }
      });
}

function login() {
    var stayLoggedIn = $("#rememberme").is(":checked");
    var payload = {"loginId":$("#inputLoginId").val().trim(),"password":$("#inputPassword").val(),"longSession":stayLoggedIn};
    $.ajax({
         type: "POST",
         url: "rest/user/authenticate",
         data: JSON.stringify(payload),
         contentType: "application/json",
         dataType: "json",
         success: function (data, status, jqXHR) {
             processAuthSuccess(data);
         },
         error: function (jqXHR, status) {
            handleGeneralError(jqXHR);
         }
      });
}

function processAuthSuccess(data) {
    localStorage.loginId = data.loginId;
    window.location = '';
}

function activate(activationToken) {
    var payload = {"activationToken":activationToken};
    $.ajax({
         type: "POST",
         url: "rest/user/activate",
         data: JSON.stringify(payload),
         contentType: "application/json",
         dataType: "json",
         success: function (data, status, jqXHR) {
             var response =data;
             setMessage('Your account was activated, you can now log in');
             router.navigate('/login');
         },
         error: function (jqXHR, status) {
             handleGeneralError(jqXHR);
         }
      });
}

function recoverUserInit() {
        var email = $("#inputEmail").val();
		if(email =='') {
			setMessage('Please fill out the form!!');
		}
		else {
			var payload = "{\"email\":\""+email+"\"}";
			$.ajax({
	             type: "POST",
	             url: "rest/user/recovery/initiate",
	             data: payload,
	             async:true,
	             contentType: "application/json; charset=utf-8",
	             crossDomain: true,
	             dataType: "json",
	             success: function (data, status, jqXHR) {
	            	 var response =data;
	            	 setMessage('We have sent you an email to '+email+' for password reset');
	             },

	             error: function (jqXHR, status) {
	            	 handleGeneralError(jqXHR);
	             }
	          });
		}
}

function recoverUserExec() {
    if($("#inputPasswordR").val()!=$("#inputPasswordRepeat").val()) {
        setMessage('Passwords are not equal!!');
    }
    else{
        var payload = {"passwordToken":$("#passwordrecoverytoken").val(),"newPassword":$("#inputPasswordR").val()};
        $.ajax({
             type: "POST",
             url: "rest/user/recovery/execute",
             data: JSON.stringify(payload),
             contentType: "application/json",
             dataType: "json",
             success: function (data, status, jqXHR) {
                 var response =data;
                 setMessage('Your account is recovered, you can now log in');
                 router.navigate('/login');
             },
             error: function (jqXHR, status) {
                 handleGeneralError(jqXHR);
             }
          });
      }
}

function changeEmail() {
		if($("#inputEmail").val() =='') {
			setMessage('Please fill out the form!!');
		}
		else{
			var payload = {"email":$("#inputEmail").val()};
			$.ajax({
	             type: "POST",
	             url: "rest/user/emailchange",
	             data:JSON.stringify(payload),
	             contentType: "application/json; charset=utf-8",
	             dataType: "json",
	             success: function (data, status, jqXHR) {
	            	 setMessage('We have sent you an email to '+payload.email+' for email verification');
	             },
	             error: function (jqXHR, status) {
	            	handleGeneralError(jqXHR);
	             }
	          });
		}
}

function archive() {
		if($("#inputPassword").val() =='') {
			setMessage('Please fill out the form!!');
		}
		else{
			var payload = {"password":$("#inputPassword").val()};
			$.ajax({
	             type: "DELETE",
	             url: "rest/user",
	             data:JSON.stringify(payload),
	             contentType: "application/json; charset=utf-8",
	             dataType: "json",
	             success: function (data, status, jqXHR) {
	            	 setMessage('Deletion successful');
	            	 loadLogout();
	             },
	             error: function (jqXHR, status) {
	            	 handleGeneralError(jqXHR);
	             }
	          });
		}
}

function changePassword() {
		if($("#inputPasswordOld").val() =='' || $("#inputPasswordR").val() == '' || $("#inputPasswordRepeat").val() == '') {
			setMessage('Please fill out the form!!');
		}
		else if( $("#inputPasswordR").val() != $("#inputPasswordRepeat").val()) {
			setMessage('Passwords are not equal!!');
		}
		else{
			var payload = {"oldPassword":$("#inputPasswordOld").val(),"newPassword":$("#inputPasswordR").val()};
			$.ajax({
	             type: "POST",
	             url: "rest/user/passwordchange",
	             data:JSON.stringify(payload),
	             contentType: "application/json; charset=utf-8",
	             dataType: "json",
	             success: function (data, status, jqXHR) {
	            	 setMessage('Password successfully changed');
	             },
	             error: function (jqXHR, status) {
	            	 handleGeneralError(jqXHR);
	             }
	          });
		}
}

function renderProfile() {
	$('#userprofile').empty();
	$.ajax({
         type: "GET",
         url: "rest/user",
         contentType: "application/json",
         success: function (data, status, jqXHR) {
            $('#userprofile').append('<tr><td><b>' + 'Login ID' + "</b></td><td>" + data.loginId+'</td></tr>'+
                                     '<tr><td><b>' + 'Email' + "</b></td><td>" + data.emailAddress+'</td></tr>'+
                                     '<tr><td><b>' + 'Member since' + "</b></td><td>" + data.createdAt+'</td></tr>');
            if(data.lastLoggedInAt != undefined && data.lastLoggedInAt != null ) {
                $('#userprofile').append('<tr><td><b>' + 'last login' + "</b></td><td>" + data.lastLoggedInAt+'</td></tr>');
            }
            if(data.emailChangedAt != undefined && data.emailChangedAt != null ) {
                $('#userprofile').append('<tr><td><b>' + 'last email change' + "</b></td><td>" + data.emailChangedAt+'</td></tr>');
            }
            if(data.passwordChangedAt != undefined && data.passwordChangedAt != null ) {
                $('#userprofile').append('<tr><td><b>' + 'last password change' + "</b></td><td>" + data.passwordChangedAt+'</td></tr>');
            }
         },
         error: function (jqXHR, status) {
             handleGeneralError(jqXHR);
         }
    });
}

function register() {
    var loginIdRegEx = new RegExp('^[a-zA-Z0-9_]+$');
    if($("#inputPasswordR").val()!=$("#inputPasswordRepeat").val()) {
        setMessage('Passwords are not equal!!');
    }
    if(!loginIdRegEx.test($("#inputLoginIdR").val().trim())) {
        setMessage('Login ID not valid, only alphanumerical characters allowed');
    }
    else{
        var payload = {"loginId":$("#inputLoginIdR").val().trim(),"password":$("#inputPasswordR").val(),"emailAddress":$("#inputEmail").val().trim()};
        $.ajax({
             type: "POST",
             url: "rest/user/register",
             data: JSON.stringify(payload),
             contentType: "application/json",
             dataType: "json",
             success: function (data, status, jqXHR) {
                 var response = data;
                 setMessage('We have sent an email to '+response.emailAddress+' for account activation');
                 router.navigate('');
             },
             error: function (jqXHR, status) {
                 handleGeneralError(jqXHR);
             }
          });
    }
}


function loadNavbar() {
    if(typeof localStorage.loginId !== "undefined" ) {
        loadSessionNavbar();
    }
    else {
        loadPublicNavbar();
    }
}


function loadSessionNavbar() {
    $('#navbaruser').html(
        '<ul class="nav navbar-nav">'+
            '<li><a class="nav-link" href="">Home</a></li>'+
            '<li><a class="nav-link" href="#!/profile/changeemail">Change Email</a></li>'+
            '<li><a class="nav-link" href="#!/profile/changepassword">Change Password</a></li>'+
        '</ul>'+
        '<ul class="nav navbar-nav navbar-right">'+
            '<li><a class="nav-link"href="#!/profile"><span class="glyphicon glyphicon-user"></span> '+localStorage.loginId+'</a></li>'+
            '<li><a href="#!/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>'+
        '</ul>'
    );
}

function loadPublicNavbar() {
	$('#navbaruser').html(
        '<ul class="nav navbar-nav">'+
            '<li><a href="">Home</a></li>'+
            '<li><a href="#!/userrecovery">User Recovery</a></li>'+
        '</ul>'+
        '<ul class="nav navbar-nav navbar-right">'+
            '<li><a href="#!/register"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>'+
            '<li><a href="#!/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>'+
        '</ul>'
    );
}


function setMessage(message) {
    document.getElementById("messagecontent").innerHTML = htmlEncode(message);
    var x = document.getElementById("snackbar");
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
}


function handleGeneralError(jqXHR) {
    console.log(jqXHR);
    if(jqXHR.status==302) {
        setMessage("Please authenticate");
        window.location = '/#!/login';
    }
    else {
        var messageText = 'Error occured';
        if(typeof jqXHR.responseJSON !== "undefined") {
             messageText =jqXHR.responseJSON.messageText;
        }
        setMessage(messageText);
    }
}


function htmlEncode(value){
  return $('<div/>').text(value).html();
}

function htmlDecode(value){
  return $('<div/>').html(value).text();
}

