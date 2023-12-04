package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import errors.exceptions.ApplicationException;
import errors.exceptions.EntityNotFoundException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import utils.Validator;

import java.util.ArrayList;
import java.util.List;


public class ContactSearchService extends ServiceHelper {

    private static ContactSearchService contactSearchService;

    private final ContactRepository contactRepository;

    private final int NAME = 1;
    private final int PHONENUMBER = 2;
    private final int GROUP = 3;


    private ContactSearchService() {
        contactRepository = ContactRepository.getInstance();
    }

    ;

    public static ContactSearchService getInstance() {
        if (contactSearchService == null) {
            contactSearchService = new ContactSearchService();
        }
        return contactSearchService;
    }

    public Contact search(int userInput) {
        List<Contact> queryResult = searchByInputType(userInput);
        if(queryResult == null) {
            return null;
        }
        if (queryResult.isEmpty()) {
            System.out.println("일치하는 항목이 없습니다.");
            return null;
        }

        showContactList(queryResult);
        return selectAndGetContact(queryResult);

    }

    public void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.getName());
            index++;
        }
    }

    public List<String> tokenizeQuery(String userInput) {
        int l = 0;
        int r = 0;
        int n = userInput.length();
        List<String> ret = new ArrayList<>();
        while (r < n && l <= r) {

            while (r < n && userInput.charAt(r) != '&' && userInput.charAt(r) != '|') {
                r++;
            }
            if (r - l >= 1) {
                if (r - l == 1 && !Character.isAlphabetic(userInput.charAt(l))) {
                    throw new ApplicationException(ErrorCode.Invalid_Input);
                }
                String token = userInput.substring(l, r);
                String groupName = token;
                if (r - l > 1) {
                    groupName = groupName.substring(1);
                }
                if (Validator.isValidGroupNameFormat(groupName) == -1){
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
                ret.add(token);
            }
            l = r;
            r++;
        }
        return ret;
    }

    public List<Contact> searchByInputType(int userInput) {
        List<Contact> queryResult = new ArrayList<>();
        String inputName, inputPhoneNumber, inputGroupName;
        switch (userInput) {
            case NAME:
                while (true) {
                    System.out.print("검색하실 이름을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputName = getUserInput().trim();
                    if (inputName.equals("0")) {
                        return null;
                    }
                    if (Validator.isValidNameFormat(inputName) == -1) {
                        continue;
                    }
                    break;
                }
                queryResult = contactRepository.findByName(inputName);
                break;
            case PHONENUMBER:
                while (true) {
                    System.out.print("검색하실 전화번호를 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputPhoneNumber = getUserInput().trim();
                    if (inputPhoneNumber.equals("0")) {
                        return null;
                    }
                    int check = Validator.isValidPhoneNumberFormat(inputPhoneNumber);
                    if (check == -1) continue;
                    break;
                }
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case GROUP:
                if (contactRepository.sizeOfGroup() == 0) {
                    System.out.println("현재 프로그램 내에 존재하는 그룹이 없습니다.");
                    break;
                }

                List<String> tokens;
                while (true) {
                    try {
                        System.out.print("검색하실 그룹을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                        inputGroupName = getUserInput().trim();

                        tokens = this.tokenizeQuery(inputGroupName);

                        if (inputGroupName.equals("0")) {
                            return null;
                        }
                        for (String t : tokens) {
                            System.out.println(t);
                        }
                        queryResult = contactRepository.findByGroupName(tokens);
                        break;
                    } catch (EntityNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    } catch (ApplicationException e) {
                        System.out.println(e.getMessage());
                    }
                }
            default:
                break;
        }
        return queryResult;
    }
}
