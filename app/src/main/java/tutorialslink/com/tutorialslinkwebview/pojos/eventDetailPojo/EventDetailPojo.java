package tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public class EventDetailPojo {

    private ArrayList<EventDetailTablePojo> Table;

    public ArrayList<EventDetailTablePojo> getTable() {
        return Table;
    }

    public void setTable(ArrayList<EventDetailTablePojo> table) {
        Table = table;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Table = "+Table+"]";
    }
}
