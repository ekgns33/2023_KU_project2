package contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactService {
    public ContactService(){}

    public void searchService(Integer userCommand, Map<Integer, Contact> userInfo){

        findInfo(userCommand, userInfo);
    }
    public void createService(){}
    public void deleteService(){}
    public void updateService(){}
    public void settingService(){}

    public void findInfo(Integer userCommand, Map<Integer, Contact> userInfo){
        String userInput;
        // index., Contact 담을 arraylist 생성
        List<IndexContact> matchContacts = new ArrayList<>();
        // 인덱스
        int index = 1;
        System.out.println(index);
        switch(userCommand){
            case 1:
                // 이름 입력받기 -> 예외 처리 X
                for(Map.Entry<Integer, Contact> entry : userInfo.entrySet()){
                    if(String.valueOf(entry.getValue().getName()).equals(userInput)){
                        Contact contact = entry.getValue();
                        matchContacts.add(new IndexContact(index++, contact));
                    }
                }
            case 2:
                // 전화번호 입력받기 -> 예외 처리 X
                for(Map.Entry<Integer, Contact> entry : userInfo.entrySet()){
                    if(String.valueOf(entry.getValue().getPhoneNumber()).equals(userInput)){
                        Contact contact = entry.getValue();
                        matchContacts.add(new IndexContact(index++, contact));
                    }
                }
            case 3:
                // 그룹 입력받기 -> 예외 처리 X
                for(Map.Entry<Integer, Contact> entry : userInfo.entrySet()){
                    if(String.valueOf(entry.getValue().getGroupName()).equals(userInput)){
                        Contact contact = entry.getValue();
                        matchContacts.add(new IndexContact(index++, contact));
                    }
                }
        }
        // 검색 결과 출력 또는 반환
        if (!matchContacts.isEmpty()) {
            for (IndexContact indexContact : matchContacts) {
                System.out.println("인덱스: " + indexContact.getIndex() + ", Contact: " + indexContact.getContact());
            }
        } else {
            System.out.println("검색 결과가 없습니다.");
        }
    }
}

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
