package com.hatzilim.hnfilter;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class EditItem extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		ObjectifyService.register(Item.class);
		
		String id = req.getParameter("i");
		
		if (id != null) {
			Item item = ofy().load().type(Item.class).id(Long.parseLong(id)).now();
			
		}
		
	}

}
