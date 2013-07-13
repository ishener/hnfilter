package com.hatzilim.hnfilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class CrawlJson extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		try {
            URL url = new URL("http://api.thriftdb.com/api.hnsearch.com/items/_search?filter[fields][create_ts]=[2013-01-01T00:00:00Z%20+%20TO%20+%20*]");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            String inJson = sb.toString();
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<String, Object>();
            map = (Map<String, Object>) gson.fromJson(inJson, map.getClass());
            ArrayList all = new ArrayList();
            all = (ArrayList) map.get("results");
//            for (Object item : ( map.get("results") )) {
//            	
//            }
            
            resp.getWriter().print(all.get(0));

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
	}

}
