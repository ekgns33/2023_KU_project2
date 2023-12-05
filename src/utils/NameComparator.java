package utils;

import contact.entity.Contact;

import java.util.Comparator;

public class NameComparator implements Comparator<Contact> {

    private static NameComparator nameComparator;

    private NameComparator() {


    }

    public static NameComparator getInstance() {
        if(nameComparator == null) {
            nameComparator = new NameComparator();
        }
        return nameComparator;
    }
    @Override
    public int compare(Contact o1, Contact o2) {
        return o1.getName().compareTo(o2.getName());
    }
}