package contact;

import contact.entity.Contact;

import java.util.List;

public final class ContactViewProvider {

    public ContactViewProvider() {}

    public static void addPropertyToContactView(StringBuilder sb, String tag, String property) {
        sb.append(tag).append(property).append('\n');
    }

    public static String buildContactView(Contact contact) {
        StringBuilder sb = new StringBuilder();
        String groupName = contact.getGroupName().equals("X") ? "" : contact.getGroupName();
        addPropertyToContactView(sb, "이름: ", contact.getName());
        addPropertyToContactView(sb, "전화번호: ", contact.getPhoneNumbersAsList().toString());
        addPropertyToContactView(sb, "그룹명: ", groupName);
        addPropertyToContactView(sb, "메모: ", contact.getMemo());

        return sb.toString();
    }

    public static void printContactView(Contact contact) {
        System.out.println(buildContactView(contact));
    }

    public static void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.getName());
            index++;
        }
    }

}
