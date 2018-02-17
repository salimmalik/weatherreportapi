package com.gia.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gia.controller.WeatherDataServiceFactory.service;
import com.gia.models.SummaryData;
import com.gia.utils.MailHelper;

@Controller
public class Conversation {
	
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}
	
	
	@RequestMapping(value = "/totalAmountDue", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public void totalAmountDue(ModelMap modelMap) {
		try {
			
		} catch (Exception e) {

		}
		modelMap.put("data", "10000");
	}
	
	
	@RequestMapping(value = "/summaryMail", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public void subscribeTourCoins(ModelMap modelMap, @RequestBody SummaryData data) {
		try {
				List<String> myList = new ArrayList<String>(Arrays.asList(data.getSummaryString().substring(1, data.getSummaryString().length()-1).split(",")));
				myList.remove(myList.size() - 1);
				final String userEmailLowerCase = "ss@edvenswa.in";
					Executors.newSingleThreadExecutor().execute(new Runnable() {
						@Override
						public void run() {
							MailHelper mailer = MailHelper.getInstance();
							try {
								mailer.sendingSummaryTemplate(userEmailLowerCase, myList, data.getImageUrl());
								modelMap.put("Success", "Mail Sent Succesfully");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					modelMap.put("success", "success");
		} catch (Exception e) {
			System.out.println("catch");
			e.printStackTrace();

		}

	}
	
	/*@RequestMapping(value = "/extService", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public String getData(ModelMap modelMap) {
		String output ="";
		String url = "http://10.1.100.197:9109/ewm/orderHeader/loadDeliveryTktNum?warehouseId=98403&customerSysId=109282&consSysId=&edi=false";
		String type = "GET";
		try {
			String name = "euscold";
			String password = "euscold";
			System.out.println("webPage : " + url);
			String authStr = name + ":" + password;
			url = url.replaceAll(" ", "%20");
			String authEncoded = Base64.getEncoder().encodeToString((authStr).getBytes("UTF-8"));
			URL formattedUrl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) formattedUrl.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod(type);
			httpConn.setRequestProperty("Authorization", "Basic " + authEncoded);
			httpConn.setRequestProperty("Accept", "application/json");
			
			if (httpConn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConn.getResponseCode());
			} else {
				StringBuilder responseStrBuilder = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));

				if ((output = br.readLine()) != null) {
					responseStrBuilder.append(output);
				}
				System.out.println("output:::::" + output);
				//return output;
				modelMap.put("data", output);
			}
			
		} catch (Exception e) {

		}
		return output;
	}
	*/

	@RequestMapping(value = "/getWeather", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public WeatherData getWeather(ModelMap modelMap) {
		IWeatherDataService dataService = WeatherDataServiceFactory.getWeatherDataService(service.OPEN_WEATHER_MAP);
		WeatherData data = null;
		try {
				data = dataService.getWeatherData(new Location("Chicago", "USA"));
				System.out.println(data.getTemperature().getValue() + " :::: " + data.getWeather().getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//modelMap.put("data", data);
		return data;
	}
}

	

