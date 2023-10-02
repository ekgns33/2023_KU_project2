package contact;

import java.util.List;
import java.util.Scanner;

public class ContactService {
    public ContactService(){
    }

    public void searchService(int userInput, ContactRepository contactRepository){
        switch (userInput){
            case 1:
            case 2:
            case 3:
            default:
                break;
        }
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}