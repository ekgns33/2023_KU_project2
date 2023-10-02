package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.Scanner;

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
        this.contactService = new ContactService();
    }
    public void searchContact() {
        while(true){
            try{
                System.out.println("1.이름");
                System.out.println("2.전화번호");
                System.out.println("3.그룹");
                String userInput = getUserInput();
                int userCommand = Integer.parseInt(userInput);
                if(userCommand > 3) throw new InvalidInputException(ErrorCode.Invalid_Input);
                if(userCommand == 0) break;
                contactService.searchService(userCommand, contactRepository);
            }catch(ApplicationException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void createContact(){

    }
    public void updateContact() {

    }
    public void deleteContact() {

    }
    public void setNextCommand(int nextCommand) {
        this.nextCommand = nextCommand;
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}
