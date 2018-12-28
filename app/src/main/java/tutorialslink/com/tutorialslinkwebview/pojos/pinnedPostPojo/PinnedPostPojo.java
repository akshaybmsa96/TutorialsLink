package tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo;

import java.util.ArrayList;

public class PinnedPostPojo {
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
