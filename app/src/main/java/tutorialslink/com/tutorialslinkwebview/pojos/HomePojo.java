package tutorialslink.com.tutorialslinkwebview.pojos;

import java.util.ArrayList;

import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.FeaturedTutorials;

/**
 * Created by akshaybmsa96 on 20/06/18.
 */

public class HomePojo {

    private ArrayList<FeedTablePojo> Newslist;

    private ArrayList<FeedTablePojo> Articlelist;

    private ArrayList<FeaturedTutorials> Tutorialslist;

    private ArrayList<FeedTablePojo> Videolist;


    public ArrayList<FeedTablePojo> getNewslist() {
        return Newslist;
    }

    public void setNewslist(ArrayList<FeedTablePojo> newslist) {
        Newslist = newslist;
    }

    public ArrayList<FeedTablePojo> getArticlelist() {
        return Articlelist;
    }

    public void setArticlelist(ArrayList<FeedTablePojo> articlelist) {
        Articlelist = articlelist;
    }

    public ArrayList<FeaturedTutorials> getTutorialslist() {
        return Tutorialslist;
    }

    public void setTutorialslist(ArrayList<FeaturedTutorials> tutorialslist) {
        Tutorialslist = tutorialslist;
    }


    public ArrayList<FeedTablePojo> getVideolist() {
        return Videolist;
    }

    public void setVideolist(ArrayList<FeedTablePojo> videolist) {
        Videolist = videolist;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Newslist = "+Newslist+", Articlelist = "+Articlelist+", Videolist = "+Videolist+", Tutorialslist = "+Tutorialslist+"]";
    }
}
