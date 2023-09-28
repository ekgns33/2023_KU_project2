package contact;

import java.util.Map;

public class ContactRepository {

    private Map<Integer, Contact> userTable;
    public ContactRepository () {
    };

    public void setUserTable(Map<Integer, Contact> userTable) {
        this.userTable = userTable;
    }
}