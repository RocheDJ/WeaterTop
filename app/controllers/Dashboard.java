package controllers;

import play.Logger;
import play.mvc.Controller;
import models.Station;

import java.util.List;

public class Dashboard extends Controller
{
  public static void index() {
    Logger.info("Rendering Dashboard");

    List<Station> stationlist = Station.findAll();// create a list of all the stations in out station list

    render ("dashboard.html",stationlist);
  }

  public static void addStation(String name)
  {
    Station station = new Station(name);
    Logger.info ("Adding a new station called " + name);
    station.save();
    redirect ("/dashboard");
  }

}
