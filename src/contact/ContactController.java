package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.Map;
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
            contactService.searchService(menuCommand, contactRepository);
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
    }
     public void createContact(Map<Integer, Contact> userInfo){
        while(true) {
            try {
                // Menu UI
                System.out.print("1. 이름 입력: ");
                String userName = getUserInput();
				if(Integer.parseInt(userName)==0) break;
				if(Integer.parseInt(userName)> 4 || Integer.parseInt(userName)<0) 
					throw new InvalidInputException(ErrorCode.Invalid_Input);
				
                System.out.print("2. 번호 입력: ");
                String userNumber = getUserInput();
                if(Integer.parseInt(userNumber)==0) break;
                if(Integer.parseInt(userNumber)> 4 || Integer.parseInt(userNumber)<0)
                	throw new InvalidInputException(ErrorCode.Invalid_Input);
                
                System.out.print("3. 그룹 입력: ");
                String userGroup = getUserInput();
                if(Integer.parseInt(userGroup)==0) break;
                if(Integer.parseInt(userGroup)> 4|| Integer.parseInt(userGroup)<0)
                	throw new InvalidInputException(ErrorCode.Invalid_Input);
                
                System.out.print("4. 메모 입력: ");
                String userMemo = getUserInput();
                if(Integer.parseInt(userMemo)==0) break;
                
                System.out.println("저장하시겠습니까?");
                String userInput = getUserInput();
                char userCommand = userInput.charAt(0);
                switch(userCommand) {
                case 'y' -> saveContact();
                case 'n' -> throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }
        }
        setNextCommand(0);
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
