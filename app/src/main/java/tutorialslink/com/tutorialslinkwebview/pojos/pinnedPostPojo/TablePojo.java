package tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo;

public class TablePojo {

    private String Image;

    private String Post_id;

    private String Title;

    private String Category;

    public String getImage ()
    {
        return Image;
    }

    public void setImage (String Image)
    {
        this.Image = Image;
    }

    public String getPost_id ()
    {
        return Post_id;
    }

    public void setPost_id (String Post_id)
    {
        this.Post_id = Post_id;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Image = "+Image+", Post_id = "+Post_id+", Title = "+Title+"]";
    }
}
