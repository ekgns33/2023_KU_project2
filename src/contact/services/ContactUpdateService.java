package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import contact.ContactViewProvider;
import utils.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactUpdateService extends ServiceHelper {

    public static ContactUpdateService contactUpdateService;

    private final ContactRepository contactRepository;

    private ContactUpdateService() {
        this.contactRepository = ContactRepository.getInstance();
    }

    public static ContactUpdateService getInstance() {
        if (contactUpdateService == null) {
            contactUpdateService = new ContactUpdateService();
        }
        return contactUpdateService;
    }

    public boolean updateName(Contact prevContact, Contact selectedContact) {
        while (true) {
            System.out.print("수정할 이름을 입력하시오.('0': 수정 메뉴로 이동)\n(" + prevContact.getName() + ")>> ");
            String inputName = getUserInput().trim();
            if (inputName.equals("0"))
                return false;
            if (Validator.isValidNameFormat(inputName) == -1)
                continue;
            //validate
            selectedContact.setName(inputName);
            break;
        }
        return true;
    }

    // inputPhoneNumber은 무선 전화 또는 유선전화의 형식으로만 주어짐.
    public boolean checkPhoneNumber(String inputPhoneNumber, Contact selectedContact, Set<String> copiedSet) {
        // 입력한 번호가 형식에 맞는지 확인.
        if (inputPhoneNumber.matches(Validator.PHONENUM)) {
            // 이미 존재하는 번호인지 확인.
            if (copiedSet.contains(inputPhoneNumber)) {
                System.out.println("이미 존재하는 번호입니다.");
                return false;
            }
        }
        if (selectedContact.hasPhoneNumber(inputPhoneNumber)) {
            System.out.println("이미 존재하는 번호입니다.");
            return false;
        }
        return true;

    }

    public boolean updatePhoneNumber(Contact prevContact, Contact selectedContact, Set<String> copiedSet) {

        // 연락처 수정시 사용자가 모든 번호를 입력하므로 Set에 존재하는 번호 제거.
        prevContact.getPhoneNumbersAsList().forEach(copiedSet::remove);

        while (true) {

            System.out.println("수정할 전화번호를 입력하시오.\n더 이상 추가를 원하지 않으시면 'Q'를 입력하시오.('0': 수정 메뉴로 이동)");
            System.out.println("(" + prevContact.getPhoneNumbersAsList().toString() + ")>> ");
            String inputPhoneNumber = getUserInput().trim();
            if (inputPhoneNumber.equals("0")) {
                return false;
            }
            if (inputPhoneNumber.equals("Q") && selectedContact.sizeOfPhoneNumbers() == 0) {
                System.out.println("최소한 전화번호 한 개 이상 추가하셔야 합니다.");
                continue;
            }
            if (inputPhoneNumber.equals("Q"))
                break;
            if (Validator.isValidPhoneNumberFormat(inputPhoneNumber) == -1)
                continue;

            if (!checkPhoneNumber(inputPhoneNumber, selectedContact, copiedSet)) {
                continue;
            }

            selectedContact.addPhoneNumber(inputPhoneNumber);
        }
        return true;
    }

    public boolean updateGroupInfo(Contact prevContact, Contact selectedContact) {
        Set<String> groupNames = new HashSet<>();
        String inputGroupName;
        // copied???
        while (true) {
            System.out.print("수정할 그룹명을 입력하시오.\n더 이상 수정을 원하지 않으면 'Q'를 입력하시오.('0': 수정 메뉴로 이동)\n(" + prevContact.getGroupListToString() + ")>> ");
            inputGroupName = getUserInput().trim();
            if (inputGroupName.isEmpty()) {
                System.out.println("올바른 입력 형식이 아닙니다.");
                continue;
            }
            if (inputGroupName.equals("0")) {
                return false;
            }
            if (inputGroupName.equals("Q")) {
                break;
            }
            if (Validator.isValidGroupNameFormat(inputGroupName) == -1) {
                continue;
            }
            if (!contactRepository.isGroupNameUnique(inputGroupName)) {
                System.out.println("현재 존재하는 그룹명이 아닙니다.");
                continue;
            }
            if (groupNames.contains(inputGroupName)) {
                System.out.println("이미 추가하신 그룹명입니다.");
                continue;
            }
            groupNames.add(inputGroupName);
        }
        return true;
    }

    public boolean updateMemo(Contact prevContact, Contact selectedContact) {
        String inputMemo;
        while (true) {
            System.out.print("수정할 메모를 입력하시오.\n(" + prevContact.getMemo() + ")>> ");
            inputMemo = getUserInput().trim();
            if (inputMemo.length() > 20) {
                System.out.println("잘못된 입력 형식입니다.");
                continue;
            }
            if (inputMemo.trim().equals("0")) {
                return false;
            }
            selectedContact.setMemo(inputMemo);
            return true;
        }
    }

    public void update(int userInput) {
        // 검색기능 그대로 사용한다.
        List<Contact> queryResult = ContactSearchService.getInstance().searchByInputType(userInput);
        if (queryResult == null || queryResult.isEmpty()) {
            System.out.println("일치하는 항목이 없습니다.");
        } else if (queryResult.get(0).getPid() == -1) {
            return;
        } else {
            ContactViewProvider.showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);
            if (selectedContact == null) {
                return;
            }
            Contact updateContact = new Contact(selectedContact.getPid());
            while (true) {
                System.out.print("수정하시겠습니까?(Y/N)\n>> ");
                String updateDecision = getUserInput().trim();
                if (updateDecision.equals("Y")) {

                    Set<String> copiedSet = new HashSet<>(contactRepository.getPhoneNumberSet());
                    if (!updateName(selectedContact, updateContact)) {
                        return;
                    }
                    if (!updatePhoneNumber(selectedContact, updateContact, copiedSet)) {
                        return;
                    }
                    if (!updateGroupInfo(selectedContact, updateContact)) {
                        return;
                    }
                    if (!updateMemo(selectedContact, updateContact)) {
                        return;
                    }

                    ContactViewProvider.printContactView(updateContact);


                    while (true) {
                        System.out.print("위와 같이 수정하시겠습니까?(Y/N)\n>> ");
                        String saveDecision = getUserInput().trim();
                        if (saveDecision.equals("Y")) {
                            System.out.println("수정되었습니다.");
                            // 새로운 전역 set적용.
                            contactRepository.setPhoneNumberSet(copiedSet);
                            contactRepository.updateContact(updateContact);
                            return;
                        } else if (saveDecision.equals("N")) {
                            return;
                        } else {
                            System.out.println("잘못된 입력 형식입니다.");
                        }
                    }
                } else if (updateDecision.equals("N")) {
                    return;
                } else {
                    System.out.println("잘못된 입력 형식입니다.");
                }
            }
        }
    }


}
