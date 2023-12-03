package group;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import contact.services.ServiceHelper;
import utils.Validator;

import java.util.List;
import java.util.Set;
import java.util.Map;
public class GroupService extends ServiceHelper {

    private static GroupService groupService;

    private final ContactRepository contactRepository;

    private static final int ADD = 1;
    private static final int DELETE = 2;
    private static final int UPDATE = 3;
    private static final int FAIL = -1;


    private GroupService() {
        contactRepository = ContactRepository.getInstance();
    }

    public static GroupService getInstance() {
        if (groupService == null) {
            groupService = new GroupService();
        }
        return groupService;
    }


    public void groupManagement(int userInput) {
        String suceed; // 각 groupManage 함수 내 리턴값을 담기 위한 변수

        switch (userInput) {
            case ADD:
                suceed = groupManageAdd(contactRepository);
                break;
            case DELETE:
                if (contactRepository.sizeOfGroup() != 0) {
                    suceed = groupManageDelete(contactRepository);
                } else {
                    System.out.println("현재 존재하는 그룹이 없습니다.");
                }
                break;
            case UPDATE:
                if (contactRepository.sizeOfGroup() != 0) {
                    suceed = groupManageModify(contactRepository);
                } else {
                    System.out.println("현재 존재하는 그룹이 없습니다.");
                }
                break;
            default:
                break;
        }
    }

    public String groupManageAdd(ContactRepository contactRepository) {
        String inputGroupName;
        String createDecision;
        while (true) {
            System.out.print("추가할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if (inputGroupName.equals("0")) {
                return null;
            }
            if (Validator.isValidGroupNameFormat(inputGroupName) == FAIL) {
                continue;
            }
            if (contactRepository.isGroupNameUnique(inputGroupName)) {
                System.out.println("이미 존재하는 그룹명입니다.");
            }
            while (true) {
                System.out.print("\'" + inputGroupName + "\' 그룹을 추가하시겠습니까?(Y/N)\n>> ");
                createDecision = getUserInput().trim();
                if (createDecision.equals("Y")) {
                    System.out.println("추가되었습니다.");
                    Set<String> groupCurrent = contactRepository.getGroupTable();
                    groupCurrent.add(inputGroupName);
                    return inputGroupName;
                } else if (createDecision.equals("N")) {
                    return null;
                }
                System.out.println("잘못된 입력 형식입니다.");
            }
        }
    }

    public String groupManageDelete(ContactRepository contactRepository) {
        String inputGroupName;
        String createDecision;
        while (true) {
            System.out.print("삭제할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if (inputGroupName.equals("0")) {
                return null;
            }
            if (Validator.isValidGroupNameFormat(inputGroupName) == FAIL) {
                continue;
            }
            if (!contactRepository.isGroupNameUnique(inputGroupName)) {
                System.out.println("존재하지 않는 그룹명입니다.");
                return null;
            }
            while (true) {
                System.out.print("삭제하시겠습니까?(Y/N)\n>> ");
                createDecision = getUserInput().trim();
                if (createDecision.equals("Y")) {
                    removeGroupFromContacts(inputGroupName);
                    ContactRepository.getInstance().getMappingTable().remove(inputGroupName);
                    Set<String> groupCurrent = contactRepository.getGroupTable();
                    groupCurrent.remove(inputGroupName);
                    return inputGroupName;
                } else if (createDecision.equals("N")) {
                    return null;
                } else {
                    System.out.println("잘못된 입력 형식입니다.");
                }
            }
        }
    }

    public void removeGroupFromContacts(String groupName) {
        List<Contact> totalContacts = contactRepository.findAll();
        for (Contact contact : totalContacts) {
                contact.removeGroupName(groupName);
        }
    }

    public boolean confirmModifyOperation() {
        while (true) {
            System.out.println("수정하시겠습니까?(Y/N)\n>> ");
            String decision = getUserInput().trim();
            if (decision.equals("Y")) {
                return true;
            } else if (decision.equals("N")) {
                return false;
            }
            System.out.println("잘못된 입력 형식입니다.");
        }
    }

    public String groupManageModify(ContactRepository contactRepository) {
        String inputGroupName;
        String modifiedGroupName = null;
        String createDecision;
        List<Contact> queryCurrent = contactRepository.findAll();
        while (true) {
            System.out.print("수정할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if (inputGroupName.equals("0")) {
                return null;
            }
            if (Validator.isValidGroupNameFormat(inputGroupName) == FAIL) {
                continue;
            }

            if (!confirmModifyOperation()) {
                return null;
            }
            while (true) {
                System.out.print("수정 후 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
                modifiedGroupName = getUserInput().trim();
                if (modifiedGroupName.equals("0")) {
                    return null;
                }
                if (Validator.isValidGroupNameFormat(modifiedGroupName) == -1) {
                    continue;
                }
                if (!confirmModifyOperation()) {
                    return null;
                }
                for (Contact contact : queryCurrent) {
                    if(contact.hasGroupName(inputGroupName)) {
                        contact.removeGroupName(inputGroupName);
                        contact.addGroupName(modifiedGroupName);
                    }
                }
                Map<String,Set<Integer>> table = ContactRepository.getInstance().getMappingTable();
                table.get(modifiedGroupName).addAll(table.get(inputGroupName));
                table.remove(inputGroupName);

                Set<String> groupCurrent = contactRepository.getGroupTable();
                if (!ContactRepository.getInstance().isGroupNameUnique(modifiedGroupName)) {
                    groupCurrent.add(modifiedGroupName);
                }
                groupCurrent.remove(inputGroupName);
                return modifiedGroupName;
            }
        }
    }
}
