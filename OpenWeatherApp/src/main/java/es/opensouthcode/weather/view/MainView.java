package es.opensouthcode.weather.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.opensouthcode.weather.controller.WeatherService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringUI(path = "eltiempo")
public class MainView extends UI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private WeatherService weatherService;
	
	private VerticalLayout mainLayout;
	private TextField citytextField;
	private Button showWeatherButton;
	private Label currentLocationTitle;
	private Label currentTemp;
	private Image iconImage;
	private HorizontalLayout dashBoadMain;
	

	@Override
	protected void init(VaadinRequest request) {
		setUpLayout();
		setHeader();
		setLogo();
		setupForm();
		dashBoardMain();
	}



	private void setUpLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		setContent(mainLayout);
	}
	
	private void setHeader() {
		HorizontalLayout headerLayot = new HorizontalLayout();
		headerLayot.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label title = new Label("OpenWeather");
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		
		headerLayot.addComponent(title);
		
		mainLayout.addComponent(headerLayot);
	}
	
	private void setLogo() {
		HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Image logo = new Image("", new ClassResource("/weather_icon.png"));
		logo.setWidth("125px");
		logo.setHeight("125px");
		
		logoLayout.addComponent(logo);
		
		mainLayout.addComponent(logoLayout);
	}

	private void setupForm() {
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		
		citytextField = new TextField();
		citytextField.setWidth("80%");
		citytextField.setPlaceholder("Introduce una ciudad");
		formLayout.addComponent(citytextField);
		
		showWeatherButton = new Button();
		showWeatherButton.setIcon(VaadinIcons.SEARCH);
		showWeatherButton.addClickListener(event -> {
			if(!citytextField.getValue().isEmpty()) {
				try {
					updateUI();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Notification.show("Indica una ciudad");
			}
			
		});
		formLayout.addComponent(showWeatherButton);
		
		mainLayout.addComponent(formLayout);
	}
	
	private void dashBoardMain() {
		dashBoadMain = new HorizontalLayout();
		dashBoadMain.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		currentLocationTitle = new Label();
		currentLocationTitle.addStyleName(ValoTheme.LABEL_H2);
		currentLocationTitle.addStyleName(ValoTheme.LABEL_LIGHT);
		
		iconImage = new Image();
		iconImage.setWidth("100px");
		iconImage.setHeight("100px");
		
		currentTemp = new Label();
		currentTemp.addStyleName(ValoTheme.LABEL_H1);
		currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
		currentTemp.addStyleName(ValoTheme.LABEL_LIGHT);
		
		dashBoadMain.addComponents(currentLocationTitle, iconImage, currentTemp);
		
		mainLayout.addComponent(dashBoadMain);
	}

	private void updateUI() throws JSONException {
		String city = citytextField.getValue();
		currentLocationTitle.setValue("Actualmente en " + city);
		JSONObject mainObject = weatherService.returnMainObject(city);
		int temp = mainObject.getInt("temp");
		
		currentTemp.setValue(temp + " ºC");
		
		String iconCode = null;
		JSONArray jsonArray = weatherService.returnWeatherArray(city);
		for(int i=0; i< jsonArray.length(); i++){
			JSONObject weatherObject = jsonArray.getJSONObject(i);
			log.debug(weatherObject.toString());
			iconCode = weatherObject.getString("icon");
		}
		
		iconImage.setSource( new ExternalResource("http://openweathermap.org/img/w/" + iconCode +".png"));
	}
}
