package models;

import play.Logger;
import play.db.jpa.Model;
import utils.StationUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


/**
 * This is the WeatherStation class that stores the name and readings for the Station
 * @version (27 - May - 2022)
 * @author Dave
 */
@Entity
public class Station extends Model {
  public String name;
  public Double latitude;
  public Double longitude;
  @OneToMany(cascade = CascadeType.ALL)
  public List<Reading> readings = new ArrayList<>();
  //Constructors
  public Station(String sName) {
    if (sName == null) {
      sName = "No Name";
    }
    this.name = sName;
    this.latitude = -90.00;
    this.longitude = -90.00;
  }

  public Station(String sName, Double dLatitude, Double dLongitude) {
    if (sName == null) {
      sName = "No Name";
    }
    if ((dLatitude > 90.0) || (dLatitude < -90.0)) {
      dLatitude = -90d;
    }
    if ((dLongitude > 180.0) || (dLongitude < -180.0)) {
      dLongitude = -180d;
    }

    this.name = sName;
    this.latitude = dLatitude;
    this.longitude = dLongitude;
  }

  /*CompareName subclass which uses the Arraylist Comparator function
    to allow sorting of an object array, we also here use an input variable to control the
    sorting order.
   */
  public static class CompareName implements Comparator<Station> {
    private int mod = 1;
    // constructor takes a boolean argument to assign ascending or descending order
    public CompareName(boolean desc) {
      if (desc) mod = -1;
    }

    @Override
    public int compare(Station arg0, Station arg1) {
      return mod * arg0.name.compareTo(arg1.name);
    }
  }

  public void SortByDate(Boolean xDecending) {
    Collections.sort(readings, new Reading.CompareLogDate(xDecending));
    xReadingsDateDec = xDecending;
  }

  public boolean ReadingsSortedbyDateDec() {
    return xReadingsDateDec;
  }

  public boolean ReadingsSortedbyDateAsc() {
    return !xReadingsDateDec;
  }

  private static Boolean xReadingsDateDec = false;


  private Reading currentReading() {
    SortByDate(true);
    return readings.get(0);
  }

  /**
   * Return current pressure as string in Deg C and F based on Appendix A - ii of specifications
   *
   */
  public String currentPressure() {
    String sReturn = "---";
    try {
      Reading lastReading = currentReading();
      sReturn = lastReading.pressure + " hPa ";
    } catch (Exception eX) {
      Logger.error("currentPressure Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return max pressure as string in Deg C and F based on Appendix A - ii of specifications
   */
  public String maxPressure() {
    String sReturn = "---";
    try {
      Integer iMaxPressure = readings.get(0).pressure;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).pressure > iMaxPressure) {
          iMaxPressure = readings.get(iX).pressure;
        }
      }
      sReturn = iMaxPressure + " hPa ";
    } catch (Exception eX) {
      Logger.error("maxPressure Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return min pressure as string in Deg C and F based on Appendix A - ii of specifications
   */
  public String minPressure() {
    String sReturn = "---";
    try {
      Integer iMinPressure = readings.get(0).pressure;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).pressure < iMinPressure) {
          iMinPressure = readings.get(iX).pressure;
        }
      }
      sReturn = iMinPressure + " hPa ";
    } catch (Exception eX) {
      Logger.error("minPressure Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return max temperiture as string in Deg C and F based on Appendix A - ii of specifications
   *
   */
  public String maxTemp() {
    String sReturn = "---";
    try {
      Double iMaxTemp = readings.get(0).temperature;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).temperature > iMaxTemp) {
          iMaxTemp = readings.get(iX).temperature;
        }
      }
      sReturn = iMaxTemp + "  Deg C  ";
    } catch (Exception eX) {
      Logger.error("maxTemp Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return min temperature as string in Deg C and F based on Appendix A - ii of specifications
   *
   */
  public String minTemp() {
    String sReturn = "---";
    try {
      double iMinTemp = readings.get(0).temperature;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).temperature < iMinTemp) {
          iMinTemp = readings.get(iX).temperature;
        }
      }
      sReturn = iMinTemp + " Deg C ";
    } catch (Exception eX) {
      Logger.error("minTemp Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return current Temp as string in Deg C and F based on Appendix A - ii of specifications
   *
   */
  public String currentTemp() {
    String sReturn = "---";
    try {
      Reading lastReading = currentReading();
      sReturn = lastReading.temperature + " C " + StationUtilities.CelsiusToFahrenheit(lastReading.temperature) + " F";
    } catch (Exception eX) {
      Logger.error("currentTemp Error " + eX.getMessage());
    }
    return sReturn;
  }

  public Double currentTempValue() {
    Double dReturn = 0.0;
    try {
      Reading lastReading = currentReading();
      dReturn = lastReading.temperature;
    } catch (Exception eX) {
      Logger.error("currentTempValue Error " + eX.getMessage());
    }
    return dReturn;
  }

  /*
   * Return current Compass Direction as string in Cardinal Points based on Appendix A - iv of specifications
  */
  public String currentWindCompass() {
    String sReturn = "---";
    try {
      Reading lastReading = currentReading();
      Integer iWindDir = lastReading.windDirection;
      sReturn = StationUtilities.sCompasHeading(iWindDir);
    } catch (Exception eX) {
      Logger.error("currentWindCompass Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return current wind chill as string Based Appendix A - v of specifications
   *
   */
  public String currentWindChill() {
    String sReturn = "---";
    try {
      Reading lastReading = currentReading();
      double dWindKph = lastReading.windSpeed;
      double dTemperature = lastReading.temperature;
      double dWindChill = 13.12 + (0.6215 * dTemperature) - 11.37 * (Math.pow(dWindKph, 0.16)) + 0.3965 * dTemperature * Math.pow(dWindKph, 0.16);//funny cals but ok
      sReturn = String.format("Feels like %.2f", dWindChill); // Limit number to 2 decimal places fro display
    } catch (Exception eX) {
      Logger.error("currentWindChill Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return min win speed as string in kph based on Appendix A - ii of specifications
   *
   */
  public String minWindSpeed() {
    String sReturn = "---";
    try {
      double dMinSpeed = readings.get(0).windSpeed;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).windSpeed < dMinSpeed) {
          dMinSpeed = readings.get(iX).windSpeed;
        }
      }
      sReturn = dMinSpeed + " kph ";
    } catch (Exception eX) {
      Logger.error("minWindSpeed Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return max wind speed as string in kph based on Appendix A - ii of specifications
   *
   */
  public String maxWindSpeed() {
    String sReturn = "---";
    try {
      double dMaxSpeed = readings.get(0).windSpeed;
      for (Integer iX = 1; iX < readings.size(); iX++) {
        if (readings.get(iX).windSpeed > dMaxSpeed) {
          dMaxSpeed = readings.get(iX).windSpeed;
        }
      }
      sReturn = dMaxSpeed + " kph ";
    } catch (Exception eX) {
      Logger.error("maxWindSpeed Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*Return current Wind as string Beaufort based on Appendix A - iii of specifications
   *
   */
  public String currentWind(Boolean xAsLabel) {
    String sReturn = "---";
    try {
      Reading lastReading = currentReading();
      Double dWS = lastReading.windSpeed;
      sReturn =StationUtilities.sBeauFortFromKph(dWS,xAsLabel);
    } catch (Exception eX) {
      Logger.error("currentWind Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*Return current conditions as string based on Appendix A - i of specifications
   */
  public String currentConditions() {
    String sReturn = "";
    try {
      Reading lastReading = currentReading();
      sReturn=StationUtilities.sConditionsFromCode(lastReading.code);
    } catch (Exception eX) {
      Logger.error("currentConditions Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*
   * Return Integer representing Trend of temp values Integer + Positive, - Negative , 0 Static
   */

  //set max number of readings to trend
  public Integer trendTemperature(Integer iNoOfReadings) {
    Integer iReturnValue = 0;
    ArrayList<Double> xValues; //date time stamp
    ArrayList<Double> yValues; //Value
    ArrayList<Reading> aReadings;// array of readings
    xValues = new ArrayList<>();
    yValues = new ArrayList<>();

    // sort by date in descending order for trending last x readings
    Collections.sort(this.readings, new Reading.CompareLogDate(true));
    //Check so we trend only what we have if we have less than the requested number of readings
    if (this.readings.size() < iNoOfReadings) {
      iNoOfReadings = this.readings.size();
    }
    for (Integer iX = 0; iX < iNoOfReadings; iX++) {
      Reading mReading = this.readings.get(iX);
      yValues.add(mReading.temperature);
      xValues.add(Double.valueOf(mReading.getId()));
    }
    Double dTrendValue = StationUtilities.dTrend(xValues, yValues);
    if (dTrendValue > 0) {
      iReturnValue = 1;
    } else if (dTrendValue < 0) {
      iReturnValue = -1;
    } else {
      iReturnValue = 0;
    }

    return iReturnValue;
  }

  /*
   * Return Integer representing Trend of Pressure values Integer + Positive, - Negative , 0 Static
   *
   */

  public Integer trendPressure(Integer iNoOfReadings) {
    Integer iReturnValue = 0;
    ArrayList<Double> xValues; //date time stamp
    ArrayList<Double> yValues; //date time stamp

    xValues = new ArrayList<>();
    yValues = new ArrayList<>();
    // sort by date in descending order for trending last x readings
    Collections.sort(this.readings, new Reading.CompareLogDate(true));
    //Check so we trend only what we have if we have less than the requested number of readings
    if (this.readings.size() < iNoOfReadings) {
      iNoOfReadings = this.readings.size();
    }
    for (Integer iX = 0; iX < iNoOfReadings; iX++) {
      Reading mReading = this.readings.get(iX);
      yValues.add(Double.valueOf(mReading.pressure));
      xValues.add(Double.valueOf(mReading.getId()));
    }
    Double dTrendValue = StationUtilities.dTrend(xValues, yValues);
    if (dTrendValue > 0) {
      iReturnValue = 1;
    } else if (dTrendValue < 0) {
      iReturnValue = -1;
    } else {
      iReturnValue = 0;
    }

    return iReturnValue;
  }


  /*
   * Return Integer representing Trend of Wind speed values Integer + Positive, - Negative , 0 Static
   *
   */

  public Integer trendWind(Integer iNoOfReadings) {
    Integer iReturnValue = 0;
    ArrayList<Double> xValues; //date time stamp
    ArrayList<Double> yValues; //date time stamp

    xValues = new ArrayList<>();
    yValues = new ArrayList<>();
    // sort by date in descending order for trending last x readings
    Collections.sort(this.readings, new Reading.CompareLogDate(true));
    //Check so we trend only what we have if we have less than the requested number of readings
    if (this.readings.size() < iNoOfReadings) {
      iNoOfReadings = this.readings.size();
    }
    for (Integer iX = 0; iX < iNoOfReadings; iX++) {
      Reading mReading = this.readings.get(iX);
      yValues.add(mReading.windSpeed);
      xValues.add(Double.valueOf(mReading.getId()));
    }
    Double dTrendValue = StationUtilities.dTrend(xValues, yValues);
    if (dTrendValue > 0) {
      iReturnValue = 1;
    } else if (dTrendValue < 0) {
      iReturnValue = -1;
    } else {
      iReturnValue = 0;
    }

    return iReturnValue;
  }


}
