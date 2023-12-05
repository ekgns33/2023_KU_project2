package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import contact.ContactViewProvider;
import utils.Validator;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactDeleteService extends ServiceHelper {

    private static ContactDeleteService contactDeleteService;

    private final ContactRepository contactRepository;

    private ContactDeleteService() {
        contactRepository = ContactRepository.getInstance();
    }

    public static ContactDeleteService getInstance() {
        if (contactDeleteService == null) {
            contactDeleteService = new ContactDeleteService();
        }
        return contactDeleteService;
    }

    private boolean confirmDeleteOperation() {
        while (true) {
            System.out.print("삭제하시겠습니까?(Y/N)\n>> ");
            String decision = getUserInput().trim();
            if (decision.equals("Y")) {
                return true;
            } else if (decision.equals("N")) {
                return false;
            }
            System.out.println("잘못된 입력 형식입니다.");
        }
    }
    public void delete() {
        while (true) {
            System.out.print("삭제할 이름을 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
            String userInput = getUserInput().trim();

            if (userInput.equals("0")) {
                return;
            }

            if (Validator.isValidNameFormat(userInput) == -1) {
                continue;
            }

            List<Contact> queryResult = contactRepository.findByName(userInput);

            if (queryResult.isEmpty()) {
                System.out.println("일치하는 항목이 없습니다.");
                return;
            }

            ContactViewProvider.showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);

            if(selectedContact == null) return;

            if(!confirmDeleteOperation()) {
                System.out.println("취소합니다.");
                return;
            }

            contactRepository.removeContact(selectedContact.getPid());
            Map<String, Set<Integer>> mappingTable = contactRepository.getMappingTable();
            for(String groupName : selectedContact.getGroups()) {
                mappingTable.get(groupName).remove(selectedContact.getPid());
            }

            System.out.println("삭제되었습니다.");
            return;
        }
    }

}
