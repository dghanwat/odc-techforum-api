window.addEventListener('load', function () {
    new FastClick(document.body);
}, false);

localStorage.removeItem("speakerData");

localStorage.removeItem("sessionData");
localStorage.removeItem("speakerData");
localStorage.removeItem("scheduleData-TR01");
localStorage.removeItem("scheduleData-TR02");
localStorage.removeItem("scheduleData-TR03");
localStorage.removeItem("scheduleData-TR04");


localStorage.setItem('SERVER_BASE_URL', 'http://odctechforum-fluidmobileapp.rhcloud.com/rest/tech/');
//localStorage.removeItem("REG-SLOT" , "11:10 AM");

var fb = new MobileApp();
var presentationCounterLength = 1
var currentPresentation = 1;
var currentSpeaker = 1;
var speakerCounterLength = 1;
var schedulePageNumber = 1;
var cache = {};
var invokedFromScheduleMatrix = false;

fb.spinner = $("#spinner");
fb.spinner.hide();
var currentCacheVersion = 12;


fb.slider = new PageSlider($('#container'));


fb.MobileRouter = Backbone.Router.extend({

    routes: {
        "":                         "welcome",
        "menu":                     "menu",
		"venue":                    "venue",
		"login":                    "login",
		"presentations":            "presentations",
		"presentations/:id" : "presentationDetails",
		"speakers/:id":         "speakersDetails",
		"schedule/:id": "schedule",
		"speakers": "speakers",
		"quiz": "quiz",
		"feedback/:slot": "feedback",
		"registerUser/:id/:slot/:action" : "registerUser",
		"mysessions" : "mysessions",
		"feedbackDetails/:id/:slot" : "feedbackDetails",
		"feedBackResponse/:questionId/:optionId" : "feedBackResponse",
		"teaserVote": "teaserVote",
		"scheduleMatrix": "scheduleMatrix",
		"recordTeaserVote/:id" : "recordTeaserVote",
    },

    welcome: function () {

    	fb.spinner.show();

    	console.log($(".largescreen").css('display'));
    	if(!localStorage.getItem("currentCacheVersion")) { // if cache does not exist then create it
    		localStorage.setItem("currentCacheVersion",currentCacheVersion);
    		localStorage.setItem('SERVER_BASE_URL', 'http://odctechforum-fluidmobileapp.rhcloud.com/rest/tech/');
    	} else {
    		var oldCacheVersion = localStorage.getItem("currentCacheVersion");
    		if(oldCacheVersion != currentCacheVersion) {
    			console.log("Clear cache");
    			localStorage.removeItem("sessionData");
    			localStorage.removeItem("speakerData");
    			localStorage.removeItem("scheduleData-TR01");
    			localStorage.removeItem("scheduleData-TR02");
    			localStorage.removeItem("scheduleData-TR03");
    			localStorage.removeItem("scheduleData-TR04");
    			localStorage.setItem("currentCacheVersion",currentCacheVersion);
    		}
    	}
        // Reset cached views
        fb.slider.slidePageFrom(new fb.views.Welcome().$el, "left");

        if(console.log($(".largescreen").css('display')) == "block") {
        	// large screen
        	
        } else {
        	// small screen
        }

        // Get All sessions to store in session
        if(!localStorage.getItem('sessionData')) {

        	var view = new fb.views.Presentations({template: fb.templateLoader.get('presentations')});
			view.getAllSessions();
		}

		if(!localStorage.getItem('speakerData')) {
			view = new fb.views.Speakers({template: fb.templateLoader.get('speakers')});
			view.getAllPresenter();
		}

		if(!localStorage.getItem('scheduleData-TR01')) {
			view = new fb.views.Schedule({template: fb.templateLoader.get('schedule')});
			view.showSchedule("TR01");
		}

		if(!localStorage.getItem('scheduleData-TR02')) {
			view = new fb.views.Schedule({template: fb.templateLoader.get('schedule')});
			view.showSchedule("TR02");
		}
		if(!localStorage.getItem('scheduleData-TR03')) {
			view = new fb.views.Schedule({template: fb.templateLoader.get('schedule')});
			view.showSchedule("TR03");
		}
		if(!localStorage.getItem('scheduleData-TR04')) {
			view = new fb.views.Schedule({template: fb.templateLoader.get('schedule')});
			view.showSchedule("TR04");
		}

		fb.spinner.hide();
    },

    menu: function () {
        fb.slider.slidePageFrom(new fb.views.Menu().$el, "left");
        fb.slider.resetHistory();
    },

	venue: function () {
        fb.slider.slidePageFrom(new fb.views.Venue().$el, "right");
        fb.slider.resetHistory();
    },
	login: function () {
        fb.slider.slidePageFrom(new fb.views.Login().$el, "right");
        fb.slider.resetHistory();
		$("#dasId").val(localStorage.getItem('DAS_ID'));
		$("#firstName").val(localStorage.getItem('FIRST_NAME'));
		$("#lastName").val(localStorage.getItem('LAST_NAME'));
		$("#baseLocation").val(localStorage.getItem('BASE_LOCATION'));
		
		
    },
	feedback: function (slot) {

		var view = new fb.views.FeedBack({template: fb.templateLoader.get('feedback')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.getQuestionsBySlot(slot);

    },
	quiz: function () {
        fb.slider.slidePageFrom(new fb.views.Quiz().$el, "right");
        fb.slider.resetHistory();
    },


	feedbackDetails: function(id,slot) {
		var view = new fb.views.FeedbackDetails({template: fb.templateLoader.get('feedbackDetails')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.getQuestionsBySession(id,slot);
	},

	feedBackResponse: function(questionId,optionId) {

		var dasId = localStorage.getItem('DAS_ID');
		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "question/add/userresponse/"+questionId+"/"+optionId;
		if(dasId != "") {
			 try {
				$.ajax({
						type: 'POST',
						dataType: 'json',
						async: 'false',
						cache: false,
						contentType: 'application/json; charset=utf-8',
						url: restUrl,
						success: function(res, status, jqXHR) {
							var userResponseQuestionOptionId = localStorage.getItem("USER_FEEDBACK_RESPONSE" + questionId);
							if(!userResponseQuestionOptionId) {
								$("#feedBackDet-"+questionId+"-"+optionId).css('background','#e64c65');
								localStorage.setItem("USER_FEEDBACK_RESPONSE" + questionId,optionId);
							} else {
								$("#feedBackDet-"+questionId+"-"+userResponseQuestionOptionId).css('background','');
								$("#feedBackDet-"+questionId+"-"+optionId).css('background','#e64c65');
								localStorage.setItem("USER_FEEDBACK_RESPONSE" + questionId,optionId);
							}
							
						},
						error: function(jqXHR, textStatus, errorThrown){
							notif({
							  type: "error",
							  msg: "Sorry. Failed to Save. Please try again later.",
							  position: "center",
							  opacity: 0.8,
							  time: 1000,
							  fade: true
							  
							});
						}
					});
				
			} catch (e) {
				
			}

		} else {
			notif({
			  type: "error",
			  msg: "Please Enter your details and try again",
			  position: "right",
			  opacity: 0.8,
			  time: 1000,
			  fade: true
			});

			fb.router.navigate("login", {trigger: true});
		}

		fb.spinner.hide();
		
	},

	registerUser: function(id,slot,action) {
		var timeSlot = "";
		switch(slot){
			case "11:50 AM" : timeSlot = "SLOT1";
						   break;
			case "12:45 PM" : timeSlot = "SLOT2";
						   break;
			case "2:20 PM" : timeSlot = "SLOT3";
						   break;
			case "3:15 PM" : timeSlot = "SLOT4";
						   break;
			case "4:30 PM" : timeSlot = "SLOT5";
						   break;
		}
		/*if(action == "register") {
			if(localStorage.getItem("REG-SLOT-"+timeSlot)) {
				var registeredSlot = localStorage.getItem("REG-SLOT-"+timeSlot);
				if(timeSlot == registeredSlot) {
					notif({
					  type: "error",
					  msg: "You have already registered for this time slot.",
					  position: "right",
					  opacity: 0.8,
					  time: 1000,
					  fade: true
					  
					});
					return false;
				}
			}
		}*/

		

		var dasId = localStorage.getItem('DAS_ID');
		var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME');
		var baseLocation =  localStorage.getItem('BASE_LOCATION');
		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/add/user/"+id+"/"+dasId+"/"+encodeURIComponent(name)+"/"+baseLocation+"/"+action;

		if(dasId != "" && baseLocation != "") {
			 try {
				$.ajax({
						type: 'POST',
						dataType: 'json',
						async: 'true',
						cache: false,
						contentType: 'application/json; charset=utf-8',
						url: restUrl,
						success: function(res, status, jqXHR) {
							//alert(res.result);
							if(res.result == "success" ) {
								notif({
								  type: "success",
								  msg: "Submission Recorded Successfully.", //Transport for Mumbai Attendees is closed
								  position: "right",
								  opacity: 0.8,
								  bgcolor: "#e64c65",
								  time: 800,
								  fade: true
								});
								if(action == "unregister") {
									localStorage.removeItem("REG-SLOT-"+timeSlot , timeSlot);
									$("#registerlink-"+id).attr("href",'#registerUser/'+id+'/'+slot+'/register');
									$("#"+id).text("REGISTER");

									var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
									var sessionData = JSON.parse(localStorage.getItem('sessionData'));
									for(var i = 0 ; i < sessionData.length;i++) {
										if(sessionData[i].id == id) {
											var users = sessionData[i].users;
											if(users.length > 0 ) {
												for(var j = 0; j < users.length ; j++) {
													if(users[j] == name) {
														sessionData[i].users.splice(j,1);
														sessionData[i].register = false;
														
														break;
													}
												}
												
											} 
											
											var dataToStore = JSON.stringify(sessionData);
											localStorage.setItem("sessionData" , dataToStore);
										}
										
									}

									

								} else {
									localStorage.setItem("REG-SLOT-"+timeSlot , timeSlot);
									$("#registerlink-"+id).attr("href",'#registerUser/'+id+'/'+slot+'/unregister');
									$("#"+id).text("UNREGISTER");

									var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
									var sessionData = JSON.parse(localStorage.getItem('sessionData'));
									for(var i = 0 ; i < sessionData.length;i++) {
										if(sessionData[i].id == id) {
											var users = sessionData[i].users;
											if(users.length > 0 ) {
												sessionData[i].users[users.length] = name;
											} else {
												sessionData[i].users[0] = name;
											}
											var dataToStore = JSON.stringify(sessionData);
											localStorage.setItem("sessionData" , dataToStore);
										}
										
									}
								}
								if(invokedFromScheduleMatrix) {
									invokedFromScheduleMatrix = false;
									fb.router.navigate("scheduleMatrix", {trigger: true});	
								}
							} else if(res.result == "registration_full") {
								notif({
									  type: "error",
									  msg: "Registration is full for this slot. Please try again.",
									  position: "right",
									  opacity: 0.8,
									  time: 4000,
									  fade: true
									  
									});
							}
							else {
								notif({
								  type: "error",
								  msg: "You have already registered for this time slot.",
								  position: "right",
								  opacity: 0.8,
								  time: 1000,
								  fade: true
								  
								});

							}
							

						},
						error: function(jqXHR, textStatus, errorThrown){
							notif({
								  type: "error",
								  msg: "Sorry. Registration failed. Please try again later",
								  position: "center",
								  opacity: 0.8,
								  time: 1000,
								  fade: true
								  
								});
						}
					});
				
			} catch (e) {
				
			}

			//fb.router.navigate("presentationDetails/" + currentPresentation, {trigger: true});

		} else {
			notif({
			  type: "error",
			  msg: "Please enter your details and try again",
			  position: "center",
			  opacity: 0.8,
			  time: 1000,
			  fade: true
			  
			});
			fb.router.navigate("login", {trigger: true});
		}
		fb.spinner.show();
	},

	mysessions: function() {

		var self = this;
		var view = new fb.views.MySessions({template: fb.templateLoader.get('mysessions')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.getMySessions();

	},

	presentations: function () {
		var self = this;
		var view = new fb.views.Presentations({template: fb.templateLoader.get('presentations')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.getAllSessions();
		//var response = '{"data" : [ {"id": "1" , "title" :"Angular JS" , "description" : "HTML 5 framework" , "imageURL" : "http://jquer.in/wp-content/uploads/2012/07/AngularJS-framework.png" } , {"id": "2" , "title" :"Apache Mahout" , "description" : "Machine Learning Framework" , "imageURL" : "http://hortonworks.com/wp-content/uploads/2013/09/mantle-mahout.png" }] }';
		//presentationCounterLength = jsonData.data.presentations.length;
		//view.model = jsonData.data.presentations;
        //view.render();
		//$('div[id^="presentation-"]').hide();
		//$("#presentation-"+currentPresentation).show();
    },

	presentationDetails: function (id) {
		var self = this;
		var view = new fb.views.Presentations({template: fb.templateLoader.get('presentations')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.getAllSessions(id);
    },

	speakersDetails: function (id) {
		fb.slider.resetHistory();
		var self = this;
		var view = new fb.views.Speakers({template: fb.templateLoader.get('speakers')});

		view.getAllPresenter(id);
        var slide = fb.slider.slidePageFrom(view.$el, "right");

        $('div[id^="speaker-"]').hide(); // hide all the divs
		$("#speaker-"+currentPresentation).show(); // show only this div
		
    },

	schedule: function (id) {
		var self = this;
		var view = new fb.views.Schedule({template: fb.templateLoader.get('schedule')});
        var slide = fb.slider.slidePageFrom(view.$el, "right");
        fb.slider.resetHistory();
		view.showSchedule(id);
		
    },

	speakers: function () {
        fb.slider.resetHistory();
		var self = this;
		var view = new fb.views.Speakers({template: fb.templateLoader.get('speakers')});

		view.getAllPresenter();
        var slide = fb.slider.slidePageFrom(view.$el, "right");
		//speakerCounterLength = jsonData.data.speakers.length;
		//view.model = jsonData.data.speakers;
        //view.render();
		//$('div[id^="speaker-"]').hide();
		//$("#speaker-"+currentPresentation).show();

		$('div[id^="speaker-"]').hide(); // hide all the divs
		$("#speaker-"+currentPresentation).show(); // show only this div
			
    },

    teaserVote: function() {
    	fb.slider.resetHistory();
		var self = this;
		var view = new fb.views.TeaserVote({template: fb.templateLoader.get('teaserVote')});

		view.getAllPresenter();
        var slide = fb.slider.slidePageFrom(view.$el, "right");

    },

    scheduleMatrix: function() {
    	fb.slider.resetHistory();
		var self = this;
		var view = new fb.views.ScheduleMatrix({template: fb.templateLoader.get('scheduleMatrix')});

		view.getAllSessions();
		
        var slide = fb.slider.slidePageFrom(view.$el, "right");

    },

    recordTeaserVote: function(sessionId) {
    	
    },


    showErrorPage: function () {
        fb.slider.slidePage(new fb.views.Error().$el);
    }

});

$(document).on('ready', function () {



    fb.templateLoader.load(['menu', 'welcome','venue','login','presentations','schedule','speakers','feedback','quiz','mysessions','feedbackDetails','teaserVote','scheduleMatrix'], function () {
        fb.router = new fb.MobileRouter();
        Backbone.history.start();
    });

	var dasId = localStorage.getItem('DAS_ID');

	 $.ajaxSetup({
	    beforeSend: function (xhr)
	    {
	       fb.spinner.show();
	       xhr.setRequestHeader("Authorization",dasId);        
	    }
	});

	if(typeof(Storage)=="undefined") {
		alert("Sorry. It looks like your client does not support HTML5. Please upgrade to an HTML5 Compliant client.");
	}

	
	if(!localStorage.getItem('DAS_ID')) {
		localStorage.setItem('DAS_ID',"");
	}
	




});

$(document).on('click', '.button.back', function() {
    window.history.back();
    return false;
});

$(document).on('click', '.logout', function () {
    return false;
});

$(document).on('login', function () {
	fb.spinner.show();
	fb.slider.slidePageFrom(new fb.views.Welcome().$el, "left");
    fb.slider.resetHistory();
	
	
});


$(document).on('click','.previous-presentation', function () {
	$('div[id^="presentation-"]').hide();
	if(currentPresentation == 1) {
		currentPresentation = presentationCounterLength;
	}
	else {
		currentPresentation--;
	}
	$("#presentation-"+currentPresentation).show();
	return false;

	
});

$(document).on('click','.next-presentation', function () {
	$('div[id^="presentation-"]').hide(); // hide all div with name starting with 
	if(currentPresentation == presentationCounterLength){
		currentPresentation = 1;
	} else {
		currentPresentation++;
	}
	$("#presentation-"+currentPresentation).show();
	return false;

	
});

$(document).on('click','.previous-speaker', function () {
	$('div[id^="speaker-"]').hide();
	if(currentSpeaker == 1) {
		currentSpeaker = speakerCounterLength;
	}
	else {
		currentSpeaker--;
	}
	$("#speaker-"+currentSpeaker).show();
	return false;

	
});

$(document).on('click','.next-speaker', function () {
	$('div[id^="speaker-"]').hide();
	if(currentSpeaker == speakerCounterLength) {
		currentSpeaker = 1;
	}
	else {
		currentSpeaker++;
	}
	$("#speaker-"+currentSpeaker).show();
	return false;
	
});

$(document).on('click','.loginsubmit', function () {
	var dasId = $("#dasId").val();
	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var baseLocation = $("#baseLocation").val();
	localStorage.setItem('DAS_ID',dasId);
	localStorage.setItem('FIRST_NAME',firstName);
	localStorage.setItem('LAST_NAME',lastName);
	localStorage.setItem('BASE_LOCATION',baseLocation);

	localStorage.removeItem("sessionData");
	

	if(baseLocation.toUpperCase() != "MUMBAI" 
		&& baseLocation.toUpperCase() != "PUNE" 
		&& baseLocation.toUpperCase() != "BANGALORE") {

		notif({
		  type: "error",
		  msg: "Please enter valid Base City",
		  position: "right",
		  opacity: 0.8,
		  bgcolor: "#e64c65",
		  time: 1000,
		  fade: true
		  
		});

		return false;
	}

	fb.router.navigate("", {trigger: true});

	notif({
		  type: "success",
		  msg: "Thank You. Your Information registered successfully",
		  position: "right",
		  opacity: 0.8,
		  bgcolor: "#e64c65",
		  time: 1000,
		  fade: true
		  
		});

	return false;


	
});

function register(id,action) {
	//this.registerUser(id,'11:10 AM',action);
	fb.router.navigate("registerUser/"+id+"/11:10 AM"+"/"+action, {trigger: true});
	return false;
}



$(document).ajaxComplete(function() {
  fb.spinner.hide();
});



Handlebars.registerHelper('if_mod', function(a, b, opts) {
    if((a % b) == 0) // Or === depending on your needs
        return opts.fn(this);
    else
        return opts.inverse(this);
});

Handlebars.registerHelper('if_eq', function(a, b, opts) {
    if(a == b) // Or === depending on your needs
        return opts.fn(this);
    else
        return opts.inverse(this);
});