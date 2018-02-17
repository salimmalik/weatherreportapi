package com.gia.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//private IntegrateWithBitBucket bitBucket;

@Controller
public class BitBucketController {
	
	@RequestMapping(value = "/issue", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void sayLoginHere(ModelMap modelMap) throws ParseException {
		try {
			String webPage = "https://api.bitbucket.org/1.0/repositories/EmagiaDev/giadev/issues/";
			String name = "sivaprasad.pattela@edvenswa.in";
			String password = "prasad007";

			webPage = webPage.replaceAll(" ", "%20");

			String authStr = name + ":" + password;

			String authEncoded = Base64.getEncoder().encodeToString((authStr).getBytes("UTF-8"));

			URL url = new URL(webPage);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("GET");

			httpConn.setRequestProperty("Authorization", "Basic " + authEncoded);
			String output;
			StringBuilder responseStrBuilder = new StringBuilder();
			if (httpConn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConn.getResponseCode());
			} else {

				BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));

				while ((output = br.readLine()) != null) {
					responseStrBuilder.append(output + "\n");
				}

				System.out.println(responseStrBuilder);

			}
			JSONParser parser = new JSONParser();
			// JSONObject json = (JSONObject) parser.parse(responseStrBuilder.toString());
			// JSONObject jsonOutput = ConvertToJSON
			// .convertStringToJSON(org.json.XML.toJSONObject(responseStrBuilder.toString()).toString());
			modelMap.put("response", parser.parse(responseStrBuilder.toString()));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
