package contact;

public class Contact {
    private int pid;
    private String name;
    private String phoneNumber;
    private String groupName;
    private String memo;

    public Contact(int pid, String name, String phoneNumber, String groupName, String memo) {
        this.pid = pid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.groupName = groupName;
        this.memo = memo;
    }

    public Contact (int pid, String name, String phoneNumber) {
        this.pid = pid;
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroupName() {
        return groupName;
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
                "|" + phoneNumber +
                "|" + groupName +
                "|" + memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
