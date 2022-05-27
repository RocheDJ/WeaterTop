package controllers;

import models.Member;
import org.hibernate.engine.internal.Collections;
import play.Logger;
import play.mvc.Controller;
import models.Station;

import java.util.List;

/**
 * This is the Dashboard class that handles station
 * display(index)
 *
 * @author Dave
 * @version (27 - May - 2022)
 */
public class Dashboard extends Controller {
  public static void index() {
    if (session.isEmpty()) {
      render("login.html");
    } else {
      Logger.info("Rendering Dashboard");
      //get the logged in member
      Member member = Accounts.getLoggedInMember();
      // load the list of station for that member
      List<Station> stations = member.stations;
      //if there are no stations for that member add a demo one.
      if (stations.size() == 0) {
        Station station = new Station("Demo Station", 52.2464056, -7.1386607);
        station.save();
        member.stations.add(station);
        member.save();
      }
      stations.sort(new Station.CompareName(false));
      render("dashboard.html", member, stations);
    }
  }
}
