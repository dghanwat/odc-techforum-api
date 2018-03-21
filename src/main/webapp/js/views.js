fb.views.Menu = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('menu');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    }

});

fb.views.Welcome = Backbone.View.extend({

    initialize: function () {
        var self = this;
        this.template = fb.templateLoader.get('welcome');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },

    events: {
        'click .login': 'login'
    },

    login: function () {
        $(document).trigger('login');
        return false;
    }

});


fb.views.Error = Backbone.View.extend({

    initialize: function () {
        this.template = _.template(fb.templateLoader.get('error'));
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },

    events: {
        'click .retry':'retry'
    },

    retry: function () {
        Backbone.history.loadUrl(Backbone.history.fragment);
    }

});

fb.views.Revoke = Backbone.View.extend({

    initialize: function () {
        this.render();
    },

    render: function () {
        this.$el.html(this.options.template());
        return this;
    },

    events: {
        "click .revoke": "revoke"
    },

    revoke: function () {
        fb.spinner.show();
        FB.api("/me/permissions", "delete", function () {
            fb.spinner.hide();
            fb.alert('Permissions revoked');
            FB.getLoginStatus();
        });
        $(document).trigger('permissions_revoved');
        return false;
    }

});

fb.views.Venue = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('venue');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    }

});

fb.views.Login = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('login');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    }

});

fb.views.Presentations = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('presentations');
        //this.render();
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getAllSessions: function(id) {
        var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/all";
		var self = this;
		if(!id) {
			id = 1;
			currentPresentation = 1;
		} else {
			currentPresentation = id;
		}
		if(localStorage.getItem('sessionData')) {
			this.model = JSON.parse(localStorage.getItem('sessionData'));
			
			
			var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
			this.model = JSON.parse(localStorage.getItem('sessionData'));
			
			for(var i = 0 ; i < self.model.length;i++) {
				var users = self.model[i].users;
				for(var j = 0; j < users.length ; j++) {
					if(users[j] == name) {
						self.model[i].register = true;
						break;
					} else {
						self.model[i].register = false;
					}
				}
			}
			self.timer = setTimeout(function() {
				self.render();	
		        self.timer = null;
		        $('div[id^="presentation-"]').hide(); // Hide all the divs
				$("#presentation-"+currentPresentation).show(); // Show the current one
				presentationCounterLength = self.model.length;

			}, 500);
			return;
		} else {
			console.log("getting from server");
		}

        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					beforeSend: function(xhr){
					   	fb.spinner.show();
					},
					success: function(res, status, jqXHR) {
						var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
						self.model = res;
						for(var i = 0 ; i < self.model.length;i++) {
							var users = self.model[i].users;
							for(var j = 0; j < users.length ; j++) {
								if(users[j] == name) {
									self.model[i].register = true;
									break;
								} else {
									self.model[i].register = false;
								}
							}
						}
						self.render();
						presentationCounterLength = res.length;
						$('div[id^="presentation-"]').hide(); // Hide all the divs
						$("#presentation-"+currentPresentation).show(); // Show the current one
						var dataToStore = JSON.stringify(self.model);
						localStorage.setItem("sessionData" , dataToStore);
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});
						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }

	}

});

fb.views.Schedule = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('schedule');
        //this.render();
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	showSchedule: function(roomNumber) {
		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/room/"+roomNumber;
		var self = this;

		if(localStorage.getItem('scheduleData-'+roomNumber)) {
			console.log("fetching from session")
			this.model = JSON.parse(localStorage.getItem('scheduleData-'+roomNumber));
			self.model.room = "Training Room " + roomNumber ;
			this.render();

		    if(self.timer)
		        clearTimeout(self.timer);
		    
		    self.timer = setTimeout(function() {
		    	
				$("#"+roomNumber).css('background','#e64c65');			
		        self.timer = null;
			}, 500);
			
			return;
		}

        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					success: function(res, status, jqXHR) {
						self.model = res;
						self.model.room = "Training Room " + roomNumber ;
						self.render();
						$("#"+roomNumber).css('background','#e64c65');

						var dataToStore = JSON.stringify(self.model);
						localStorage.setItem('scheduleData-'+roomNumber , dataToStore);

						
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});

						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }
		
	}

});

fb.views.FeedBack = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('feedback');
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getQuestionsBySlot: function(slot) {
		if(!slot) {
			slot = "SLOT1";
		}
		var timeSlot = "";
		switch(slot){
			case "SLOT1" : timeSlot = "11:50 AM";
			               break;
			case "SLOT2" : timeSlot = "12:45 PM";
			               break;
			case "SLOT3" : timeSlot = "2:20 PM";
			               break;
			case "SLOT4" : timeSlot = "3:15 PM";
			               break;
			case "SLOT5" : timeSlot = "4:30 PM";
			               break;
			default : timeSlot = slot;
			          break;
		}

		switch(timeSlot){
			case "11:50 AM" : slot = "SLOT1";
			               break;
			case "12:45 PM" : slot = "SLOT2";
			               break;
			case "2:20 PM" : slot = "SLOT3";
			               break;
			case "3:15 PM" : slot = "SLOT4";
			               break;
			case "4:30 PM" : slot = "SLOT5";
			               break;
			
		}

		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/slot/"+timeSlot;
		var self = this;
        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					success: function(res, status, jqXHR) {
						self.model = res;
						self.model.slot = timeSlot;
						self.render();
						$("#"+slot).css('background','#e64c65');
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});

						fb.router.navigate("", {trigger: true});
					}
				});


            
        } catch (e) {
            
        }
	}

});

fb.views.FeedbackDetails = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('feedbackDetails');
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getQuestionsBySession: function(sessionId,slot) {
		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "question/session/"+sessionId;
		var self = this;
        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					success: function(res, status, jqXHR) {
						self.model = res;
						if(self.model && self.model[0] && self.model[0].sessionName){
							self.model.sessionName = self.model[0].sessionName;
						}
						self.model.slot = slot;
						self.render();
						time = 1000;
						var d1 = new Date();
						var d2 = new Date();
						console.log("adding delay");
						while (d2.valueOf() < d1.valueOf() + time) {
							d2 = new Date();
						}
						console.log("adding delay ends");
						for(var i = 0 ;i < self.model.length ; i++ ) {
							var userResponseQuestionOptionId = localStorage.getItem("USER_FEEDBACK_RESPONSE" + self.model[i].id);
							if(userResponseQuestionOptionId) {
								$("#feedBackDet-"+self.model[i].id+"-"+userResponseQuestionOptionId).css('background','#e64c65');
							}
						}
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});

						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }
	}

});


fb.views.Quiz = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('quiz');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    }

});

fb.views.Speakers = Backbone.View.extend({


    initialize: function () {
        this.template = fb.templateLoader.get('speakers');
        //this.render();
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getAllPresenter: function(id) {
        var restUrl = localStorage.getItem('SERVER_BASE_URL') + "presenter/all";
		var self = this;
		if(!id) {
			id = 1;
			currentPresentation = 1;
		} else {
			currentPresentation = id;
		}

		if(localStorage.getItem('speakerData')) {
			console.log("fetching from session")
			this.model = JSON.parse(localStorage.getItem('speakerData'));
			this.render();
						
			speakerCounterLength = this.model.length;
			return;
		}

        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					success: function(res, status, jqXHR) {
						self.model = res;
						self.render();
						speakerCounterLength  = res.length;
						$('div[id^="speaker-"]').hide(); // hide all the divs
						$("#speaker-"+currentPresentation).show(); // show only this div
						var dataToStore = JSON.stringify(self.model);
						localStorage.setItem("speakerData" , dataToStore);
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});

						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }

	}

});


fb.views.MySessions = Backbone.View.extend({

    initialize: function () {
        this.template = fb.templateLoader.get('mysessions');
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getMySessions: function() {
		var dasId = localStorage.getItem('DAS_ID');
		if(dasId != "") {
			var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/user/"+localStorage.getItem('DAS_ID');
			var self = this;
			try {
				$.ajax({
						type: 'GET',
						dataType: 'json',
						async: 'false',
						cache: false,
						contentType: 'application/json; charset=utf-8',
						url: restUrl,
						success: function(res, status, jqXHR) {
							self.model = res;
							self.render();
							localStorage.removeItem("sessionData");
						},
						error: function(jqXHR, textStatus, errorThrown){
							notif({
							  type: "error",
							  msg: "Sorry. Connection to Server Failed",
							  position: "right",
							  opacity: 0.8,
							  time: 1000,
							  fade: true
							});
							fb.router.navigate("", {trigger: true});
						}
					});
				
			} catch (e) {
				
			}
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

	}

});

fb.views.SpeakersList = Backbone.View.extend({

    initialize: function () {
        var self = this;
        this.template = fb.templateLoader.get('speakersList');
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },


});

fb.views.TeaserVote = Backbone.View.extend({

    initialize: function () {
        var self = this;
        this.template = fb.templateLoader.get('teaserVote');
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },

    render: function () {
        this.$el.html(this.template(this.model));
        return this;
    },

	getAllPresenter: function(id) {
        var restUrl = localStorage.getItem('SERVER_BASE_URL') + "presenter/all";
		var self = this;
        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: true,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					success: function(res, status, jqXHR) {
						self.model = res;
						self.render();
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});

						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }

	}


});


fb.views.ScheduleMatrix = Backbone.View.extend({

	

	events: {
        'click .chart_list li': 'toggleContextMenu',
        'click .register' : 'register',
        'click .details' : 'details',
        'click .feedback' : 'feedback',
        'click .bestTeaser' : 'bestTeaser',
    },

    register: function(e) {
    	
    	invokedFromScheduleMatrix = true;

    	if(sessionDataRegister == "false") {
    		console.log("register");
    		fb.router.navigate("registerUser/"+sessionDataId+"/"+sessionDataSlot+"/register", {trigger: true});	
    	} else {
    		console.log("unregister");
    		fb.router.navigate("registerUser/"+sessionDataId+"/"+sessionDataSlot+"/unregister", {trigger: true});	
    	}

    	
    	$(".profile_details").hide();
    	e.preventDefault();



    },

    details: function(e) {
    	fb.router.navigate("presentations/"+sessionDataId, {trigger: true});	
    	$(".profile_details").hide();
    	e.preventDefault();
    },

    feedback: function(e) {
    	fb.router.navigate("feedbackDetails/"+sessionDataId+"/"+sessionDataSlot, {trigger: true});	
    	$(".profile_details").hide();
    	e.preventDefault();
    },

    bestTeaser: function(e) {
    	
    	$('img[id^="teaser-vote-"]').hide();
    	
    	var postToServer = true;
    	var localStorageBestTeaserSessionId = localStorage.getItem("BEST_TEASER_SESSIONID");
    	console.log(localStorageBestTeaserSessionId);
    	if(!localStorageBestTeaserSessionId) {
    		postToServer = true;
    	} else {
    		if(localStorageBestTeaserSessionId == sessionDataId) {
    			postToServer = false;
    		}
    	}

    	var dasId = localStorage.getItem('DAS_ID');
		var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/recordTeaserVote/"+sessionDataId+"/"+dasId;

		if(postToServer) {

			if(dasId != "" ) {
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
									  msg: "Thank You. Your Vote Is Registered Successfully.",
									  position: "right",
									  opacity: 0.8,
									  bgcolor: "#e64c65",
									  time: 800,
									  fade: true
									});

									localStorage.setItem('BEST_TEASER_SESSIONID',sessionDataId);
									$("#teaser-vote-"+sessionDataId).show();
								} 
								else if(res.result == "period_closed"){
									notif({
									  type: "error",
									  msg: "Sorry. Voting period is now closed.",
									  position: "right",
									  opacity: 0.8,
									  bgcolor: "#e64c65",
									  time: 800,
									  fade: true
									});
								}
							},
							error: function(jqXHR, textStatus, errorThrown){
								notif({
									  type: "error",
									  msg: "Sorry. Operation Failed. Please try again later",
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
				  msg: "Please enter your details and try again",
				  position: "center",
				  opacity: 0.8,
				  time: 1000,
				  fade: true
				});
				fb.router.navigate("login", {trigger: true});
			}
			fb.spinner.show();
		} else {
			notif({
			  type: "success",
			  msg: "Thank You. Your Vote Is Already Registered.",
			  position: "right",
			  opacity: 0.8,
			  bgcolor: "#e64c65",
			  time: 800,
			  fade: true
			});
			$("#teaser-vote-"+sessionDataId).show();
		}
    	
    	
    	$(".profile_details").hide();
    	e.preventDefault();
    },

    toggleContextMenu: function(e) {
    	sessionDataId = e.currentTarget.attributes.sessiondataid.value;
    	sessionDataRegister = e.currentTarget.attributes.sessiondataregister.value;
    	sessionDataSlot = e.currentTarget.attributes.sessiondataslot.value;

    	if(sessionDataRegister == "false") {
    		$("#registerText").text("Register");
    	} else {
    		$("#registerText").text("Unregister");
    	}
    	if($(".profile_details").css('display') == "block") {
    		$(".profile_details").hide();
    		e.preventDefault();
    	} else {
    		$(".profile_details").show();
    		e.preventDefault();
    	}

    },

    initialize: function () {
        var self = this;
        var sessionDataId;
		var sessionDataRegister;
		var sessionDataSlot;
		invokedFromScheduleMatrix = false;
        this.template = fb.templateLoader.get('scheduleMatrix');

    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },

    render: function () {
        this.$el.html(this.template(this.model));
        $(".profile_details").hide();
        $('img[id^="teaser-vote-"]').hide();
        var localStorageBestTeaserSessionId = localStorage.getItem("BEST_TEASER_SESSIONID");
    	if(localStorageBestTeaserSessionId) {
    		$("#teaser-vote-"+localStorageBestTeaserSessionId).show();
    	} 
        return this;
    },

    getAllSessions: function(id) {
        var restUrl = localStorage.getItem('SERVER_BASE_URL') + "session/all";
		var self = this;
		if(!id) {
			id = 1;
			currentPresentation = 1;
		} else {
			currentPresentation = id;
		}
		if(localStorage.getItem('sessionData')) {
			var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
			this.model = JSON.parse(localStorage.getItem('sessionData'));
			for(var i = 0 ; i < self.model.length;i++) {
				var users = self.model[i].users;
				for(var j = 0; j < users.length ; j++) {
					if(users[j] == name) {
						self.model[i].register = true;
						break;
					} else {
						self.model[i].register = false;
					}
				}
			}
			self.timer = setTimeout(function() {
				self.render();	
		        self.timer = null;
			}, 500);
			
			return;
		}
        try {
        	$.ajax({
					type: 'GET',
                    dataType: 'json',
                    async: 'false',
					cache: false,
                    contentType: 'application/json; charset=utf-8',
					url: restUrl,
					beforeSend: function(xhr){
					   	fb.spinner.show();
					},
					success: function(res, status, jqXHR) {
						var name = 	localStorage.getItem('FIRST_NAME') + " " + localStorage.getItem('LAST_NAME')
						self.model = res;
						for(var i = 0 ; i < self.model.length;i++) {
							var users = self.model[i].users;
							for(var j = 0; j < users.length ; j++) {
								if(users[j] == name) {
									self.model[i].register = true;
									break;
								} else {
									self.model[i].register = false;
								}
							}
						}
						self.render();

						var dataToStore = JSON.stringify(self.model);
						localStorage.setItem("sessionData" , dataToStore);
					},
					error: function(jqXHR, textStatus, errorThrown){
						notif({
						  type: "error",
						  msg: "Sorry. Connection to Server Failed",
						  position: "right",
						  opacity: 0.8,
						  time: 1000,
						  fade: true
						});
						fb.router.navigate("", {trigger: true});
					}
				});
            
        } catch (e) {
            
        }

	}



});


