package tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public class EventDetailTablePojo {

    private String Description;

    private String Event_id;

    private String Country;

    private String Created_By;

    private String Title;

    private String End_time;

    private String City;

    private String Created_At;

    private String Event_date;

    private String Status;

    private String Tags;

    private String Venue;

    private String Views;

    private String Image;

    private String Language;

    private String Start_time;

    private String URl;

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
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

    public String getCreated_By ()
    {
        return Created_By;
    }

    public void setCreated_By (String Created_By)
    {
        this.Created_By = Created_By;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    public String getEnd_time ()
    {
        return End_time;
    }

    public void setEnd_time (String End_time)
    {
        this.End_time = End_time;
    }

    public String getCity ()
    {
        return City;
    }

    public void setCity (String City)
    {
        this.City = City;
    }

    public String getCreated_At ()
    {
        return Created_At;
    }

    public void setCreated_At (String Created_At)
    {
        this.Created_At = Created_At;
    }

    public String getEvent_date ()
    {
        return Event_date;
    }

    public void setEvent_date (String Event_date)
    {
        this.Event_date = Event_date;
    }

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getTags ()
    {
        return Tags;
    }

    public void setTags (String Tags)
    {
        this.Tags = Tags;
    }

    public String getVenue ()
    {
        return Venue;
    }

    public void setVenue (String Venue)
    {
        this.Venue = Venue;
    }

    public String getViews ()
    {
        return Views;
    }

    public void setViews (String Views)
    {
        this.Views = Views;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLanguage ()
    {
        return Language;
    }

    public void setLanguage (String Language)
    {
        this.Language = Language;
    }

    public String getStart_time ()
    {
        return Start_time;
    }

    public void setStart_time (String Start_time)
    {
        this.Start_time = Start_time;
    }

    public String getURl() {
        return URl;
    }

    public void setURl(String URl) {
        this.URl = URl;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [Description = "+Description+", Event_id = "+Event_id+", Country = "+Country+", Created_By = "+Created_By+", Title = "+Title+", End_time = "+End_time+", City = "+City+", Created_At = "+Created_At+", Event_date = "+Event_date+", Status = "+Status+", Tags = "+Tags+", Venue = "+Venue+", Views = "+Views+", Image = "+Image+", Language = "+Language+", Start_time = "+Start_time+", URl = "+URl+"]";
    }
}
