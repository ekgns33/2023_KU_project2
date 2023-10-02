package contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactRepository {

    private Map<Integer, Contact> userTable;
    public ContactRepository () {
    };

    public void save(Contact input) {
        userTable.put(input.getPid(), input);
    }

    /**
     * 모든 레코드를 반환하는 메소드
     * @return List<Contact> 모든 객체정보가 담긴 리스트 반환
     * */
    public List<Contact> findAll() {
        return new ArrayList<>(this.userTable.values());
    }

    public List<Contact> findByName(String name) {
        List<Contact> queryResult = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            if(contact.getName().equals(name)) queryResult.add(contact);
        }
        return queryResult;
    }
    public List<Contact> findByPhoneNumber(String phoneNumber) {
        List<Contact> queryResult = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            if(contact.getPhoneNumber().equals(phoneNumber)) queryResult.add(contact);
        }
        return queryResult;
    }

    public List<Contact> findByGroupName(String groupName) {
        List<Contact> queryResult = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            if(contact.getGroupName().equals(groupName)) queryResult.add(contact);
        }
        return queryResult;
    }


    public void setUserTable(Map<Integer, Contact> userTable) {
        this.userTable = userTable;
    }

    public List<String> toStringList() {
        List<String> contactList = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            contactList.add(contact.toString());
        }
        return contactList;
    }
}