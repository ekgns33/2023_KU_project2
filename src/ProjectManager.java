import contact.ContactController;
import contact.ContactRepository;
import java.util.List;
import java.util.Scanner;

public class ProjectManager extends ProjectManagerSupport {

    private final ContactController contactController;
    private final ContactRepository contactRepository;

    public ProjectManager() {
        super("data.txt");
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
            String userInput = getUserInput();
            if(userInput.equals("0")) break;
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
