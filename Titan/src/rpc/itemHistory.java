package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class itemHistory
 */
@WebServlet("/history")
public class itemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public itemHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		// TODO Auto-generated method stub
		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();
		DBConnection conn = DBConnectionFactory.getDBConnection();
		Set<Item> items = conn.getFavoriteItems(userId);
		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			try {
				obj.append("favorite", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String user_id = input.getString("user_id");
			JSONArray array = input.getJSONArray("favorite");
			List<String> itemIDs = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String item_id = array.get(i).toString();
				itemIDs.add(item_id);
			}
			DBConnection conn = DBConnectionFactory.getDBConnection();
			conn.setFavoriteItems(user_id, itemIDs);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String user_id = input.getString("user_id");
			JSONArray array = input.getJSONArray("favorite");
			List<String> itemIDs = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String item_id = array.get(i).toString();
				itemIDs.add(item_id);
			}
			DBConnection conn = DBConnectionFactory.getDBConnection();
			conn.unsetFavoriteItems(user_id, itemIDs);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
