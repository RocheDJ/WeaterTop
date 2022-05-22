package models;
import javax.persistence.Entity;

import play.db.jpa.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a Reading class that stores the reading from the Weather Station
 *
 * @version (03-May-2022)
 */
@Entity
public class Reading extends Model {
    public LocalDateTime date;
    public Integer code;
    public Double temperature;
    public Double windSpeed;
    public Integer pressure;
    public Integer windDirection;

    public Reading(Integer iCode,
                   Double dTemperature,
                   Double dWindSpeed,
                   Integer iPressure,
                   Integer iWindDirection,
                   String sDate)
    {
        this.code = iCode;
        this.temperature =dTemperature;
        this.windSpeed=dWindSpeed;
        this.pressure=iPressure;
        this.windDirection=iWindDirection;
        setDate(sDate);
    }

    public String getDate(){
        String sReturnValue = "Invalid";
        DateTimeFormatter oFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sReturnValue = this.date.format(oFormatObj);
        return sReturnValue;
    }


    public void setDate(String sDate){
        String sTempDate;
        //Strip out all the DT format additions from the yml file string
        sTempDate = sDate.replace("ISO8601:","");
        sTempDate = sTempDate.replace("T"," ");
        sTempDate = sTempDate.replace("+0000","");
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //save actual Dt format
        this.date = LocalDateTime.parse(sTempDate,inputFormat);
    }


}
