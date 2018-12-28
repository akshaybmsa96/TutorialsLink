package tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 14/06/18.
 */

public class TutorialsPojo {

    private ArrayList<TutorialLibrary> Tutorials;

    private ArrayList<FeaturedTutorials> featured;

    public ArrayList<TutorialLibrary> getTutorials() {
        return Tutorials;
    }

    public void setTutorials(ArrayList<TutorialLibrary> tutorials) {
        Tutorials = tutorials;
    }

    public ArrayList<FeaturedTutorials> getFeatured() {
        return featured;
    }

    public void setFeatured(ArrayList<FeaturedTutorials> featured) {
        this.featured = featured;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Tutorials = "+Tutorials+", featured = "+featured+"]";
    }

}
