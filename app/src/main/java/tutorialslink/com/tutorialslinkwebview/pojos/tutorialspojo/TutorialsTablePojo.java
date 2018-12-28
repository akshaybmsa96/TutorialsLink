package tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo;

/**
 * Created by akshaybmsa96 on 14/06/18.
 */

public class TutorialsTablePojo {

    private String TCat_id;

    private String Image;

    private String TCat_name;

    public String getTCat_id ()
    {
        return TCat_id;
    }

    public void setTCat_id (String TCat_id)
    {
        this.TCat_id = TCat_id;
    }

    public String getImage ()
    {
        return Image;
    }

    public void setImage (String Image)
    {
        this.Image = Image;
    }

    public String getTCat_name ()
    {
        return TCat_name;
    }

    public void setTCat_name (String TCat_name)
    {
        this.TCat_name = TCat_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TCat_id = "+TCat_id+", Image = "+Image+", TCat_name = "+TCat_name+"]";
    }
}
