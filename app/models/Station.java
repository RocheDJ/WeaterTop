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
 * @version (03-May-2022)
 */
@Entity
public class Station extends Model {
    public String name;
    public Double latitude;
    public Double longitude;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Reading> readings = new ArrayList<>();

    public Station(String sName)
    {
        this.name=sName;
        this.latitude = -90.00;
        this.longitude= -90.00;
    }

    public Station(String sName, Double dLatitude, Double dLongitude )
    {
        this.name=sName;
        this.latitude = dLatitude;
        this.longitude= dLongitude;
    }


    // converted from example on https://www.campbellsci.com/blog/convert-wind-directions
    final private static String[] Windsector = new String[]  {"N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW","W","WNW","NW","NNW","N"};

    private Double CelsiusToFahrenheit(Double dCelcious){
        return dCelcious * (9/5) +32;
    }

    private Reading currentReading(){
        Integer iLastReadingIndex;
        iLastReadingIndex = readings.size()-1;
        return readings.get(iLastReadingIndex);
    }


    /**
     * Return current pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (03-May-2022)
     */
    public String currentPressure(){
        String sReturn = "---";
        try{
            Reading lastReading =currentReading();
            sReturn = lastReading.pressure + " hPa ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return max pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String maxPressure(){
        String sReturn = "---";
        try{
            Integer iMaxPressure = readings.get(0).pressure;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).pressure >iMaxPressure) {
                    iMaxPressure = readings.get(iX).pressure;
                }
            }
            sReturn = iMaxPressure + " hPa ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return min pressure as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String minPressure(){
        String sReturn = "---";
        try{
            Integer iMinPressure = readings.get(0).pressure;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).pressure <iMinPressure) {
                    iMinPressure = readings.get(iX).pressure;
                }
            }
            sReturn = iMinPressure + " hPa ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }
    /**
     * Return max temperiture as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String maxTemp(){
        String sReturn = "---";
        try{
            Double iMaxTemp = readings.get(0).temperature;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).temperature >iMaxTemp) {
                    iMaxTemp = readings.get(iX).temperature;
                }
            }
            sReturn = iMaxTemp + "  Deg C  ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return min temperature as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String minTemp(){
        String sReturn = "---";
        try{
            double iMinTemp = readings.get(0).temperature;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).temperature <iMinTemp) {
                    iMinTemp = readings.get(iX).temperature;
                }
            }
            sReturn = iMinTemp + " Deg C ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }



    /**
     * Return current Temp as string in Deg C and F based on Appendix A - ii of specifications
     *
     * @version (03-May-2022)
     */
    public String currentTemp(){
        String sReturn = "---";
        try {
            Reading lastReading =currentReading();
            sReturn = lastReading.temperature + " C " +CelsiusToFahrenheit(lastReading.temperature) +" F";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }


    /**
     * Return current Compass Direction as string in Cardinal Points based on Appendix A - iv of specifications
     *
     * @version (03-May-2022)
     */
    public String currentWindCompass(){
        String sReturn = "---";
        try{
            Reading lastReading =currentReading();
        /* from https://www.campbellsci.com/blog/convert-wind-directions
         convert wind direction to integer values that correspond with the 17 index values within our array.
        */
            Integer iWindDir = lastReading.windDirection;
            Integer iIndex;
            if(iWindDir>360) {
                iIndex= lastReading.windDirection % 360; // use modular division to get remainder if >360
            }else{
                iIndex = lastReading.windDirection;
            }
            double dIndex = (iIndex/ 22.5);
            iIndex =(int)Math.round(dIndex);
            sReturn = Windsector[iIndex];
        }catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return current wind chill as string Based Appendix A - v of specifications
     *
     * @version (03-May-2022)
     */
    public String currentWindChill(){
        String sReturn = "---";
        try{
            Reading lastReading =currentReading();
            double dWindKph = lastReading.windSpeed;
            double dTemperature= lastReading.temperature;
            double dWindChill = 13.12 + (0.6215*dTemperature) - 11.37*(Math.pow(dWindKph,0.16)) + 0.3965 * dTemperature * Math.pow(dWindKph,0.16);//funny cals but ok
            sReturn = String.format("Feels like %.2f" , dWindChill); // Limit number to 2 decimal places fro display
        }catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }

        return sReturn;
    }

    /**
     * Return min win speed as string in kph based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String minWindSpeed(){
        String sReturn = "---";
        try{
            double dMinSpeed = readings.get(0).windSpeed;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).windSpeed <dMinSpeed) {
                    dMinSpeed = readings.get(iX).windSpeed;
                }
            }
            sReturn = dMinSpeed + " kph ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return max wind speed as string in kph based on Appendix A - ii of specifications
     *
     * @version (15-May-2022)
     */
    public String maxWindSpeed(){
        String sReturn = "---";
        try{
            double dMaxSpeed = readings.get(0).windSpeed;
            for (Integer iX = 1; iX < readings.size();iX++ ){
                if(readings.get(iX).windSpeed >dMaxSpeed) {
                    dMaxSpeed = readings.get(iX).windSpeed;
                }
            }
            sReturn = dMaxSpeed + " kph ";
        } catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }

    /**
     * Return current Wind as string baufort based on Appendix A - iii of specifications
     *
     * @version (03-May-2022)
     */
    public String currentWind(){
        String sReturn = "---";
        try{
            Reading lastReading =currentReading();
            double dWindSpeed = lastReading.windSpeed;
            //start from max and work down returning after each check
            if (dWindSpeed >= 103){
                return "Violent Storm";
            }
            if (dWindSpeed >= 89){
                return "Strong storm";
            }
            if (dWindSpeed >= 75){
                return "Severe Gale";
            }
            if (dWindSpeed >= 62){
                return "Gale";
            }
            if (dWindSpeed >= 50){
                return "Near Gale";
            }
            if (dWindSpeed >= 39){
                return "Strong Breeze";
            }
            if (dWindSpeed >= 29){
                return "Fresh Breeze";
            }
            if (dWindSpeed >= 20){
                return "Moderate Breeze";
            }
            if (dWindSpeed >= 12){
                return "Gentle Breeze";
            }
            if (dWindSpeed >= 6){
                return "Light Breeze";
            }
            if (dWindSpeed >= 1){
                return "Light Air";
            }
            if (dWindSpeed >= 0){
                return "Calm";
            }
        }catch(Exception eX){
            sReturn ="Error " +eX.getMessage();
        }

        return sReturn;

    }





    /**
     * Return current conditions as string based on Appendix A - i of specifications
     *
     * @version (03-May-2022)
     */
    public  String currentConditions(){
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
        }catch (Exception eX){
            sReturn ="Error " +eX.getMessage();
        }
        return sReturn;
    }

}
