package models;

import play.db.jpa.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


/**
 * This is the WeatherStation class that stores the name and readings for the Station
 *
 * @version (03 - May - 2022)
 */
@Entity
public class Station extends Model {
    public String name;
    public Double latitude;
    public Double longitude;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Reading> readings = new ArrayList<>();

    public Station(String sName) {
        this.name = sName;
        this.latitude = -90.00;
        this.longitude = -90.00;
    }

    public Station(String sName, Double dLatitude, Double dLongitude) {
        this.name = sName;
        this.latitude = dLatitude;
        this.longitude = dLongitude;
    }


    // converted from example on https://www.campbellsci.com/blog/convert-wind-directions
    final private static String[] Windsector = new String[]{"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
            "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};

    private static Boolean xReadingsDateDec = false;

    private Double CelsiusToFahrenheit(Double dCelcious) {
        return dCelcious * (9 / 5) + 32;
    }

    private Reading currentReading() {
        Integer iLastReadingIndex;
        iLastReadingIndex = readings.size() - 1;
        return readings.get(iLastReadingIndex);
    }

    /**
     * Return current pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (03 - May - 2022)
     */
    public String currentPressure() {
        String sReturn = "---";
        try {
            Reading lastReading = currentReading();
            sReturn = lastReading.pressure + " hPa ";
        } catch (Exception eX) {
            sReturn = "Error " + eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return max pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return min pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return max temperiture as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return min temperature as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return current Temp as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (03 - May - 2022)
     */
    public String currentTemp() {
        String sReturn = "---";
        try {
            Reading lastReading = currentReading();
            sReturn = lastReading.temperature + " C " + CelsiusToFahrenheit(lastReading.temperature) + " F";
        } catch (Exception eX) {
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return current Compass Direction as string in Cardinal Points based on Appendix A - iv of specifications
     *
     * @version (03 - May - 2022)
     */
    public String currentWindCompass() {
        String sReturn = "---";
        try {
            Reading lastReading = currentReading();
        /* from https://www.campbellsci.com/blog/convert-wind-directions
         convert wind direction to integer values that correspond with the 17 index values within our array.
        */
            Integer iWindDir = lastReading.windDirection;
            Integer iIndex;
            if (iWindDir > 360) {
                iIndex = lastReading.windDirection % 360; // use modular division to get remainder if >360
            } else {
                iIndex = lastReading.windDirection;
            }
            double dIndex = (iIndex / 22.5);
            iIndex = (int) Math.round(dIndex);
            sReturn = Windsector[iIndex];
        } catch (Exception eX) {
            sReturn = "Error " + eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return current wind chill as string Based Appendix A - v of specifications
     *
     * @version (03 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return min win speed as string in kph based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return max wind speed as string in kph based on Appendix A - ii of specifications
     *
     * @version (15 - May - 2022)
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return current Wind as string Baufort based on Appendix A - iii of specifications
     *
     * @version (03 - May - 2022)
     */
    public String currentWind(Boolean xAsLabel) {
        String sReturn = "---";
        try {
            Reading lastReading = currentReading();
            double dWindSpeed = lastReading.windSpeed;
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
            sReturn = "Error " + eX.getMessage();
        }

        return sReturn;

    }

    /**
     * Return current conditions as string based on Appendix A - i of specifications
     *
     * @version (03 - May - 2022)
     */
    public String currentConditions() {
        String sReturn = "";
        try {
            Reading lastReading = currentReading();
            switch (lastReading.code) {
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
            sReturn = "Error " + eX.getMessage();
        }
        return sReturn;
    }


    public void SortByDate(Boolean xDecending) {
        Collections.sort(this.readings, new Reading.CompareLogDate(xDecending));
        xReadingsDateDec = xDecending;
    }

    public boolean ReadingsSortedbyDateDec() {
        return xReadingsDateDec;
    }

    public boolean ReadingsSortedbyDateAsc() {
        return !xReadingsDateDec;
    }


    /**
     * Return Integer representing Trend of temp values Integer + Positive, - Negative , 0 Static
     *
     * @version (16 - May - 2022)
     */
    public Integer trendTemperature() {
        Integer iReturnValue = 0;
        ArrayList<Double> xValues; //date time stamp
        ArrayList<Double> yValues; //date time stamp
        ArrayList<Reading> aReadings;// array of readings
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        for (Integer iX = 0; iX < this.readings.size(); iX++) {
            Reading mReading = this.readings.get(iX);
            yValues.add(mReading.temperature);
            xValues.add(Double.valueOf(mReading.getId()));
        }
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }

    //set max number of readings to trend
    public Integer trendTemperature(Integer iNoOfReadings) {
        Integer iReturnValue = 0;
        ArrayList<Double> xValues; //date time stamp
        ArrayList<Double> yValues; //date time stamp
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
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }

    /**
     * Return Integer representing Trend of Pressure values Integer + Positive, - Negative , 0 Static
     *
     * @version (16 - May - 2022)
     */
    public Integer trendPressure() {
        Integer iReturnValue = 0;
        ArrayList<Double> xValues; //date time stamp
        ArrayList<Double> yValues; //date time stamp

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (Integer iX = 0; iX < this.readings.size(); iX++) {
            Reading mReading = this.readings.get(iX);
            yValues.add(Double.valueOf(mReading.pressure));
            xValues.add(Double.valueOf(mReading.getId()));
        }
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }

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
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }


    /**
     * Return Integer representing Trend of Wind speed values Integer + Positive, - Negative , 0 Static
     *
     * @version (16 - May - 2022)
     */
    public Integer trendWind() {
        Integer iReturnValue = 0;
        ArrayList<Double> xValues; //date time stamp
        ArrayList<Double> yValues; //date time stamp

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();


        for (Integer iX = 0; iX < this.readings.size(); iX++) {
            Reading mReading = this.readings.get(iX);
            yValues.add(mReading.windSpeed);
            xValues.add(Double.valueOf(mReading.getId()));
        }
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }

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
        Double dTrendValue = dTrend(xValues, yValues);
        if (dTrendValue > 0) {
            iReturnValue = 1;
        } else if (dTrendValue < 0) {
            iReturnValue = -1;
        } else {
            iReturnValue = 0;
        }

        return iReturnValue;
    }

    /**
     * Return average Value of a list of doubles
     *
     * @version (16 - May - 2022)
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

    /**
     * Return the slope or trend for an array code based on formula outlined in
     * https://study.com/academy/lesson/what-is-a-trend-line-in-math-definition-equation-analysis.html
     */
    private static double dTrend(ArrayList<Double> yList, ArrayList<Double> xList) {
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

}
