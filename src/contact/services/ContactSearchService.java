package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
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
    };

    public static ContactSearchService getInstance() {
        if(contactSearchService == null) {
            contactSearchService = new ContactSearchService();
        }
        return contactSearchService;
    }

    public Contact search(int userInput){
        List<Contact> queryResult = searchByInputType(userInput);
        if(queryResult == null || queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return null;
        } else if(queryResult.get(0).getPid() == -1) {
            return queryResult.get(0);
        }
        else {
            showContactList(queryResult);
            return selectAndGetContact(queryResult);
        }
    }
    public void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.getName());
            index++;
        }
    }
    public List<Contact> searchByInputType(int userInput) {
        List<Contact> queryResult = null;
        String inputName, inputPhoneNumber, inputGroupName;
        switch (userInput) {
            case NAME:
                while(true) {
                    System.out.print("검색하실 이름을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputName = getUserInput().trim();
                    if(inputName.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidNameFormat(inputName);
                    if (check == -1) {
                        continue;
                    }
                    else break;
                }
                queryResult = contactRepository.findByName(inputName);
                break;
            case PHONENUMBER:
                while(true) {
                    System.out.print("검색하실 전화번호를 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputPhoneNumber = getUserInput().trim();
                    if(inputPhoneNumber.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidPhoneNumberFormat(inputPhoneNumber);
                    if(check == -1)
                        continue;
                    else break;
                }
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case GROUP:
                if(contactRepository.sizeOfGroup() == 0) {
                    System.out.println("현재 프로그램 내에 존재하는 그룹이 없습니다.");
                    Contact contact = new Contact(-1);
                    queryResult = new ArrayList<>();
                    queryResult.add(contact);
                    break;
                }
                while(true) {
                    System.out.print("검색하실 그룹을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputGroupName = getUserInput().trim();
                    if(inputGroupName.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidGroupNameFormat(inputGroupName);
                    if(check == -1)
                        continue;
                    else break;
                }
                queryResult = contactRepository.findByGroupName(inputGroupName);
                break;
            default:
                break;
        }
        return queryResult;
    }

}
