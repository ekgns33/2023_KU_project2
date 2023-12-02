package contact.repositories;

import contact.entity.Contact;
import errors.exceptions.EntityNotFoundException;
import errors.exceptions.ErrorCode;
import utils.Validator;

import java.util.*;

public class ContactRepository {

    private static ContactRepository contactRepository;

    private ContactRepository () {
        this.phoneNumberSet = new HashSet<>();
        this.groupTable = new HashSet<>();
    };

    public static ContactRepository getInstance() {
        if(contactRepository == null) contactRepository = new ContactRepository();
        return contactRepository;
    }

    private int sortBy;
    private int lastPid;

    private Map<Integer, Contact> userTable;

    private Set<String> groupTable;

    private Set<String> phoneNumberSet;



    public void save(Contact input) {
        input.setPid(this.lastPid);
        userTable.put(this.lastPid, input);
        phoneNumberSet.addAll(input.getPhoneNumbersAsList());
        this.lastPid++;
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
            if(contact.hasPhoneNumber(phoneNumber)) {
                queryResult.add(contact);
            }
        }
        return queryResult;
    }

    public List<Contact> findByGroupName(String groupName) {
        List<Contact> queryResult = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            if(contact.getGroupName().equals(groupName))
                queryResult.add(contact);
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

    public void initPhoneNumberSet() {
        for(Contact c : userTable.values()) {
            for(String phoneNums : c.getPhoneNumbersAsList()) {
                if(phoneNumberSet.contains(phoneNums)) {
                    System.out.println("전화번호부 파일 형식에 오류가 있습니다.");
                    System.exit(1);
                }
                if(phoneNums.matches(Validator.PHONENUM)) {
                    this.phoneNumberSet.add(phoneNums);
                }
            }
        }
    }

    public void removePhoneNumber(String phoneNumber) {
        if(this.phoneNumberSet.contains(phoneNumber)) {
            phoneNumberSet.remove(phoneNumber);
        }
    }

    public void removeContact(int userPid) {
        List<String> removalNumbers = userTable.get(userPid).getPhoneNumbersAsList();
        removalNumbers.forEach(this.phoneNumberSet::remove);
        userTable.remove(userPid);
    }

    public boolean hasGroup(String groupName) {
        return groupTable.contains(groupName);
    }

    public int sizeOfGroup() {
        return groupTable.size();
    }

    public boolean hasPhoneNumber(String phoneNumber) {
        return phoneNumberSet.contains(phoneNumber);
    }

    public void setPhoneNumberSet(Set<String> phoneNumberSet) {
        this.phoneNumberSet = phoneNumberSet;
    }

    public Set<String> getPhoneNumberSet() {
        return this.phoneNumberSet;
    }
    public boolean isNumberUnique(String phoneNumber) {
        return this.phoneNumberSet.contains(phoneNumber);
    }

    public void setGroupTable(Set<String> groupTable) {
        this.groupTable = groupTable;
    }

    public Set<String> getGroupTable() {
        return groupTable;
    }

    public boolean isGroupNameUnique(String groupName) { return this.groupTable.contains(groupName);}
    public int getSortBy() {
        return sortBy;
    }

    public int getLastPid() {
        return lastPid;
    }
    public void setUserTable(Map<Integer, Contact> userTable) {
        this.userTable = userTable;
    }

    public Map<Integer, Contact> getUserTable() {
        return this.userTable;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public void setLastPid(int lastPid) {
        this.lastPid = lastPid;
    }
    public List<String> toStringList() {
        List<String> contactList = new ArrayList<>();
        for(Contact contact : this.userTable.values()) {
            contactList.add(contact.toString());
        }
        return contactList;
    }
}