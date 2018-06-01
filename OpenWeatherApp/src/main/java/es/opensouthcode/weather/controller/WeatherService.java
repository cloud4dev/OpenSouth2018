package es.opensouthcode.weather.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
@Service
public class WeatherService {
	
	private OkHttpClient client;
	private Response response;
	
	 @Value("${apikey.weather}")
	 @Getter private String apikey;
	
	
	
	public JSONObject getWeather(String name) {
		String url = "http://api.openweathermap.org/data/2.5/weather?q=" + name  + "&units=metric&appid=" + apikey;
		log.debug("request ->" + url);
		client = new OkHttpClient();
		Request request = new Request.Builder()
					.url(url)
					.build();
		
		try {
			response = client.newCall(request).execute();
			return new JSONObject(response.body().string());
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public JSONArray returnWeatherArray(String name) throws JSONException {
		JSONArray weatherJsonArray = getWeather(name).getJSONArray("weather");
		return weatherJsonArray;
		
	}
	
	public JSONObject returnMainObject(String name) throws JSONException {
		JSONObject mainObject = getWeather(name).getJSONObject("main");
		return mainObject;
	}
	
	public JSONObject returnWindObject(String name) throws JSONException {
		JSONObject windObject = getWeather(name).getJSONObject("wind");
		return windObject;
	}
	
	public JSONObject returnSunsetObject(String name) throws JSONException {
		JSONObject sunObject = getWeather(name).getJSONObject("sys");
		return sunObject;
	}

}
