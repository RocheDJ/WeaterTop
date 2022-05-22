package controllers;

import models.Member;
import play.Logger;
import play.mvc.Controller;
import models.Station;

import java.util.List;

public class Dashboard extends Controller {
    public static void index() {
        Logger.info("Rendering Dashboard");
        Member member = Accounts.getLoggedInMember();
        List<Station> stations = member.stations;

        render("dashboard.html", member,stations);
    }

    public static void addStation(String name, Double latitude, Double longitude) {
        Station station = new Station(name, latitude, longitude);
        Logger.info("Adding a new station called " + name + "@" + latitude + ":" + longitude);
        station.save();
        redirect("/dashboard");
    }

}
