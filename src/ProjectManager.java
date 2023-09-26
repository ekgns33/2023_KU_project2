import java.util.Scanner;

public class ProjectManager {

    public ProjectManager() {};

    public void init() {

    }
    public void bootProgram() {
        // read datas from FILE
        init();
        while(true) {
            String userInput = getUserInput();
            if(userInput.equals("0")) break;
        }
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput = scan.nextLine();
        return userInput;
    }

}
