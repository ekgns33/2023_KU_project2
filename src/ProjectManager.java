import contact.ContactController;
import contact.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.List;
import java.util.Scanner;
// Map 라이브러리 import
import java.util.Map;

public class ProjectManager extends ProjectManagerSupport {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    public ProjectManager() {
        super("./src/data.txt");
        // 의존성 부여를 위한 객체 생성
        this.contactController = new ContactController();
        this.contactRepository = new ContactRepository();
    }

    public void init() {
        List<String> dataList = fileHandler.readFile();
        this.contactRepository.setUserTable(contactMapper.mapListToHashMap(dataList));
    }

    public void bootProgram() {
        // read datas from FILE
        init();
        while(true) {
            try {
                String userInput = getUserInput();
                int userCommand = Integer.parseInt(userInput);

                if(userCommand> 5) throw new InvalidInputException(ErrorCode.Invalid_Input);
                //end clause
                if(userCommand == 0) break;
                // routeRequest에 userTable 정보 parameter로 추가
                Map<Integer, contact.Contact> userTable = contactRepository.getUserTable();
                this.contactController.routeRequest(userCommand, userTable);
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }

        }
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }

}
