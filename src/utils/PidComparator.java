package utils;

import contact.entity.Contact;
import contact.repositories.ContactRepository;

import java.util.Comparator;

public class PidComparator implements Comparator<Contact> {
    private static PidComparator pidComparator;

    public PidComparator() {

    }
    public static PidComparator getInstance() {
        if(pidComparator == null) {
            pidComparator  = new PidComparator();
        }
        return pidComparator;
    }

    @Override
    public int compare(Contact o1, Contact o2) {
        return o1.getPid() - o2.getPid();
    }
}
