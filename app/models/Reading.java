package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * This is a Reading class that stores the reading from the Weather Station
 *
 * @version (03 - May - 2022)
 */
@Entity
public class Reading extends Model {
  public LocalDateTime date;
  public Integer code;
  public Double temperature;
  public Double windSpeed;
  public Integer pressure;
  public Integer windDirection;
  public Long epocDateSeconds;// date, time stamp in Seconds since 00:00 1/1/1970

  public Reading(Integer iCode,
                 Double dTemperature,
                 Double dWindSpeed,
                 Integer iPressure,
                 Integer iWindDirection,
                 String sDate) {
    try {
      if (iCode > 0) {
        this.code = iCode;
      }
      if (dTemperature != null) {
        this.temperature = dTemperature;
      } else {
        this.temperature = 0d;
      }
      if (dWindSpeed != null) {
        if (dWindSpeed < 0) {
          dWindSpeed = 0d;
        }
      } else {
        dWindSpeed = 0d;
      }
      this.windSpeed = dWindSpeed;
      if (iPressure == null) {
        iPressure = 1013; // nominal pressure
      } else if (iPressure > 2000) {
        iPressure = 2000; //max pressure on earth ever recorded was 1084
      } else if (iPressure < 500) {
        iPressure = 500;//870 would be in a tornado
      }
      this.pressure = iPressure;

      // Clamp the wind Direction 0 to 360 degrees
      if ((iWindDirection == null) || (iWindDirection < 0)) {
        iWindDirection = 0;
      } else if (iWindDirection > 360) {
        iWindDirection = 360;
      }

      this.windDirection = iWindDirection;
      if (sDate == null) {
        sDate = "2000-01-01 00:00:00";
      }
      setDate(sDate);
    } catch (Exception eX) {
      //add dummy data to db
      this.code = 0;
      this.temperature = 0d;
      this.windSpeed = 0d;
      this.pressure = 0;
      this.windDirection = 0;
      setDate("2000-01-01 00:00:00");
    }
  }

  //adapted from post on https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
  //based on arraylist Comparator
  public static class CompareLogDate implements Comparator<Reading> {
    private int mod = 1;

    public CompareLogDate(boolean desc) {
      if (desc) mod = -1;
    }

    @Override
    public int compare(Reading arg0, Reading arg1) {
      return mod * arg0.epocDateSeconds.compareTo(arg1.epocDateSeconds);
    }
  }

  public String getDate() {
    String sReturnValue;
    DateTimeFormatter oFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    sReturnValue = this.date.format(oFormatObj);
    return sReturnValue;
  }

  public void setDate(String sDate) {
    String sTempDate;
    //Strip out all the DT format additions from the yml file string
    sTempDate = sDate.replace("ISO8601:", "");
    sTempDate = sTempDate.replace("T", " ");
    sTempDate = sTempDate.replace("+0000", "");
    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //save actual Dt format
    this.date = LocalDateTime.parse(sTempDate, inputFormat);
    ZoneId zoneId = ZoneId.systemDefault();
    this.epocDateSeconds = this.date.atZone(zoneId).toEpochSecond();
  }

}
