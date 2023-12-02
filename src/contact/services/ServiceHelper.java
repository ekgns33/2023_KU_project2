package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import utils.Validator;

import java.util.*;

public class ServiceHelper {

    public Contact selectAndGetContact(List<Contact> queryResult) {
        int targetIndex = selectIndex();
        while(targetIndex != 0) {
            try {
                if (targetIndex > queryResult.size() || targetIndex < 0) {
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
                return queryResult.get(targetIndex - 1);
            } catch(NumberFormatException e1) {
                System.out.println("잘못된 입력 형식입니다.");
                targetIndex = selectIndex();
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
                targetIndex = selectIndex();
            }
        }
        return null;
    }

    public int selectIndex(){
        String userInput=null;
        int index=0;
        try {
            System.out.print("인덱스 선택\n>> ");
            userInput = getUserInput().trim();
            index = Integer.parseInt(userInput);
        } catch(NumberFormatException e) {
            index = -1;
        }
        return index;
    }

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }

}