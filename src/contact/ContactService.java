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

    public Contact search(int userInput, ContactRepository contactRepository){
        List<Contact> queryResult = searchByInputType(userInput, contactRepository);

        if(queryResult == null || queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return null;
        } else {
            showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);
            return selectedContact;
        }
    }
    public void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.toString());
            index++;
        }
    }
    public List<Contact> searchByInputType(int userInput, ContactRepository contactRepository) {
        List<Contact> queryResult = null;
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
        return queryResult;
    }
    public Contact selectAndGetContact(List<Contact> queryResult) {
        int targetIndex = selectIndex();
        while(targetIndex != 0) {
            try {
                if (targetIndex > queryResult.size() || targetIndex < 0) {
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
                return queryResult.get(targetIndex - 1);
//                    targetIndex = Integer.parseInt(getUserInput());
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
                targetIndex = selectIndex();
            }
        }
        return null;
    }
    public int selectIndex(){
        System.out.print("인덱스 선택 : ");
        String userInput = getUserInput();
        return Integer.parseInt(userInput);
    }

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}