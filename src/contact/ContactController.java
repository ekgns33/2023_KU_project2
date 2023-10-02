package contact;

public class ContactController {
    private ContactService contactService;
    private ContactRepository contactRepository;

    private int nextCommand;
    //constructor
    public void routeRequest(int userRequest) {
        setNextCommand(userRequest);
        // read request and route to the certain controller.
        while(nextCommand != 0) {
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
    public ContactController(ContactRepository contactRepository){
        //initial value
        this.contactRepository = contactRepository;
        this.nextCommand = -1;
    }

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

    public void setNextCommand(int nextCommand) {
        this.nextCommand = nextCommand;
    }
}
