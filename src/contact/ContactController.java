package contact;

import java.util.Map;

public class ContactController {
    private ContactService contactService;

    private int nextCommand;
    //constructor
    public void routeRequest(int userRequest, Map<Integer, Contact> userTable) {
        setNextCommand(userRequest);
        // read request and route to the certain controller.
        while(nextCommand != 0) {
            switch (userRequest) {
                case 1:
                    this.searchContact(userTable);
                    break;
                case 2:
                    this.createContact();
                    break;
                case 3:
                    this.updateContact();
                    break;
                case 4:
                    this.deleteContact();
                    break;
                default:
                    break;
            }
        }
    }
    public ContactController(){
        //initial value
        this.nextCommand = -1;
    }

    public void searchContact(Map<Integer, Contact> userTable) {
        setNextCommand(0);
    }
    public void createContact(){
    }

    public void updateContact() {

    }
    public void saveContact() {

    }

    public void deleteContact() {

    }

    public void setNextCommand(int nextCommand) {
        this.nextCommand = nextCommand;
    }
}
