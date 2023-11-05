package contact;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumber {

    private final List<String> phoneNumbers;

    public PhoneNumber(){
        this.phoneNumbers = new ArrayList<>();
    }

    public void insertPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getTargetPhoneNumber(int i){
        return phoneNumbers.get(i);
    }

    public int size() {
        return phoneNumbers.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String phoneNumber: phoneNumbers) {
            sb.append(phoneNumber);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();

    }


}
