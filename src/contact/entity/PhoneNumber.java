package contact.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhoneNumber {

    private final Set<String> phoneNumbers;

    public PhoneNumber() {
        this.phoneNumbers = new HashSet<>();
    }

    public PhoneNumber(Set<String> phoneNumbers){
        this.phoneNumbers = phoneNumbers;
    }

    public void insertPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public List<String> getPhoneNumbers() {
        return new ArrayList<>(phoneNumbers);
    }

    public int size() {
        return phoneNumbers.size();
    }

    public boolean hasPhoneNumber(String number) {
        return this.phoneNumbers.contains(number);
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
