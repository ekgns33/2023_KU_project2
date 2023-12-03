package contact.services;

import contact.entity.Contact;
import contact.repositories.ContactRepository;
import contact.ContactViewProvider;
import utils.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactCreateService extends ServiceHelper {

    private static ContactCreateService contactCreateService;

    private final ContactRepository contactRepository;

    private ContactCreateService() {
        this.contactRepository = ContactRepository.getInstance();
    }

    public static ContactCreateService getInstance() {
        if(contactCreateService == null) {
            contactCreateService = new ContactCreateService();
        }
        return contactCreateService;
    }

    public Contact create(){
        List<Contact> queryCurrent = contactRepository.findAll();
        Contact contact = buildContactInfo();
        if(contact == null){
            return null;
        }

        while(true) {
            ContactViewProvider.printContactView(contact);
            System.out.print("위와 같이 저장하시겠습니까?(Y/N)\n>> ");
            String createDecision = getUserInput().trim();
            if (createDecision.equals("Y")) {
                contactRepository.save(contact);
                for(String group : contact.getGroups()) {
                    contactRepository.addToMappingTable(group, contact.getPid());
                }
                break;
            } else if (createDecision.equals("N")) {
                return null;
            } else {
                System.out.println("잘못된 입력 형식입니다.");
            }
        }
        return contact;
    }

    private String readUserName() {
        while(true) {
            System.out.print("추가할 연락처의 이름을 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
            String userName = getUserInput().trim();
            if(userName.equals("0")) {
                return null;
            }
            if(Validator.isValidNameFormat(userName) == -1) continue;
            return userName;
        }
    }
    private boolean isRemoteNumberUnique(String inputPhoneNumber) {
        if (contactRepository.isNumberUnique(inputPhoneNumber)) {
            System.out.println("이미 존재하는 번호입니다.");
            return false;
        }
        return true;
    }



    private Set<String> readUserPhoneNumber() {
        Set<String> phoneNumbers = new HashSet<>();
        // 전화번호 입력
        while(true) {
            String userPhoneNumber;
            System.out.print("추가할 연락처의 전화번호를 입력하시오.\n더 이상 추가를 원하지 않으시면 'Q'를 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
            userPhoneNumber = getUserInput().trim();
            if(userPhoneNumber.equals("0")) {
                return null;
            }
            if(userPhoneNumber.equals("Q") && phoneNumbers.isEmpty()) {
                System.out.println("최소한 전화번호를 한 개 이상 추가하셔야 합니다.");
                continue;
            }
            if(userPhoneNumber.equals("Q")) {
                return phoneNumbers;
            }
            if (Validator.isValidPhoneNumberFormat(userPhoneNumber) == -1) {
                continue;
            }

            // 무선번호
            if (userPhoneNumber.matches(Validator.PHONENUM)) {
                boolean isUnique = isRemoteNumberUnique(userPhoneNumber);
                if(!isUnique) continue;
            }

            if(phoneNumbers.contains(userPhoneNumber)) {
                System.out.println("이미 존재하는 번호입니다.");
                continue;
            }

            phoneNumbers.add(userPhoneNumber);
        }
    }

    private Set<String> readUserGroup() {
        Set<String> groupNames = new HashSet<>();
        String userGroup;
        while(true) {
            System.out.print("추가할 연락처의 그룹명을 입력하시오.\n더 이상 추가를 원하지 않으면 'Q'를 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
            userGroup = getUserInput().trim();
            if(userGroup.isEmpty()) {
                System.out.println("올바른 입력 형식이 아닙니다.");
                continue;
            }
            if(userGroup.equals("0")) {
                return null;
            }
            if(userGroup.equals("Q")) {
                break;
            }
            if(Validator.isValidGroupNameFormat(userGroup) == -1) {
                continue;
            }
            if(contactRepository.isGroupNameUnique(userGroup)) {
                if(groupNames.contains(userGroup)) {
                    System.out.println("이미 추가하신 그룹명입니다.");
                    continue;
                }
                groupNames.add(userGroup);}
            else {
                System.out.println("현재 존재하는 그룹명이 아닙니다.");
            }
        }
        return groupNames;
    }

    private String readUserMemo() {
        String userMemo;
        while(true) {
            System.out.print("추가할 연락처의 메모를 입력하시오.\n메로 추가를 원하지 않을 시 'enter' 키를 누르시오.('0': 초기 메뉴로 이동)\n>>  ");
            userMemo = getUserInput().trim();
            if(userMemo.length() > 20) {
                System.out.println("잘못된 입력 형식입니다.");
                continue;
            }
            if (userMemo.trim().equals("0")) {
                return null;
            }
            return userMemo;
        }
    }

    private Contact buildContactInfo() {
        // 전화번호 입력 -> 에러 처리 구현 X
        String userName, userMemo;
        Set<String> userGroup, phoneNumbers;
        if((userName = readUserName()) == null) return null;
        if((phoneNumbers = readUserPhoneNumber()) == null) return null;
        if((userGroup = readUserGroup()) == null) return null;
        if((userMemo = readUserMemo()) == null) return null;

        return new Contact(userName, phoneNumbers, userGroup, userMemo);
    }
}
