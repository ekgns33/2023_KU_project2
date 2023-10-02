package contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactService {
    public ContactService(){
    }

    public void searchService(int userInput, ContactRepository contactRepository){
        List<Contact> queryResult = new ArrayList<>();
        switch (userInput) {
            case 1:
                String inputName = getUserInput();
                queryResult = contactRepository.findByName(inputName);
            case 2:
                String inputPhoneNumber = getUserInput();
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
            case 3:
                String inputGroup = getUserInput();
                queryResult = contactRepository.findByGroupName(inputGroup);
            default:
                break;
        }
        if(queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return ;
        }
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}