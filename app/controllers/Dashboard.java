package controllers;

import models.Member;
import org.hibernate.engine.internal.Collections;
import play.Logger;
import play.mvc.Controller;
import models.Station;

import java.util.List;

public class Dashboard extends Controller {
  public static void index() {
    if(session.isEmpty()){
      render("login.html");
    }else{
      Logger.info("Rendering Dashboard");
      Member member = Accounts.getLoggedInMember();
      List<Station> stations = member.stations;
      stations.sort(new Station.CompareName(false));
      render("dashboard.html", member, stations);
    }
  }

  public static void addStation(String name, Double latitude, Double longitude) {
    Logger.info("Adding a new station called " + name + "@" + latitude + ":" + longitude);
    Station station = new Station(name, latitude, longitude);
    station.save();
    Member member = Accounts.getLoggedInMember();
    member.stations.add(station);
    member.save();
    List<Station> stations = member.stations;
    stations.sort(new Station.CompareName(false));
    render("dashboard.html", member, stations);
  }

}
