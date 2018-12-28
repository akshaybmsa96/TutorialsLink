package tutorialslink.com.tutorialslinkwebview.pojos.highlightsPojo;

public class TablePojo {

    private String Status;

    private String Created_date;

    private String Sr_no;

    private String Image;

    private String URl;

    private String Title;

    private int backImage;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getCreated_date ()
    {
        return Created_date;
    }

    public void setCreated_date (String Created_date)
    {
        this.Created_date = Created_date;
    }

    public String getSr_no ()
    {
        return Sr_no;
    }

    public void setSr_no (String Sr_no)
    {
        this.Sr_no = Sr_no;
    }

    public String getImage ()
    {
        return Image;
    }

    public void setImage (String Image)
    {
        this.Image = Image;
    }

    public String getURl ()
    {
        return URl;
    }

    public void setURl (String URl)
    {
        this.URl = URl;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    public int getBackImage() {
        return backImage;
    }

    public void setBackImage(int backImage) {
        this.backImage = backImage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Created_date = "+Created_date+", Sr_no = "+Sr_no+", Image = "+Image+", URl = "+URl+", Title = "+Title+"]";
    }

}
