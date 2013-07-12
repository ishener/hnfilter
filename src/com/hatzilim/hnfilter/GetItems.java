package com.hatzilim.hnfilter;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

@SuppressWarnings("serial")
public class GetItems extends HttpServlet {
	
	public static final int MESSAGE_BATCH = 30;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		ObjectifyService.register(Item.class);
		Query<Item> query = ofy().load().type(Item.class).order("-created").limit(MESSAGE_BATCH);
		
		String cursor = req.getParameter("start");
		if (cursor != null) 
			query = query.startAt(Cursor.fromWebSafeString(cursor));
		
		List<Item> items = new ArrayList<Item>();
		QueryResultIterator<Item> iterator = query.iterator();
	    while (iterator.hasNext()) {
	    	items.add(iterator.next());
	    }
	    
	    Map<String, Object> itemBatch = new HashMap<String, Object>();
	    itemBatch.put("items", items);
	    itemBatch.put("cursor", iterator.getCursor().toWebSafeString());
		
		Gson gson = new Gson();
		resp.setContentType("application/json");
		resp.getWriter().print(gson.toJson(itemBatch));
	}
}

