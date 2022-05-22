import java.util.List;

import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job
{
    public void doJob()
    {
        if(Member.count()==0){
            Logger.info("Loading data.yml");//only load from yml if blank
            Fixtures.loadModels("data.yml");
        }
    }
}