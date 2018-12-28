package tutorialslink.com.tutorialslinkwebview.pojos.searchpojo;

public class TablePojo {

    private String Tags;

    private String Category;

    private String Sr_no;

    private String Views;

    private String Subtosub_Category;

    private String Image;

    private String Short_Description;

    private String Title;

    private String Created_At;

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

    public String getSr_no ()
    {
        return Sr_no;
    }

    public void setSr_no (String Sr_no)
    {
        this.Sr_no = Sr_no;
    }

    public String getViews ()
    {
        return Views;
    }

    public void setViews (String Views)
    {
        this.Views = Views;
    }

    public String getSubtosub_Category ()
    {
        return Subtosub_Category;
    }

    public void setSubtosub_Category (String Subtosub_Category)
    {
        this.Subtosub_Category = Subtosub_Category;
    }

    public String getImage ()
    {
        return Image;
    }

    public void setImage (String Image)
    {
        this.Image = Image;
    }

    public String getShort_Description ()
    {
        return Short_Description;
    }

    public void setShort_Description (String Short_Description)
    {
        this.Short_Description = Short_Description;
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

    @Override
    public String toString()
    {
        return "ClassPojo [Tags = "+Tags+", Category = "+Category+", Sr_no = "+Sr_no+", Views = "+Views+", Subtosub_Category = "+Subtosub_Category+", Image = "+Image+", Short_Description = "+Short_Description+", Title = "+Title+", Created_At = "+Created_At+"]";
    }

}
