package tutorialslink.com.tutorialslinkwebview.pojos.eventspojo;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public class EventsPojo {

    private String Event_date;

    private String Description;

    private String Venue;

    private String Event_id;

    private String Country;

    private String City;

    private String End_time;

    private String Start_time;

    private String Title;

    public String getEvent_date ()
    {
        return Event_date;
    }

    public void setEvent_date (String Event_date)
    {
        this.Event_date = Event_date;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getVenue ()
    {
        return Venue;
    }

    public void setVenue (String Venue)
    {
        this.Venue = Venue;
    }

    public String getEvent_id ()
    {
        return Event_id;
    }

    public void setEvent_id (String Event_id)
    {
        this.Event_id = Event_id;
    }

    public String getCountry ()
    {
        return Country;
    }

    public void setCountry (String Country)
    {
        this.Country = Country;
    }

    public String getCity ()
    {
        return City;
    }

    public void setCity (String City)
    {
        this.City = City;
    }

    public String getEnd_time ()
    {
        return End_time;
    }

    public void setEnd_time (String End_time)
    {
        this.End_time = End_time;
    }

    public String getStart_time ()
    {
        return Start_time;
    }

    public void setStart_time (String Start_time)
    {
        this.Start_time = Start_time;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Event_date = "+Event_date+", Description = "+Description+", Venue = "+Venue+", Event_id = "+Event_id+", Country = "+Country+", City = "+City+", End_time = "+End_time+", Start_time = "+Start_time+", Title = "+Title+"]";
    }
}
