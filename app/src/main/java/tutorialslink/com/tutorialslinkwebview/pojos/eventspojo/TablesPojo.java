package tutorialslink.com.tutorialslinkwebview.pojos.eventspojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public class TablesPojo {

    private ArrayList<EventsPojo> Table;

    public ArrayList<EventsPojo> getTable() {
        return Table;
    }

    public void setTable(ArrayList<EventsPojo> table) {
        Table = table;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Table = "+Table+"]";
    }
}
