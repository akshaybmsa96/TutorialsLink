package tutorialslink.com.tutorialslinkwebview.pojos.authorSearchPojo;

public class TablePojo {

    private String Picture;

    private String Email;

    private String Last_name;

    private String Author_id;

    private String First_name;

    public String getPicture ()
    {
        return Picture;
    }

    public void setPicture (String Picture)
    {
        this.Picture = Picture;
    }

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
    }

    public String getLast_name ()
    {
        return Last_name;
    }

    public void setLast_name (String Last_name)
    {
        this.Last_name = Last_name;
    }

    public String getAuthor_id ()
    {
        return Author_id;
    }

    public void setAuthor_id (String Author_id)
    {
        this.Author_id = Author_id;
    }

    public String getFirst_name ()
    {
        return First_name;
    }

    public void setFirst_name (String First_name)
    {
        this.First_name = First_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Picture = "+Picture+", Email = "+Email+", Last_name = "+Last_name+", Author_id = "+Author_id+", First_name = "+First_name+"]";
    }

}
