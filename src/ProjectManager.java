import contact.ContactController;
import contact.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

import java.util.List;
import java.util.Scanner;

public class ProjectManager extends ProjectManagerSupport {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    public ProjectManager() {
        super("data.txt");
        // 의존성 부여를 위한 객체 생성
        this.contactRepository = new ContactRepository();
        this.contactController = new ContactController(this.contactRepository);

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
                this.contactController.routeRequest(userCommand);
                //save file
                fileHandler.writeListToFile(this.contactRepository.toStringList());
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
