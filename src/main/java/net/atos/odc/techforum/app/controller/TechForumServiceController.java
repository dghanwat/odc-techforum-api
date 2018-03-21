/**
 * 
 */
package net.atos.odc.techforum.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.atos.odc.techforum.app.model.PresenterDto;
import net.atos.odc.techforum.app.model.QuestionDto;
import net.atos.odc.techforum.app.model.SessionDto;
import net.atos.odc.techforum.app.model.UserDetailsDto;
import net.atos.odc.techforum.app.model.UserDto;
import net.atos.odc.techforum.app.service.AttendanceService;
import net.atos.odc.techforum.app.service.PresenterService;
import net.atos.odc.techforum.app.service.QuestionService;
import net.atos.odc.techforum.app.service.SessionService;
import net.atos.odc.techforum.app.service.UserFeedbackService;
import net.atos.odc.techforum.app.service.UserService;
import net.atos.odc.techforum.app.service.impl.LdapService;
import net.atos.odc.techforum.app.util.ApplicationCache;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Path("/tech")
@Produces({ MediaType.APPLICATION_JSON })
@RequestScoped
public class TechForumServiceController {

	private static final String ALL_SESSION_DATA = "ALL_SESSION_DATA";

	private static final String ALL_PRESENTER_DATA = "ALL_PRESENTER_DATA";

	@Inject
	SessionService sessionService;

	@Inject
	PresenterService presenterService;

	@Inject
	UserService userService;

	@Inject
	QuestionService questionService;

	@Inject
	UserFeedbackService feedbackService;

	@Inject
	ApplicationCache applicationCache;

	@Inject
	AttendanceService attendanceService;

	public TechForumServiceController() {

	}

	// The Java method will process HTTP GET requests

	/**
	 * Retrieves all sessions
	 * 
	 * @param roomNo
	 * @return
	 */
	@GET
	@Path("/session/all")
	public List<SessionDto> getAllSession() {

		List<SessionDto> fetchAllSession = null;

		fetchAllSession = (List<SessionDto>) applicationCache
				.get(ALL_SESSION_DATA);
		if (fetchAllSession == null) {
			fetchAllSession = sessionService.fetchAllSession();
			applicationCache.set(ALL_SESSION_DATA, fetchAllSession);
		} else {
			System.out.println("Fetching from cache");
		}

		return fetchAllSession;

	}

	/**
	 * Retrieves the session based on id passed
	 * 
	 * @param sessionId
	 * @return
	 */
	@GET
	@Path("/session/{id}")
	public SessionDto getSessionById(@PathParam("id") long sessionId) {
		return sessionService.fetchSessionById(sessionId);
	}

	/**
	 * Retrieves all sessions based on room number passed
	 * 
	 * @param roomNo
	 * @return
	 */
	@GET
	@Path("/session/room/{id}")
	public List<SessionDto> getSessionByRoom(@PathParam("id") String roomNo) {
		return sessionService.fetchSessionByRoom(roomNo);
	}

	/**
	 * Retrieves all sessions based on time slot passed
	 * 
	 * @param timeSlot
	 * @return
	 */
	@GET
	@Path("/session/slot/{id}")
	public List<SessionDto> getSessionBySlot(@PathParam("id") String timeSlot) {
		return sessionService.fetchSessionBySlot(timeSlot);
	}

	/**
	 * Retrieves all sessions based on presenter id passed
	 * 
	 * @param presenterId
	 * @return
	 */
	@GET
	@Path("/session/presenter/{id}")
	public List<SessionDto> getSessionByPresenter(
			@PathParam("id") long presenterId) {
		return sessionService.fetchSessionByPresenter(presenterId);
	}

	/**
	 * Retrieves all presenter
	 * 
	 * @param roomNo
	 * @return
	 */
	@GET
	@Path("/presenter/all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<PresenterDto> getAllPresenter() {
		List<PresenterDto> presenters = (List<PresenterDto>) applicationCache
				.get(ALL_PRESENTER_DATA);
		if (presenters == null) {
			presenters = presenterService.fetchAllPresenter();
			applicationCache.set(ALL_PRESENTER_DATA, presenters);
		} else {
			System.out.println("Fetching from cache");
		}

		return presenters;
	}

	/**
	 * Retrieves all attendees based on session
	 * 
	 * @param sessionId
	 * @return
	 */
	@GET
	@Path("/user/session/{id}")
	public List<UserDto> getAllUsersBySession(@PathParam("id") long sessionId) {
		return userService.fetchUsersBySession(sessionId);
	}

	/**
	 * Creats a new reply announcement which is submitted by the user from
	 * mobile app
	 * 
	 * @param announcement
	 * @return
	 */
	@POST
	@Path("/session/add/user/{sessionId}/{dasId}/{name}/{location}/{action}")
	public Response registerUserToSession(
			@PathParam("sessionId") String sessionId,
			@PathParam("dasId") String dasId, @PathParam("name") String name,
			@PathParam("location") String location,
			@PathParam("action") String action) {

		dasId = dasId.toUpperCase();
		if (action.equals("unregister")) {
			sessionService.registerUserToSession(Long.valueOf(sessionId),
					dasId, name, location);
			return buildResponse("{\"result\":\"" + "success" + "\"}");
		} else {
			// System.out.println("******************** Session id from is " +
			// sessionId);

//			if (true) {
//				return buildResponse("{\"result\":\"" + "registration_full"
//						+ "\"}");
//			}

			if (!isRegForSameSlot(Long.parseLong(sessionId), dasId)) {

				sessionService.registerUserToSession(Long.valueOf(sessionId),
						dasId, name, location);
				return buildResponse("{\"result\":\"" + "success" + "\"}");
			} else {
				return buildResponse("{\"result\":\"" + "registered" + "\"}");
			}

			// return "{\"result\":\"" +
			// "registration_full" + "\"}";
			// if (!isRegForSameSlot(Long.parseLong(sessionId), dasId)) {
			//
			// sessionService.registerUserToSession(Long.valueOf(sessionId),
			// dasId, name, location);
			// return buildResponse("{\"result\":\"" + "success" + "\"}");
			// } else {
			// return buildResponse("{\"result\":\"" + "registered" + "\"}");
			// }
		}
	}

	@OPTIONS
	@Path("/session/add/user/{sessionId}/{dasId}/{name}/{location}/{action}")
	public Response optionsRegisterUserToSession(
			@PathParam("sessionId") String sessionId,
			@PathParam("dasId") String dasId, @PathParam("name") String name,
			@PathParam("location") String location,
			@PathParam("action") String action) {
		return optionsAll();
	}

	/**
	 * Returns users session
	 * 
	 * @param dasId
	 * @return
	 */
	@GET
	@Path("/session/user/{dasId}")
	public Response getAllSessionsForUser(@PathParam("dasId") String dasId) {

		// TODO : Optimise this method
		List<SessionDto> allSessions = sessionService.fetchAllSession();
		UserDto userDto = new UserDto();
		List<SessionDto> userSessions = new ArrayList<SessionDto>();
		for (SessionDto dto : allSessions) {
			List<UserDto> users = userService.fetchUsersBySession(dto.getId());
			for (UserDto user : users) {
				if (user.getDasId().equalsIgnoreCase(dasId)) {
					userSessions.add(dto);
					break;
				}
			}
		}
		userDto.setSessionDto(userSessions);

		return Response
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization,access_token")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "3600").entity(userDto)
				.build();
	}

	/**
	 * Retrieves all question based on session
	 * 
	 * @param sessionId
	 * @return
	 */
	@GET
	@Path("/question/session/{id}")
	public List<QuestionDto> getAllQuestionsBySession(
			@PathParam("id") long sessionId) {
		return questionService.fetchQuestionsBySession(sessionId);
	}

	@GET
	@Path("/questions")
	public List<QuestionDto> getAllQuestions() {
		List<SessionDto> fetchAllSession = (List<SessionDto>) applicationCache
				.get(ALL_SESSION_DATA);
		if (fetchAllSession == null) {
			fetchAllSession = sessionService.fetchAllSession();
			applicationCache.set(ALL_SESSION_DATA, fetchAllSession);
		} else {
			System.out.println("Fetching from cache");
		}
		List<QuestionDto> allQuestions = new ArrayList<QuestionDto>();
		for (SessionDto session : fetchAllSession) {
			allQuestions.addAll(questionService.fetchQuestionsBySession(session
					.getId()));
		}
		return allQuestions;
	}

	@POST
	@Path("/question/add/userresponse/{questionId}/{optionId}")
	public Response postUserResponse(@PathParam("questionId") long questionId,
			@PathParam("optionId") long optionId,
			@HeaderParam("authorization") String dasId) {
		System.out.println("DAS ID is " + dasId);

		if (dasId == null) {
			System.out.println("No DAS ID found");
			throw new RuntimeException("DAS Id not passed");
		}

		feedbackService.postUserResponse(questionId, optionId, dasId);

		return buildResponse("{\"result\":\"" + "success" + "\"}");

	}

	@OPTIONS
	@Path("/question/add/userresponse/{questionId}/{optionId}")
	public Response optionPostUserResponse(
			@PathParam("questionId") long questionId,
			@PathParam("optionId") long optionId,
			@HeaderParam("Authorization") String dasId) {

		return optionsAll();
	}

	/**
	 * Retrieves all attendees based all session
	 * 
	 * @param sessionId
	 * @return
	 */
	@GET
	@Path("/user/attendance")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response exportAttendance() {
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		List<SessionDto> fetchAllSession = sessionService.fetchAllSession();

		for (SessionDto dto : fetchAllSession) {
			// Create a blank sheet
			XSSFSheet sheet = workbook.createSheet(dto.getName());
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("1", new Object[] { "NAME", "SIGN" });

			List<UserDto> fetchUsersBySession = userService
					.fetchUsersBySession(dto.getId());
			int index = 2;
			for (UserDto dto2 : fetchUsersBySession) {
				data.put(String.valueOf(index), new Object[] {
						dto2.getName().toUpperCase(), "" });
				index++;
			}

			// Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			int rownum = 0;
			for (String key : keyset) {
				Row row = sheet.createRow(rownum++);
				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}

		}

		FileInputStream fileInputStream = null;

		try {
			File archive = null;
			archive = File.createTempFile("Attendance", ".xlsx");
			archive.deleteOnExit();
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(archive);
			workbook.write(out);
			out.close();
			fileInputStream = new FileInputStream(archive);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response
				.ok(fileInputStream)
				.header("Content-Disposition",
						"inline; filename=Attendance.xlsx").build();

	}

	private boolean isRegForSameSlot(long sessionId, String dasId) {

		boolean isReg = false;

		SessionDto fetchSessionById = sessionService
				.fetchSessionById(sessionId);

		String timeSlot = fetchSessionById.getTimeSlot();

		List<SessionDto> fetchSessionBySlot = sessionService
				.fetchSessionBySlot(timeSlot);

		for (SessionDto dto : fetchSessionBySlot) {
			SessionDto fetchSessionByIdAndUser = sessionService
					.fetchSessionByIdAndUser(dto.getId(), dasId);
			if (fetchSessionByIdAndUser != null) {
				isReg = true;
				break;
			}
		}

		return isReg;
	}

	@POST
	@Path("/session/recordTeaserVote/{sessionId}/{dasId}")
	public Response recordTeaserVote(@PathParam("sessionId") String sessionId,
			@PathParam("dasId") String dasId) {

		sessionService.recordTeaserVote(Long.valueOf(sessionId), dasId);

		// returning success
		// return "{\"result\":\"" + "period_closed" + "\"}";
		return buildResponse("{\"result\":\"" + "success" + "\"}");

	}

	@OPTIONS
	@Path("/session/recordTeaserVote/{sessionId}/{dasId}")
	public Response optionRecordTeaserVote(
			@PathParam("sessionId") String sessionId,
			@PathParam("dasId") String dasId) {

		return optionsAll();

	}

	@GET
	@Path("/user/search/{dasId}")
	public Response searchUserDetails(@PathParam("dasId") String dasId) {
		String adminUser = "A506826";
		String adminPass = "Aug@2017";

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL,
				"ldaps://ldap.myatos.net:636/dc=atosorigin,dc=com");
		props.put(Context.SECURITY_PRINCIPAL, "aoLdapKey=AA" + adminUser
				+ ",ou=people,dc=atosorigin,dc=com");// adminuser - User with
														// special priviledge,
														// dn user
		props.put(Context.SECURITY_CREDENTIALS, adminPass);// dn user password

		String username = dasId;// , "a508928"; a203833 // The User you want to
								// find
		System.out.println("Searching for User with DAS ID " + username);
		InitialDirContext context = null;
		try {
			context = new InitialDirContext(props);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		SearchControls ctrls = new SearchControls();
		// ctrls.setReturningAttributes(new String[] { "givenName",
		// "sn","memberOf","mail" });
		ctrls.setReturningAttributes(new String[] { "*", "+" });
		ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answers = null;
		try {
			answers = context.search("ou=people", "(uid=" + username + ")",
					ctrls);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		UserDetailsDto dto = new UserDetailsDto();
		try {
			javax.naming.directory.SearchResult result = answers.nextElement();
			NamingEnumeration e = result.getAttributes().getAll();
			while (e.hasMoreElements()) {
				Object name = (Object) e.nextElement();
				if (name.toString().startsWith("aoLegalGivenName:")) {
					dto.setFirstName(name.toString().replace(
							"aoLegalGivenName:", ""));
				} else if (name.toString().startsWith("aoLegalSurname:")) {
					dto.setLastName(name.toString().replace("aoLegalSurname:",
							""));
				} else if (name.toString().startsWith("l:")) {
					dto.setLocation(name.toString().replace("l:", ""));
				} else if (name.toString().startsWith("street:")) {
					dto.setLocation(name.toString().replace("street:", ""));
				}

			}
		} catch (NullPointerException ex) {

		}

		if (dto.getFirstName() != null) {
			return Response
					.status(200)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers",
							"origin, content-type, accept, authorization,access_token")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods",
							"GET, POST, PUT, DELETE, OPTIONS, HEAD")
					.header("Access-Control-Max-Age", "3600").entity(dto)
					.build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@OPTIONS
	@Path("{path : .*}")
	public Response options() {
		return Response
				.ok("")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization , access_token")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "3600").build();

	}

	@GET
	@Path("/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticatUserById(@PathParam("userId") String userId,
			@HeaderParam("authorization") String authString) {
		if (!isUserAuthenticated(authString)) {
			return Response
					.status(200)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers",
							"origin, content-type, accept, authorization,access_token")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods",
							"GET, POST, PUT, DELETE, OPTIONS, HEAD")
					.header("Access-Control-Max-Age", "3600")
					.entity("{\"status\":\"403\"}").build();

		} else {
			String encUserID = new BASE64Encoder().encode(userId.getBytes());
			return Response
					.status(200)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers",
							"origin, content-type, accept, authorization,access_token")
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Methods",
							"GET, POST, PUT, DELETE, OPTIONS, HEAD")
					.header("Access-Control-Max-Age", "3600")
					.entity("{\"status\": \"" + encUserID + "\"}").build();
		}
	}

	@OPTIONS
	@Path("/user/{userId}")
	public Response optionsAthenticatUserById(
			@PathParam("userId") String userId,
			@HeaderParam("authorization") String authString) {
		return optionsAll();
	}

	private boolean isUserAuthenticated(String authString) {
		String decodedAuth = "";
		boolean isValid = false;
		// Header is in the format "Basic 5tyc0uiDat4"
		// We need to extract data before decoding it back to original string
		String[] authParts = authString.split("\\s+");
		String authInfo = authParts[1];
		// Decode the data back to original string
		byte[] bytes = null;
		try {
			bytes = new BASE64Decoder().decodeBuffer(authInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isValid = false;
			return isValid;
		}
		decodedAuth = new String(bytes);

		LdapService Ldap = new LdapService();
		System.out.println("Decoded Auth is " + decodedAuth);
		try {
			if (Ldap.doValidate(decodedAuth)) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}

		return isValid;
	}

	@POST
	@Path("/attendance/{dasId}/{room}/{name}")
	public Response markAttendance(@PathParam("dasId") String dasId,
			@PathParam("room") String room, @PathParam("name") String name) {

		attendanceService.markAttendance(dasId, name, room);

		// returning success
		// return "{\"result\":\"" + "period_closed" + "\"}";
		return buildResponse("{\"result\":\"" + "success" + "\"}");

	}

	@OPTIONS
	@Path("/attendance/{dasId}/{room}/{name}")
	public Response optionmarkAttendance(@PathParam("dasId") String dasId,
			@PathParam("room") String room, @PathParam("name") String name) {

		return optionsAll();

	}

	@OPTIONS
	@Path("/session/user/{dasId}")
	public Response optionsGetAllSessionsForUser(
			@PathParam("dasId") String dasId) {
		return optionsAll();
	}

	@OPTIONS
	@Path("/user/search/{dasId}")
	public Response optionsSearchUserDetails(@PathParam("dasId") String dasId) {
		return optionsAll();
	}

	@OPTIONS
	@Path("")
	public Response optionsAll() {
		return Response
				.ok("")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization , access_token")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "3600").build();

	}

	private Response buildResponse(Object dto) {
		return Response
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization,access_token")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "3600").entity(dto).build();

	}

}
