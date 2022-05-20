package models;
import javax.persistence.Entity;

import play.db.jpa.Model;
/**
 * This is a Reading class that stores the reading from the Weather Station
 *
 * @version (03-May-2022)
 */
@Entity
public class Reading extends Model {
    public Integer code;
    public Double temperature;
    public Double windSpeed;
    public Integer pressure;
    public Integer windDirection;

public Reading(Integer iCode,
               Double dTemperature,
               Double dWindSpeed,
               Integer iPressure,
               Integer iWindDirection)
{
    this.code = iCode;
    this.temperature =dTemperature;
    this.windSpeed=dWindSpeed;
    this.pressure=iPressure;
    this.windDirection=iWindDirection;
}

}
