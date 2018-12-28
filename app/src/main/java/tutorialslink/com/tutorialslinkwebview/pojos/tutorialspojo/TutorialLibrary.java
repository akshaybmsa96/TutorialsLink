package tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo;

/**
 * Created by akshaybmsa96 on 16/06/18.
 */

public class TutorialLibrary {

    private String Category;

    private String ico;

    private String Cat_id;

    private String TCat_name;

    private String Tcat_id;

    private String url;

    public String getCategory ()
    {
        return Category;
    }

    public void setCategory (String Category)
    {
        this.Category = Category;
    }

    public String getIco ()
    {
        return ico;
    }

    public void setIco (String ico)
    {
        this.ico = ico;
    }

    public String getCat_id ()
    {
        return Cat_id;
    }

    public void setCat_id (String Cat_id)
    {
        this.Cat_id = Cat_id;
    }

    public String getTCat_name ()
    {
        return TCat_name;
    }

    public void setTCat_name (String TCat_name)
    {
        this.TCat_name = TCat_name;
    }

    public String getTcat_id ()
    {
        return Tcat_id;
    }

    public void setTcat_id (String Tcat_id)
    {
        this.Tcat_id = Tcat_id;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Category = "+Category+", ico = "+ico+", Cat_id = "+Cat_id+", TCat_name = "+TCat_name+", Tcat_id = "+Tcat_id+"]";
    }

}
