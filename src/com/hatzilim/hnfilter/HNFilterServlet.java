package com.hatzilim.hnfilter;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class HNFilterServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		System.out.println("running fetch");
		resp.setContentType("text/plain");
		ObjectifyService.register(Item.class);
            
        try {
        	 
        	SAXParserFactory factory = SAXParserFactory.newInstance();
        	SAXParser saxParser = factory.newSAXParser();
        	
        	DefaultHandler handler = new DefaultHandler() {
        		
        	boolean btitle = false;
        	boolean bhref = false;
        	boolean bdate = false;
        	Item item = null;
        	
         
        	public void startElement(String uri, String localName,String qName, 
                        Attributes attributes) throws SAXException {
        		if (qName.equalsIgnoreCase("TITLE")) {
        			btitle = true;
        		}
        		if (qName.equalsIgnoreCase("LINK")) {
        			bhref = true;
        		}
        		if (qName.equalsIgnoreCase("ITEM")) {
        			item = new Item();
        		}
        		if (qName.equalsIgnoreCase("pubDate")) {
        			bdate = true;
        		}
        	}
         
        	public void endElement(String uri, String localName,
        		String qName) throws SAXException {
        		if (qName.equalsIgnoreCase("ITEM")) {
        			// time to persist the item. check if the link exists
        			Item itemExists = ofy().load().type(Item.class).filter("url", item.getUrl()).first().now();
        			if (itemExists == null) {
        				ofy().save().entity(item).now();
        			}  
        		}
        	}
         
        	public void characters(char ch[], int start, int length) throws SAXException {
        		if (btitle) {
        			if (item != null) item.setTitle(new String(ch, start, length));
        			btitle = false;
        		}
        		if (bhref) {
        			if (item != null) item.setUrl(new String(ch, start, length));
        			bhref = false;
        		}
        		if (bdate) {
        			try {
						Date dd = (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH))
								.parse(new String(ch, start, length));
						if (item != null) item.setCreated(dd);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			bdate = false;
        		}
        	}
         
             };
         
             
              saxParser.parse("http://feeds.feedburner.com/newsyc20?format=xml", handler);
         
         } catch (Exception e) {
           e.printStackTrace();
         }
		
		
	}
}
