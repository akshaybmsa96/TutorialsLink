package tutorialslink.com.tutorialslinkwebview.pojos.detailspojo;

import java.util.ArrayList;

/**
 * Created by akshaybmsa96 on 08/06/18.
 */

public class DetailPojo {

    private ArrayList<DetailsTablePojo> Table;


    public ArrayList<DetailsTablePojo> getTable() {
        return Table;
    }

    public void setTable(ArrayList<DetailsTablePojo> table) {
        Table = table;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Table = "+Table+"]";
    }
}
