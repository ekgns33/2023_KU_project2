package contact;

public class ContactController {
    private ContactService contactService;
    //constructor
    public void routeRequest(int userRequest) {
        // read request and route to the certain controller.
        while(true) {
            switch (userRequest) {
                case 1:
                    this.searchContact();
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
    public ContactController(){}

    public void searchContact() {
    }

    public void createContact(){
    }

    public void updateContact() {

    }
    public void saveContact() {

    }

    public void deleteContact() {

    }

}
