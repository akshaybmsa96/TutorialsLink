package tutorialslink.com.tutorialslinkwebview.pojos.notificationPojo;

import java.util.ArrayList;

public class NotificationPojo {

    private ArrayList<TablePojo> Table;

    public ArrayList<TablePojo> getTable() {
        return Table;
    }

    public void setTable(ArrayList<TablePojo> table) {
        Table = table;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Table = "+Table+"]";
    }
}
