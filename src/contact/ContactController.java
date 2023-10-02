package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.Map;
import java.util.Scanner;

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
        // Initialize contactService
        this.contactService = new ContactService();
    }

    public void searchContact(Map<Integer, Contact> userInfo) {
        while(true) {
            try {
                // Menu UI
                System.out.println("1. 이름");
                System.out.println("2. 전화번호");
                System.out.println("3. 그룹");
                String userInput = getUserInput();
                int userCommand = Integer.parseInt(userInput);
                if(userCommand> 4) throw new InvalidInputException(ErrorCode.Invalid_Input);
                //end clause
                if(userCommand == 0) break;
//              for (Map.Entry<Integer, Contact> entry : userInfo.entrySet()) {
//                  System.out.println(entry.getKey() + ": " + entry.getValue());
//              }
                contactService.searchService(userCommand, userInfo);
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }
        }
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

    // getUserInput 추가
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}
