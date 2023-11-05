package contact;

import errors.exceptions.EntityNotFoundException;
import errors.exceptions.ErrorCode;
import utils.Validator;

import java.util.*;

public class ContactRepository {

    private int sortBy;
    private int lastPid;

    private Map<Integer, Contact> userTable;

    private ArrayList<String> groupTable = new ArrayList<>();

    private Set<String> phonNumberSet;

    private List<Contact> sequencedUserTable = new ArrayList<>();

    public ContactRepository () {
        this.phonNumberSet = new HashSet<>();
    };

    public void save(Contact input) {
        input.setPid(this.lastPid);
        userTable.put(this.lastPid, input);
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
            PhoneNumber ph = contact.getPhoneNumber();
            for(int i=0;i<ph.size();i++) {
                if(ph.getTargetPhoneNumber(i).equals(phoneNumber)) {
                    queryResult.add(contact);
                }
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
            for(String phoneNums : c.getPhoneNumber().getPhoneNumbers()) {
                if(phoneNums.matches(Validator.PHONENUM)) {
                    this.phonNumberSet.add(phoneNums);
                }
            }
//            this.phonNumberSet.addAll(c.getPhoneNumber().getPhoneNumbers());
        }
    }

    public List<Contact> getSequencedUserTable() {
        return sequencedUserTable;
    }
    public void setSequencedUserTable(int sortBy) {
        // 1. 가나다
        // 2. 그룹
        // 3. 최근(default)
        if(sortBy == 1) {
            List<Contact> userTable = findAll();
            Collections.sort(userTable, (c1, c2) -> c1.getName().compareTo(c2.getName()));
            this.sequencedUserTable.clear();
            for(Contact contact: userTable) {
                this.sequencedUserTable.add(contact);
            }
        }
        else if(sortBy == 2) {
            List<Contact> userTable = findAll();
            Collections.sort(userTable, (c1, c2) -> c1.getGroupName().compareTo(c2.getGroupName()));
            this.sequencedUserTable.clear();
            for(Contact contact : userTable) {
                this.sequencedUserTable.add(contact);
            }
        }
        else if(sortBy == 3) {
            List<Contact> userTable = findAll();
            Collections.sort(userTable, (c1, c2) -> Integer.compare(c2.getPid(), c1.getPid()));
            this.sequencedUserTable.clear();
            for(Contact contact : userTable) {
                this.sequencedUserTable.add(contact);
            }
        }
    }
    public Set<String> getPhoneNumberSet() {
        return this.phonNumberSet;
    }
    public boolean isNumberUnique(String phoneNumber) {
        return this.phonNumberSet.contains(phoneNumber);
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

    public ArrayList<String> getGroupTable(){
        return this.groupTable;
    }
    public void setGroupTable(ArrayList<String> groupTable){
        this.groupTable = groupTable;
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