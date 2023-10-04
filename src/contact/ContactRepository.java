package contact;

import errors.exceptions.EntityNotFoundException;
import errors.exceptions.ErrorCode;

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
    /**
     * 수정된 내용을 담고 있는 객체와 이미 저장되어있던 객체를 바꾼다.
     * @param updatedContact : 사용자가 수정한 정보를 담고있는 객체로 pid값은 변하지 않는다.
     * */
    public void updateContact(Contact updatedContact) {
        int targetPid = updatedContact.getPid();
        if(!this.userTable.containsKey(targetPid))  {
            throw new EntityNotFoundException(ErrorCode.Entity_Not_found);
        }
        this.userTable.replace(targetPid, updatedContact);
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