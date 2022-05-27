package controllers;

import models.Member;
import models.Reading;
import play.Logger;
import play.mvc.Controller;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;
import java.util.List;

import models.Station;

/**
 * Station control Class  to
 * Display Stations (index)
 * Add a Station
 * Delete a station,
 * Add a reading
 * Delete a reading
 *
 * @author Dave
 * @version (27 - May - 2022)
 */
public class StationControl extends Controller {

  public static void index(Long id) {
    if (session.isEmpty()) {
      render("login.html");
    } else {
      Station station = Station.findById(id);
      Logger.info("Selected Station id = " + id);
      render("station.html", station);
    }
  }

  public static void addStation(String name, Double latitude, Double longitude) {
    Logger.info("Adding a new station called " + name + "@" + latitude + ":" + longitude);
    //add the station
    Station station = new Station(name, latitude, longitude);
    station.save();
    //load the member details
    Member member = Accounts.getLoggedInMember();
    //save station to member
    member.stations.add(station);
    member.save();
    ///load and sort the new station list
    List<Station> stations = member.stations;
    stations.sort(new Station.CompareName(false));
    // open the page
    render("dashboard.html", member, stations);
  }

  public static void delete(Long id) {
    // need to delete the station from the member before deleting from list
    Station station = Station.findById(id);
    Logger.info("Removing " + station.name);
    //load the member details
    Member member = Accounts.getLoggedInMember();
    //remove station from member
    member.stations.remove(station);
    member.save();
    //delete the station
    station.delete();
    //load the members stations
    List<Station> stations = member.stations;
    stations.sort(new Station.CompareName(false));
    render("dashboard.html", member, stations);
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



