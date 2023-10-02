package contact;

import java.util.*;

import errors.exceptions.ApplicationException;
import errors.exceptions.EntityNotFoundException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

class IndexContact{
    private int index;
    private Contact contact;
    public IndexContact(int index, Contact contact){
        this.index = index;
        this.contact = contact;
    }
    public int getIndex() {
        return index;
    }
    public Contact getContact() {
        return contact;
    }
}
public class ContactService {
    public ContactService(){}

    public Map<Integer, Contact> searchService(int userCommand, Map<Integer, Contact> userInfo) {
        // index., Contact 담을 arraylist 생성
        List<IndexContact> matchContacts = new ArrayList<>();
        findInfo(userCommand, userInfo, matchContacts);
        if (!matchContacts.isEmpty()) {
            while (true) {
                try {
                    System.out.print("인덱스 입력 : ");
                    // 인덱스 선택
                    String chooseIndex = getUserInput();
                    int userIndex = Integer.parseInt(chooseIndex);
                    if (userIndex == 0)break;
                    if (userIndex > matchContacts.size() || userIndex < 1)
                        throw new InvalidInputException(ErrorCode.Invalid_Input);
                    // 인덱스 선택 한 것 출력
                    return chooseInfo(userIndex, matchContacts, userInfo);
                } catch (ApplicationException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return null;
    }
    public void createService(){}
    public void deleteService(){}
    public void updateService(){}
    public void settingService(){}

    public void findInfo(int userCommand, Map<Integer, Contact> userInfo, List<IndexContact> matchContacts){
        try {
            String userInput = getUserInput();
            // 인덱스
            int index = 1;
            switch (userCommand) {
                // 각각의 경우 함수로 나중에 뺌
                case 1:
                    // 이름 입력받기 -> 예외 처리 X
                    for (Map.Entry<Integer, Contact> entry : userInfo.entrySet()) {
                        if (String.valueOf(entry.getValue().getName()).equals(userInput)) {
                            Contact contact = entry.getValue();
                            matchContacts.add(new IndexContact(index++, contact));
                        }
                    }
                case 2:
                    // 전화번호 입력받기 -> 예외 처리 X
                    for (Map.Entry<Integer, Contact> entry : userInfo.entrySet()) {
                        if (String.valueOf(entry.getValue().getPhoneNumber()).equals(userInput)) {
                            Contact contact = entry.getValue();
                            matchContacts.add(new IndexContact(index++, contact));
                        }
                    }
                case 3:
                    // 그룹 입력받기 -> 예외 처리 X
                    for (Map.Entry<Integer, Contact> entry : userInfo.entrySet()) {
                        if (String.valueOf(entry.getValue().getGroupName()).equals(userInput)) {
                            Contact contact = entry.getValue();
                            matchContacts.add(new IndexContact(index++, contact));
                        }
                    }
            }
            // 검색 결과 출력 또는 반환
            if (matchContacts.isEmpty()) {
                throw new EntityNotFoundException(ErrorCode.Entity_Not_found);
            }
        } catch(ApplicationException e){
            System.out.println(e.getMessage());
        }
        for (IndexContact indexedContact : matchContacts) {
            System.out.println("[" + indexedContact.getIndex() + "] " + indexedContact.getContact().toString());
        }
    }
    public Map<Integer, Contact> chooseInfo(int index, List<IndexContact> userList, Map<Integer, Contact> userInfo){
        IndexContact indexContact = userList.get(index-1);
        System.out.println(indexContact);
        Contact contact = indexContact.getContact();
        return searchMapByContact(userInfo, contact);
    }

    public Map<Integer, Contact> searchMapByContact(Map<Integer, Contact> origin, Contact contact){
        Map<Integer, Contact> resultMap = new HashMap<>();
        for (Map.Entry<Integer, Contact> entry : origin.entrySet()) {
            if (entry.getValue().equals(contact)) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        }
        return resultMap;
    }
    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}

