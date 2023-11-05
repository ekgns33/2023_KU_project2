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
                    this.deleteContact();
                    break;
                case 4:
                    this.updateContact();
                    break;
                case 5:
                    // groupManagement()
                    this.groupManagementMenu();
                    break;
                case 6:
                    // 정렬 방식 설정
                    this.updateConfig();
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
            System.out.println("무엇으로 검색하시겠습니까?('0': 초기 메뉴로 이동)");
            System.out.println("1) 이름 2) 전화번호 3) 그룹");
            System.out.print(">> ");
            String userInput = getUserInput();
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3 || menuCommand < 0) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 0) {
                setNextCommand(0);
                return ;
            }
            Contact searchedContact =  contactService.search(menuCommand, this.contactRepository);
            if(searchedContact != null && searchedContact.getPid() != -1) {
                String print = printContact(searchedContact);
                System.out.println(print);
            }
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }catch(NumberFormatException e1) {
            System.out.println("잘못된 입력 형식입니다.");
        }
    }
    public void createContact(){
        try{ // 해당 부분에서 에러 처리가 필요할 수 있는 경우 대비해 try-catch로 작성
            Contact createdContact =  contactService.create(this.contactRepository);
            if(createdContact != null){
                System.out.println("저장되었습니다.");
            }
            setNextCommand(0);
        }catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
    }
    public void updateContact() {
        try{
            System.out.println("무엇으로 검색하시겠습니까?('0': 초기 메뉴로 이동)");
            System.out.println("1) 이름 2) 전화번호 3) 그룹");
            System.out.print(">> ");
            String userInput = getUserInput().trim();
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 3 && contactRepository.getGroupTable().size() == 0) {
                System.out.println("현재 프로그램 내에 존재하는 그룹이 없습니다.");
                return;
            }
            if(menuCommand == 0) {
                setNextCommand(0);
                return;
            }
            contactService.update(menuCommand, this.contactRepository);
        } catch(NumberFormatException e1) {
            System.out.println("잘못된 입력 형식입니다.");
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteContact() {
        contactService.delete(this.contactRepository);
        setNextCommand(0);
    }

    public void groupManagementMenu(){
        try{
            System.out.println("수행하실 작업을 고르시오.('0': 초기 메뉴로 이동)");
            System.out.println("1) 그룹 추가 2) 그룹 삭제 3) 그룹 수정");
            System.out.print(">> ");
            String userInput = getUserInput().trim();
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3 || menuCommand < 0) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 0){
                setNextCommand(0);
                return;
            }
            contactService.groupManagement(menuCommand, this.contactRepository);
        } catch(NumberFormatException e1) {
            System.out.println("잘못된 입력 형식입니다.");
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateConfig() {
        String sort;
        if(this.contactRepository.getSortBy() == 1) {
            sort = "이름 가나다순";
        } else if(this.contactRepository.getSortBy() == 2) {
            sort = "그룹별 정렬";
        } else {
            sort = "최근 저장순";
        }
        try {
            System.out.println("정렬 기준을 선택하시오.('0': 초기 메뉴로 이동)");
            System.out.println("현재 정렬 기준: " + sort);
            System.out.print("1) 이름 가나다순 2) 그룹별 정렬 3) 최근 저장순\n>> ");
            String userInput = getUserInput().trim();
            int menuCommand = Integer.parseInt(userInput);
            if(menuCommand > 3 || menuCommand < 0) throw new InvalidInputException(ErrorCode.Invalid_Input);
            if(menuCommand == 0) {
                setNextCommand(0);
                return;
            }
            contactService.modifyConfig(menuCommand, this.contactRepository);
            System.out.println(this.contactRepository.getSequencedUserTable());
        } catch(NumberFormatException e) {
            System.out.println("잘못된 입력 형식입니다.");
        } catch(ApplicationException e1) {
            System.out.println(e1.getMessage());
        }
    }
    public void setNextCommand(int nextCommand) {
        this.nextCommand = nextCommand;
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine().trim();
        return userInput.trim();
    }

    public String printContact(Contact contact) {
        String sout;
        String none = "";
        if (!contact.getGroupName().equals("X")) {
            none = contact.getGroupName();
        }
        sout = "이름: " + contact.getName() + "\n";
        sout += "전화번호: " + contact.getPhoneNumber().toString() + "\n";
        sout += "그룹: " + none + "\n";
        sout += "메모: " + contact.getMemo() + "\n";
        return sout;
    }
}
