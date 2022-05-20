package controllers;
import models.Reading;
import play.Logger;
import play.mvc.Controller;

import java.util.List;
import models.Station;

public class StationControl extends Controller {

    public static void index(Long id)
    {
        Station station = Station.findById(id);
        Logger.info ("Selected Station id = " + id);
        render("station.html", station);
    }

    public static void delete (Long id)
    {
        //check out fromantic modal forms to confirm these actions....
        Station station = Station.findById(id);
        Logger.info ("Removing " + station.name);
        station.delete();
        List<Station> Stations = station.findAll();
        render ("dashboard.html", Stations);
    }


    public static void addReading(Long id,Integer code, Double temperature,Double windspeed, Integer winddirection,
                                  Integer pressure)
    {
        Reading  reading= new Reading(code,temperature,windspeed,pressure,winddirection);
        Station station = Station.findById(id);
        station.readings.add(reading);
        station.save();
        redirect ("/station/" + id);
    }


}



