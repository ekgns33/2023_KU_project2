package contact;

public final class ContactViewProvider {

    public ContactViewProvider() {}

    public static void addPropertyToContactView(StringBuilder sb, String tag, String property) {
        sb.append(tag).append(property).append('\n');
    }

    public static String buildContactView(Contact contact) {
        StringBuilder sb = new StringBuilder();
        String groupName = contact.getGroupName().equals("X") ? "" : contact.getGroupName();
        addPropertyToContactView(sb, "이름: ", contact.getName());
        addPropertyToContactView(sb, "전화번호: ", contact.getPhoneNumber().toString());
        addPropertyToContactView(sb, "그룹명: ", groupName);
        addPropertyToContactView(sb, "메모: ", contact.getMemo());

        return sb.toString();
    }

    public static void printContactView(Contact contact) {
        System.out.println(buildContactView(contact));
    }

}
