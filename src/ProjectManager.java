import contact.ContactController;
import contact.ContactRepository;

import java.util.Scanner;

public class ProjectManager {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    public ProjectManager() {
        // 의존성 부여를 위한 객체 생성
        this.contactController = new ContactController();
        this.contactRepository = new ContactRepository();
    }

    public void init() {

    }
    public void bootProgram() {
        // read datas from FILE
        init();
        while(true) {
            String userInput = getUserInput();
            this.contactController.routeRequest(Integer.parseInt(userInput));

        }
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }

}
