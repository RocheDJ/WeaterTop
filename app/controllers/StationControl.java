package controllers;

import models.Reading;
import play.Logger;
import play.mvc.Controller;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Station;

public class StationControl extends Controller {

  public static void index(Long id) {
    if(session.isEmpty()){
      render("login.html");
    }else {
      Station station = Station.findById(id);
      Logger.info("Selected Station id = " + id);
      render("station.html", station);
    }
  }


  public static void delete(Long id) {
    Station station = Station.findById(id);
    Logger.info("Removing " + station.name);
    station.delete();
    List<Station> stationlist = Station.findAll();// create a list of all the stations in out station list
    stationlist.sort(new Station.CompareName(false));
    render("dashboard.html", stationlist);
  }


  public static void addReading(Long id, Integer code, Double temperature, Double windspeed, Integer winddirection,
                                Integer pressure) {
    String sDate;
    //Get the local date time as an object
    LocalDateTime oDateObj = LocalDateTime.now();
    //format that date and time as string
    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    sDate = oDateObj.format(inputFormat);
    Reading reading = new Reading(code, temperature, windspeed, pressure, winddirection, sDate);
    Station station = Station.findById(id);
    station.readings.add(reading);
    station.save();
    redirect("/station/" + id);
  }

  public static void deleteReading(Long id, long lReadingID) {
    Logger.info("Delete Reading from Station id = " + id + " Reading id = " + lReadingID);
    Station oStation = Station.findById(id); // get the station
    Reading oReading = Reading.findById(lReadingID);//get the reading
    oStation.readings.remove(oReading);//remove the reading by passing the object to remove
    oStation.save();//save the station
    redirect("/station/" + id);
  }
}



