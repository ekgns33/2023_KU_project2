package contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactService {
    public ContactService(){
    }

    public void searchService(int userInput, ContactRepository contactRepository){
        List<Contact> queryResult = contactRepository.findAll();
        switch (userInput) {
            case 1:
                String inputName = getUserInput();
                queryResult = contactRepository.findByName(inputName);
                break;
            case 2:
                String inputPhoneNumber = getUserInput();
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case 3:
                String inputGroup = getUserInput();
                queryResult = contactRepository.findByGroupName(inputGroup);
                break;
            default:
                break;
        }
        if(queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return ;
        }
        else{
            int index = 1;
            for(Contact contact : queryResult){
                System.out.print("["+index+"] ");
                System.out.println(queryResult.toString());
                index++;
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