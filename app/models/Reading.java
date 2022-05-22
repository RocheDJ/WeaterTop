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
    this.code = iCode;
    this.temperature = dTemperature;
    this.windSpeed = dWindSpeed;
    this.pressure = iPressure;
    this.windDirection = iWindDirection;
    setDate(sDate);
  }


  //adapted from post on https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
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
    String sReturnValue = "Invalid";
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
