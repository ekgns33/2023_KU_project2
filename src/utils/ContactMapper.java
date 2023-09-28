package utils;

import contact.Contact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactMapper {

    public ContactMapper() {}

    public Map<Integer, Contact> mapListToHashMap(List<String> lines) {
        Map<Integer, Contact> map = new HashMap<>();

        for(String line : lines) {
            Contact contact = mapStringToContact(line);
            map.put(contact.getPid(), contact);
        }
        return map;
    }
    public Contact mapStringToContact(String line) {
        String[] tokens = line.split(" ");
        Contact contact = new Contact(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
        if(tokens.length >= 4) {
            contact.setGroupName(tokens[3]);
        }
        if(tokens.length == 5) {
            contact.setMemo(tokens[4]);
        }
        return contact;
    }
}
