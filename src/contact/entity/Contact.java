package contact.entity;

import contact.entity.PhoneNumber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contact {
    private int pid;
    private String name;
    private PhoneNumber phoneNumber;
    private Set<String> groupList;
    private String memo;

//    public Contact() {}
//
//    public Contact(int pid, String name, String groupName, String memo) {
//        this.pid = pid;
//        this.name = name;
//        this.phoneNumber = new PhoneNumber();
//        this.groupName = groupName;
//        this.memo = memo;
//    }

    public Contact(String name, Set<String> phoneNumbers, Set<String> groupNames, String memo){
        this.name = name;
        this.phoneNumber = new PhoneNumber(phoneNumbers);
        this.groupList = groupNames;
        this.memo = memo;
    }

    public Contact (int pid, String name) {
        this.pid = pid;
        this.name = name;
        this.phoneNumber = new PhoneNumber();
        this.groupList = new HashSet<>();
    }

    public Contact(int pid) {
        this.pid = pid;
        this.phoneNumber = new PhoneNumber();
        this.groupList = new HashSet<>();
    }

    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumber.insertPhoneNumber(phoneNumber);
    }

    public boolean hasPhoneNumber(String phoneNumber) {
        return this.phoneNumber.hasPhoneNumber(phoneNumber);
    }

    public boolean hasGroupName(String groupName) {
        return this.groupList.contains(groupName);
    }

    public void addGroupName(String groupName) {
        this.groupList.add(groupName);
    }

    public void removeGroupName(String groupName) {
        this.groupList.remove(groupName);
    }

    public void modifyGroupName(String prevGroupName, String modifiedGroupName) {
        removeGroupName(prevGroupName);
        addGroupName(modifiedGroupName);
    }

    public List<String> getPhoneNumbersAsList() {
        return this.phoneNumber.getPhoneNumbers();
    }

    public int sizeOfPhoneNumbers() {
        return this.phoneNumber.size();
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int sizeOfGroupList() {
        return this.groupList.size();
    }

    public String getGroupListToString() {
        if(groupList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(String groupName: groupList) {
            sb.append(groupName);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    public String toString() {
        return  pid +
                "|" + name  +
                "|" + phoneNumber.toString() +
                "|" + getGroupListToString() +
                "|" + memo;
    }
}
