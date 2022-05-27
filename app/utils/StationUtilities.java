package utils;

import play.Logger;

import java.util.ArrayList;
import java.util.List;

public class StationUtilities {

  public static Double CelsiusToFahrenheit(Double dCelcious) {
    return dCelcious * (9 / 5) + 32;
  }

  /*
   * Return average Value of a list of doubles
   */
  private static double average(List<Double> lstDouble) {
    Double dReturn;
    Double dTotal;
    dTotal = 0d;
    for (Integer iX = 0; iX < lstDouble.size(); iX++) {
      dTotal = dTotal + lstDouble.get(iX);
    }
    dReturn = dTotal / lstDouble.size();
    return dReturn;
  }

  /*
   * Return the slope or trend for an array code based on formula outlined in
   * https://study.com/academy/lesson/what-is-a-trend-line-in-math-definition-equation-analysis.html
   */
  public static double dTrend(ArrayList<Double> yList, ArrayList<Double> xList) {
    Double dReturnValue;
    dReturnValue = 0d; //0d sets the value to 0 as double same as 0.0

    //get the average of the x coordinates (time stamp)
    double yAvg = average(yList);
    //get the average of the y coordinates (the value we are trending)
    double xAvg = average(xList);

    double sumNumerator = 0d;
    double sumDenominator = 0d;
    //for each point get
    for (int i = 0; i < yList.size(); i++) {
      //the differences between each y-coordinate and the average of all of the y-coordinates
      double y = yList.get(i);
      // the differences between each x-coordinate and the average of all of the x-coordinates
      double x = xList.get(i);
      double yDiff = y - yAvg;
      double xDiff = x - xAvg;
      // multiply columns 1 and 2
      double numerator = xDiff * yDiff;
      double denominator = xDiff * xDiff;
      sumNumerator += numerator;
      sumDenominator += denominator;
    }
    //Calculate the slope (m) of your trend line by dividing the total for Column 3 by the total for Column 4
    double dSlope = sumNumerator / sumDenominator;

    dReturnValue = dSlope;
    return dReturnValue;
  }

  final private static String[] Windsector = new String[]{"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
      "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};

  /*
  * Return The string compass heading from decimal degrees
    Construct an array of strings that are the wind directions
     converted from example on https://www.campbellsci.com/blog/convert-wind-directions
  */
  public static String sCompasHeading(Integer iWindDir) {
    String sReturn = "---";
    /* from https://www.campbellsci.com/blog/convert-wind-directions
         convert wind direction to integer values that correspond with the 17 index values within our array.
        */
    try {
      Integer iIndex;
      if (iWindDir > 360) {
        iIndex = iWindDir % 360; // use modular division to get remainder if >360
      } else {
        iIndex = iWindDir;
      }
      double dIndex = (iIndex / 22.5);
      iIndex = (int) Math.round(dIndex);
      sReturn = Windsector[iIndex];
    } catch (Exception eX) {
      Logger.error("sCompasHeading Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*Return the Beaufort String or force value from the wind speed
   */
  public static String sBeauFortFromKph(double dWindSpeed, Boolean xAsLabel) {
    String sReturn = "---";
    try {
      //start from max and work down returning after each check
      if (dWindSpeed >= 103) {
        if (xAsLabel) {
          return "Violent Storm";
        } else {
          return "Force 11";
        }
      }
      if (dWindSpeed >= 89) {
        if (xAsLabel) {
          return "Strong storm";
        } else {
          return "Force 10";
        }
      }
      if (dWindSpeed >= 75) {
        if (xAsLabel) {
          return "Severe Gale";
        } else {
          return "Force 9";
        }
      }
      if (dWindSpeed >= 62) {
        if (xAsLabel) {
          return "Gale";
        } else {
          return "Force 8";
        }
      }
      if (dWindSpeed >= 50) {
        if (xAsLabel) {
          return "Near Gale";
        } else {
          return "Force 7";
        }
      }
      if (dWindSpeed >= 39) {
        if (xAsLabel) {
          return "Strong Breeze";
        } else {
          return "Force 6";
        }
      }
      if (dWindSpeed >= 29) {
        if (xAsLabel) {
          return "Fresh Breeze";
        } else {
          return "Force 5";
        }
      }
      if (dWindSpeed >= 20) {
        if (xAsLabel) {
          return "Moderate Breeze";
        } else {
          return "Force 4";
        }
      }
      if (dWindSpeed >= 12) {
        if (xAsLabel) {
          return "Gentle Breeze";
        } else {
          return "Force 3";
        }
      }
      if (dWindSpeed >= 6) {
        if (xAsLabel) {
          return "Light Breeze";
        } else {
          return "Force 2";
        }
      }
      if (dWindSpeed >= 1) {
        if (xAsLabel) {
          return "Light Air";
        } else {
          return "Force 1";
        }
      }
      if (dWindSpeed >= 0) {
        if (xAsLabel) {
          return "Calm";
        } else {
          return "Force 0";
        }
      }
    } catch (Exception eX) {
      Logger.error("sBeauFortFromKph Error " + eX.getMessage());
    }
    return sReturn;
  }

  /*return a string with Weather conditions based on input code
   */
  public static String sConditionsFromCode(Integer iCode) {
    String sReturn = "";
    try {
      switch (iCode) {
        case 100:
          sReturn = "Clear";
          break;
        case 200:
          sReturn = "Partial clouds";
          break;
        case 300:
          sReturn = "Cloudy";
          break;
        case 400:
          sReturn = "Light showers";
          break;
        case 500:
          sReturn = "Heavy showers";
          break;
        case 600:
          sReturn = "Rain";
          break;
        case 700:
          sReturn = "Snow";
          break;
        case 800:
          sReturn = "Thunder";
          break;
        default:
          sReturn = "Unknown code";
      }
    } catch (Exception eX) {
      Logger.error("sConditionsFromCode Error " + eX.getMessage());
    }
    return sReturn;
  }

}
