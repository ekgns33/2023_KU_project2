package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

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
        }
        else {
            int index = 1;
            for (Contact contact : queryResult) {
                System.out.print("[" + index + "] ");
                System.out.println(contact.toString());
                index++;
            }
            int targetIndex = selectIndex();
            getContactDetails(targetIndex, queryResult);
        }
    }
    public int selectIndex(){
        System.out.print("인덱스 선택 : ");
        String userInput = getUserInput();
        return Integer.parseInt(userInput);
    }
    public void getContactDetails(int indexCommand, List<Contact> queryResult){
        do {
            try {
                if (indexCommand > queryResult.size() || indexCommand < 0) {
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
                if (indexCommand == 0) return;
                System.out.println(queryResult.get(indexCommand - 1));
                return ;
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }
            indexCommand = selectIndex();
        }while(true);
    };

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}