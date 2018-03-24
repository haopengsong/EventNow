package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBConnection conn = DBConnectionFactory.getDBConnection();
		//doget : 看用户有没有登录
		try {
			JSONObject obj = new JSONObject();
			HttpSession session = request.getSession(false); //记录用户登录状态，记录于session内，额外的信息，保证用户登录状态存在于tomcat
															//服务器自动产生，传回浏览器
			if (session == null) { //没session，没登录，返回invalid
				response.setStatus(403);
				obj.put("status", "Session Invalid");
			} else { // 有session，已登录，返回ok
				String userid = (String) session.getAttribute("user_id");
				String name = conn.getFullname(userid);
				obj.put("status", "OK");
				obj.put("user_id", userid);
				obj.put("name", name);
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//建立登录的session
		DBConnection conn = DBConnectionFactory.getDBConnection();		
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userid = input.getString("user_id");
			String pwd = input.getString("password");
			JSONObject obj = new JSONObject();
			
			if (conn.verifyLogin(userid, pwd)) {
				HttpSession session = request.getSession();
				session.setAttribute("user_id", userid);
				session.setMaxInactiveInterval(10 * 60);
				String name = conn.getFullname(userid);
				obj.put("user_id", userid);
				obj.put("name", name);
				System.out.println(name);
				obj.put("status", "OK");
				
			} else {
				response.setStatus(401);
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
