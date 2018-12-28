package tutorialslink.com.tutorialslinkwebview.pojos.feedPojo;

public class TablePojo {

    private String Tags;

    private String Category;

    private String Created_by;

    private String Views;

    private String Last_name;

    private String Subtosub_Category;

    private String First_name;

    private String Short_Description;

    private String Icon;

    private String Image;

    private String Title;

    private String Created_At;

    private String Sr_No;

    public String getTags ()
    {
        return Tags;
    }

    public void setTags (String Tags)
    {
        this.Tags = Tags;
    }

    public String getCategory ()
    {
        return Category;
    }

    public void setCategory (String Category)
    {
        this.Category = Category;
    }

    public String getCreated_by ()
    {
        return Created_by;
    }

    public void setCreated_by (String Created_by)
    {
        this.Created_by = Created_by;
    }

    public String getViews ()
    {
        return Views;
    }

    public void setViews (String Views)
    {
        this.Views = Views;
    }

    public String getLast_name ()
    {
        return Last_name;
    }

    public void setLast_name (String Last_name)
    {
        this.Last_name = Last_name;
    }

    public String getSubtosub_Category ()
    {
        return Subtosub_Category;
    }

    public void setSubtosub_Category (String Subtosub_Category)
    {
        this.Subtosub_Category = Subtosub_Category;
    }

    public String getFirst_name ()
    {
        return First_name;
    }

    public void setFirst_name (String First_name)
    {
        this.First_name = First_name;
    }

    public String getShort_Description ()
    {
        return Short_Description;
    }

    public void setShort_Description (String Short_Description)
    {
        this.Short_Description = Short_Description;
    }

    public String getIcon ()
    {
        return Icon;
    }

    public void setIcon (String Icon)
    {
        this.Icon = Icon;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    public String getCreated_At ()
    {
        return Created_At;
    }

    public void setCreated_At (String Created_At)
    {
        this.Created_At = Created_At;
    }

    public String getSr_No ()
    {
        return Sr_No;
    }

    public void setSr_No (String Sr_No)
    {
        this.Sr_No = Sr_No;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Tags = "+Tags+", Category = "+Category+", Created_by = "+Created_by+", Views = "+Views+", Last_name = "+Last_name+", Subtosub_Category = "+Subtosub_Category+", First_name = "+First_name+", Short_Description = "+Short_Description+", Icon = "+Icon+", Title = "+Title+", Created_At = "+Created_At+", Sr_No = "+Sr_No+"]";
    }
}
