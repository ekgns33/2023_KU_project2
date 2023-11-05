package contact;

import java.util.List;

public class Contact {
    private int pid;
    private String name;
    private PhoneNumber phoneNumber;
    private String groupName;
    private String memo;

    public Contact() {}

    public Contact(int pid, String name, String groupName, String memo) {
        this.pid = pid;
        this.name = name;
        this.phoneNumber = new PhoneNumber();
        this.groupName = groupName;
        this.memo = memo;
    }

    public Contact(String name, PhoneNumber phoneNumber, String groupName, String memo){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.groupName = groupName;
        this.memo = memo;
    }

    public Contact (int pid, String name) {
        this.pid = pid;
        this.name = name;
        this.phoneNumber = new PhoneNumber();
    }
    public Contact(int pid) {
        this.pid = pid;
        this.phoneNumber = new PhoneNumber();

    }
    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumber.insertPhoneNumber(phoneNumber);
    }

    public PhoneNumber getPhoneNumber() {
        return this.phoneNumber;
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

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMemo() {
        return memo;
    }

    @Override
    public String toString() {
        return  pid +
                "|" + name  +
                "|" + phoneNumber.toString() +
                "|" + groupName +
                "|" + memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
