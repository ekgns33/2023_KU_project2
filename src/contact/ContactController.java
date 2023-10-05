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
        try{
            System.out.println("1.이름");
            System.out.println("2.전화번호");
            System.out.println("3.그룹");
            String userInput = getUserInput();
            // 각각에 맞는 예외처리 구현 X
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 0) {
                setNextCommand(0);
                return ;
            }
            Contact searchedContact =  contactService.search(menuCommand, this.contactRepository);
            System.out.println(searchedContact);
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
    }
    public void createContact(){
        try{ // 해당 부분에서 에러 처리가 필요할 수 있는 경우 대비해 try-catch로 작성
            Contact createdContact =  contactService.create(this.contactRepository);
            if(createdContact != null){
                System.out.println(createdContact);
            }
            setNextCommand(0);
            return ;
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
    }
    public void updateContact() {
        try{
            System.out.println("1.이름");
            System.out.println("2.전화번호");
            System.out.println("3.그룹");
            String userInput = getUserInput();
            // 각각에 맞는 예외처리 구현 X
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 0) {
                setNextCommand(0);
                return ;
            }
            contactService.update(menuCommand, this.contactRepository);
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
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
