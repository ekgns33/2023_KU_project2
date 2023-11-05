package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import utils.Validator;

import java.util.*;

public class ContactService {

    public ContactService(){
    }

    public Contact search(int userInput, ContactRepository contactRepository){
        List<Contact> queryResult = searchByInputType(userInput, contactRepository);
        if(queryResult == null || queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return null;
        } else if(queryResult.get(0).getPid() == -1) {
            return queryResult.get(0);
        }
        else {
            showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);
            return selectedContact;
        }
    }
    public void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.getName());
            index++;
        }
    }
    public List<Contact> searchByInputType(int userInput, ContactRepository contactRepository) {
        List<Contact> queryResult = null;
        String inputName, inputPhoneNumber, inputGroupName;
        switch (userInput) {
            case 1:
                while(true) {
                    System.out.print("검색하실 이름을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputName = getUserInput().trim();
                    if(inputName.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidNameFormat(inputName);
                    if (check == -1) {
                        continue;
                    }
                    else break;
                }
                queryResult = contactRepository.findByName(inputName);
                break;
            case 2:
                while(true) {
                    System.out.print("검색하실 전화번호를 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputPhoneNumber = getUserInput().trim();
                    if(inputPhoneNumber.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidPhoneNumberFormat(inputPhoneNumber);
                    if(check == -1)
                        continue;
                    else break;
                }
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case 3:
                if(contactRepository.getGroupTable().size() == 0) {
                    System.out.println("현재 프로그램 내에 존재하는 그룹이 없습니다.");
                    Contact contact = new Contact(-1);
                    queryResult = new ArrayList<>();
                    queryResult.add(contact);
                    break;
                }
                while(true) {
                    System.out.print("검색하실 그룹을 입력하시오.('0': 검색 메뉴로 이동)\n>> ");
                    inputGroupName = getUserInput().trim();
                    if(inputGroupName.equals("0")) {
                        Contact cancel = new Contact(-1);
                        queryResult = new ArrayList<>();
                        queryResult.add(cancel);
                        return queryResult;
                    }
                    int check = Validator.isValidGroupNameFormat(inputGroupName);
                    if(check == -1)
                        continue;
                    else break;
                }
                queryResult = contactRepository.findByGroupName(inputGroupName);
                break;
            default:
                break;
        }
        return queryResult;
    }
    public Contact selectAndGetContact(List<Contact> queryResult) {
        int targetIndex = selectIndex();
        while(targetIndex != 0) {
            try {
                if (targetIndex > queryResult.size() || targetIndex < 0) {
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                }
                return queryResult.get(targetIndex - 1);
            } catch(NumberFormatException e1) {
                System.out.println("잘못된 입력 형식입니다.");
                targetIndex = selectIndex();
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
                targetIndex = selectIndex();
            }
        }
        return null;
    }

    public Contact create(ContactRepository contactRepository){
        List<Contact> queryCurrent = contactRepository.findAll();
        Contact contact = createContactInfo(contactRepository);
        if(contact == null){
            return null;
        }
        else{
            // Y
            while(true){
                String print = printContact(contact);
                System.out.println(print);
                System.out.print("위와 같이 저장하시겠습니까?(Y/N)\n>> ");
                String createDecision = getUserInput().trim();
                if(createDecision.equals("Y")) {
                    contactRepository.save(contact);
                    break;
                }
                else if(createDecision.equals("N")){
                    return null;
                }
                else{
                    System.out.println("잘못된 입력 형식입니다.");
                }
            }
            return contact;
        }
    }

    public Contact createContactInfo(ContactRepository contactRepository) {
        // 전화번호 입력 -> 에러 처리 구현 X
        String userNameInput, userNumInput = null;
        String userGroupInput, userMemoInput;
        int check;
        PhoneNumber phoneNumber = new PhoneNumber();
        while (true) {
            try {
                // 이름 입력
                System.out.print("추가할 연락처의 이름을 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
                userNameInput = getUserInput().trim();
                if(userNameInput.equals("0")) {
                    return null;
                }
                if (Validator.isValidNameFormat(userNameInput) == -1)
                    throw new InvalidInputException(ErrorCode.Invalid_Input);
                // 전화번호 입력
                while(true) {
                    System.out.print("추가할 연락처의 전화번호를 입력하시오.\n더 이상 추가를 원하지 않으시면 'Q'를 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
                    userNumInput = getUserInput().trim();
                    if(userNumInput.equals("0")) {
                        return null;
                    }
                    if(userNumInput.equals("Q") && phoneNumber.getPhoneNumbers().size() == 0) {
                        System.out.println("최소한 전화번호를 한 개 이상 추가하셔야 합니다.");
                        continue;
                    }
                    if(userNumInput.equals("Q") && phoneNumber.getPhoneNumbers().size() > 0) {
                        break;
                    }
                    if (Validator.isValidPhoneNumberFormat(userNumInput) == -1) {
                        continue;
                    }
                    if (userNumInput.matches(Validator.PHONENUM)) {
                        // 무선
                        // set에 있는 애들이랑 비교해야되고
                        // 현재 추가하고 있는 자신의 번호들은 set에 없어서 자기 자신 번호랑도 비교해야 함
                        if (contactRepository.isNumberUnique(userNumInput)) {
                            System.out.println("이미 존재하는 번호입니다.");
                            continue;
                        }
                        check = 0;
                        for(String phoneNumbers : phoneNumber.getPhoneNumbers()) {
                            if(userNumInput.equals(phoneNumbers)) {
                                check = 1;
                                System.out.println("이미 존재하는 번호입니다.");
                                break;
                            }
                        }
                        if(check == 1)
                            continue;
                        phoneNumber.insertPhoneNumber(userNumInput);
                    } else {
                        // 유선
                        // 자기 자신이랑만 비교하면 되고
                        check = 0;
                        for (String phoneNumbers : phoneNumber.getPhoneNumbers()) {
                            if (userNumInput.equals(phoneNumbers)) {
                                System.out.println("이미 존재하는 번호입니다.");
                                check = 1;
                            }
                        }
                        if(check == 1)
                            continue;
                        phoneNumber.insertPhoneNumber(userNumInput);
                    }
                }
                // 그룹 입력 -> 에러 처리 구현 X
                while(true) {
                    System.out.print("추가할 연락처의 그룹명을 입력하시오.\n그룹명 추가를 원하지 않을 시 'enter' 키를 누르시오.('0': 초기 메뉴로 이동)\n>> ");
                    userGroupInput = getUserInput().trim();
                    if(userGroupInput.equals("")) {
                        userGroupInput = "X";
                        break;
                    }
                    if(userGroupInput.equals("0")) {
                        return null;
                    }
                    if(Validator.isValidGroupNameFormat(userGroupInput) == -1) {
                        continue;
                    }
                    else {
                        if(contactRepository.isGroupNameUnique(userGroupInput)) {
                            break;
                        }
                        else {
                            System.out.println("현재 존재하는 그룹명이 아닙니다.");
                            continue;
                        }
                    }
                }
                // 메모 입력
                while(true) {
                    System.out.print("추가할 연락처의 메모를 입력하시오.\n메로 추가를 원하지 않을 시 'enter' 키를 누르시오.('0': 초기 메뉴로 이동)\n>>  ");
                    userMemoInput = getUserInput().trim();
                    if(userMemoInput.length() > 20) {
                        System.out.println("잘못된 입력 형식입니다.");
                        continue;
                    }
                    if (userMemoInput.trim().equals("0")) {
                        return null;
                    }
                    if (userMemoInput == null) {
                        userMemoInput = "";
                    }
                    break;
                }
                break;
            } catch(ApplicationException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
        return new Contact(userNameInput, phoneNumber, userGroupInput, userMemoInput);
    }
    public void update(int userInput, ContactRepository contactRepository) {
        // 검색기능 그대로 사용한다.
        List<Contact> queryResult = searchByInputType(userInput, contactRepository);
        if (queryResult == null || queryResult.isEmpty()) {
            System.out.println("일치하는 항목이 없습니다.");
        }
        else {
            showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);
            while (true) {
                System.out.print("수정하시겠습니까?(Y/N)\n>> ");
                String updateDecision = getUserInput().trim();
                if (updateDecision.equals("Y")) {
                    Contact updateContact = new Contact(selectedContact.getPid());

                    while (true) {
                        System.out.print("수정할 이름을 입력하시오.('0': 수정 메뉴로 이동)\n(" + selectedContact.getName() + ")>> ");
                        String inputName = getUserInput().trim();
                        if(inputName.equals("0"))
                            return;
                        if (Validator.isValidNameFormat(inputName) == -1)
                            continue;
                        //validate
                        updateContact.setName(inputName);
                        break;
                    }
                    while (true) {
                        int check = 0;
                        System.out.println("수정할 전화번호를 입력하시오.\n더 이상 추가를 원하지 않으시면 'Q'를 입력하시오.('0': 수정 메뉴로 이동)");
                        System.out.println("(" + selectedContact.getPhoneNumber().toString() + ")>> ");
                        String inputNum = getUserInput().trim();
                        if (inputNum.equals("0")) {
                            return;
                        }
                        if (inputNum.equals("Q") && updateContact.getPhoneNumber().getPhoneNumbers().size() == 0) {
                            System.out.println("최소한 전화번호 한 개 이상 추가하셔야 합니다.");
                            continue;
                        }
                        if (inputNum.equals("Q") && updateContact.getPhoneNumber().getPhoneNumbers().size() > 0)
                            break;
                        if (Validator.isValidPhoneNumberFormat(inputNum) == -1)
                            continue;
                        Set<String> phoneNumExceptMine = new HashSet<>(contactRepository.getPhoneNumberSet());
                        for (String numbers : contactRepository.getPhoneNumberSet()) {
                            for (String phoneNumbers : selectedContact.getPhoneNumber().getPhoneNumbers()) {
                                if (numbers.equals(phoneNumbers))
                                    phoneNumExceptMine.remove(numbers);
                            }
                        }
                        check = 0;
                        if (inputNum.matches(Validator.PHONENUM)) {
                            check = 0;
                            for (String numbers : phoneNumExceptMine) {
                                if (inputNum.equals(numbers)) {
                                    check = 1;
                                    System.out.println("이미 존재하는 번호입니다.");
                                    break;
                                }
                            }
                            if (check == 1)
                                continue;
                            check = 0;
                            for (String numbers : updateContact.getPhoneNumber().getPhoneNumbers()) {
                                if (inputNum.equals(numbers)) {
                                    check = 1;
                                    System.out.println("이미 존재하는 번호입니다.");
                                    break;
                                }
                            }
                            if (check == 1)
                                continue;
                            updateContact.getPhoneNumber().getPhoneNumbers().add(inputNum);
                        } else {
                            check = 0;
                            for (String numbers : updateContact.getPhoneNumber().getPhoneNumbers()) {
                                if (inputNum.equals(numbers)) {
                                    check = 1;
                                    System.out.println("이미 존재하는 번호입니다.");
                                    break;
                                }
                            }
                            if (check == 1)
                                continue;
                            updateContact.getPhoneNumber().getPhoneNumbers().add(inputNum);
                        }
                    }
                    String inputGroupName;
                    while (true) {
                        System.out.print("수정할 그룹명을 입력하시오.\n그룹명 추가를 원하지 않을 시 'enter'키를 누르시오.('0': 수정 메뉴로 이동)\n(" + selectedContact.getGroupName() + ")>> ");
                        inputGroupName = getUserInput().trim();
                        if (inputGroupName.equals("")) {
                            inputGroupName = "X";
                            break;
                        }
                        if (inputGroupName.equals("0")) {
                            return;
                        }
                        if (Validator.isValidGroupNameFormat(inputGroupName) == -1) {
                            continue;
                        } else {
                            if (contactRepository.isGroupNameUnique(inputGroupName)) {
                                break;
                            } else {
                                System.out.println("현재 존재하는 그룹명이 아닙니다.");
                                continue;
                            }
                        }
                    }
                    updateContact.setGroupName(inputGroupName);
                    String inputMemo;
                    while(true) {
                        System.out.print("수정할 메모를 입력하시오.\n(" + selectedContact.getMemo() + ")>> ");
                        inputMemo = getUserInput().trim();
                        if(inputMemo.length() > 20) {
                            System.out.println("잘못된 입력 형식입니다.");
                            continue;
                        }
                        if (inputMemo.trim().equals("0")) {
                            return;
                        }
                        if (inputMemo == null) {
                            inputMemo = "";
                        }
                        break;
                    }
                    updateContact.setMemo(inputMemo);
                    String print = printContact(updateContact);
                    while(true) {
                        System.out.println(print);
                        System.out.print("위와 같이 수정하시겠습니까?(Y/N)\n>> ");
                        String saveDecision = getUserInput().trim();
                        if (saveDecision.equals("Y")) {
                            System.out.println("수정되었습니다.");
                            contactRepository.updateContact(updateContact);
                            return;
                        } else if (saveDecision.equals("N")) {
                            return;
                        } else {
                            System.out.println("잘못된 입력 형식입니다.");
                            continue;
                        }
                    }
                } else if (updateDecision.equals("N")) {
                    return;
                } else {
                    System.out.println("잘못된 입력 형식입니다.");
                    continue;
                }
            }
        }
    }

    public void delete(ContactRepository contactRepository) {
        while(true) {
            System.out.print("삭제할 이름을 입력하시오.('0': 초기 메뉴로 이동)\n>> ");
            String userInput = getUserInput().trim();
            if(userInput.equals("0"))
                break;
            if(Validator.isValidNameFormat(userInput) == -1) {
                continue;
            }
            else {
                List<Contact> queryResult = contactRepository.findByName(userInput);
                if(queryResult == null || queryResult.isEmpty()){
                    System.out.println("일치하는 항목이 없습니다.");
                    break;
                } else {
                    showContactList(queryResult);
                    Contact selectedContact = selectAndGetContact(queryResult);
                    if(selectedContact == null)
                        break;
                    while(true) {
                        System.out.print("삭제하시겠습니까?(Y/N)\n>> ");
                        String decision = getUserInput().trim();
                        if(decision.equals("Y")) {
                            System.out.println("삭제되었습니다.");
                            break;
                        }
                        else if(decision.equals("N"))
                            return;
                        else {
                            System.out.println("잘못된 입력 형식입니다.");
                            continue;
                        }
                    }
                    contactRepository.getUserTable().remove(selectedContact.getPid());
                    if(selectedContact.getPid() == contactRepository.getLastPid()-1) {
                        contactRepository.setLastPid(contactRepository.getLastPid()-1);
                    }
                    break;
                }
            }
        }
    }

    public void groupManagement(int userInput, ContactRepository contactRepository){
        String suceed; // 각 groupManage 함수 내 리턴값을 담기 위한 변수
        switch (userInput) {
            case 1:
                suceed = groupManageAdd(contactRepository);
                break;
            case 2:
                if(contactRepository.getGroupTable().size() != 0) {
                    suceed = groupManageDelete(contactRepository);
                }
                else {
                    System.out.println("현재 존재하는 그룹이 없습니다.");
                }
                break;
            case 3:
                if(contactRepository.getGroupTable().size() != 0) {
                    suceed = groupManageModify(contactRepository);
                }
                else {
                    System.out.println("현재 존재하는 그룹이 없습니다.");
                }
                break;
            default:
                break;
        }
    }
    public String groupManageAdd(ContactRepository contactRepository){
        String inputGroupName;
        String createDecision;
        while(true) {
            System.out.print("추가할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if(inputGroupName.equals("0")){
                return null;
            }
            else if (Validator.isValidGroupNameFormat(inputGroupName) == -1) {
                continue;
            } else {
                if (contactRepository.isGroupNameUnique(inputGroupName)) {
                    System.out.println("이미 존재하는 그룹명입니다.");
                    continue;
                } else {
                    while(true) {
                        System.out.print("\'" + inputGroupName + "\' 그룹을 추가하시겠습니까?(Y/N)\n>> ");
                        createDecision = getUserInput().trim();
                        if (createDecision.equals("Y")) {
                            System.out.println("추가되었습니다.");
                            ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                            groupCurrent.add(inputGroupName);
                            contactRepository.setGroupTable(groupCurrent);
                            break;
                        } else if (createDecision.equals("N")) {
                            return null;
                        } else {
                            System.out.println("잘못된 입력 형식입니다.");
                        }
                    }
                    break;
                }
            }
        }
        return inputGroupName;
    }

    public String groupManageDelete(ContactRepository contactRepository){
        String inputGroupName;
        String createDecision;
        while(true) {
            System.out.print("삭제할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if(inputGroupName.equals("0")){
                return null;
            }
            else if (Validator.isValidGroupNameFormat(inputGroupName) == -1) {
                continue;
            }
            else if (!contactRepository.isGroupNameUnique(inputGroupName)) {
                System.out.println("존재하지 않는 그룹명입니다.");
            }
            else {
                while(true) {
                    System.out.print("삭제하시겠습니까?(Y/N)\n>> ");
                    createDecision = getUserInput().trim();
                    if (createDecision.equals("Y")) {
                        List<Contact> queryCurrent = contactRepository.findAll();
                        for (Contact contact : queryCurrent) {
                            if (contact.getGroupName().equals(inputGroupName)) {
                                contact.setGroupName("X");
                            }
                        }
                        ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                        groupCurrent.remove(inputGroupName);
                        contactRepository.setGroupTable(groupCurrent);
                        break;
                    } else if (createDecision.equals("N")) {
                        return null;
                    } else {
                        System.out.println("잘못된 입력 형식입니다.");
                    }
                }
                break;
            }
        }
        return inputGroupName;
    }

    public String groupManageModify(ContactRepository contactRepository){
        String inputGroupName;
        String modifiedGroupName=null;
        String createDecision;
        List<Contact> queryCurrent = contactRepository.findAll();
        while(true){
            System.out.print("수정할 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
            inputGroupName = getUserInput().trim();
            if(inputGroupName.equals("0")){
                return null;
            }
            else if(Validator.isValidGroupNameFormat(inputGroupName) == -1) {
                continue;
            }
            else if(!contactRepository.isGroupNameUnique(inputGroupName)){
                System.out.println("존재하지 않는 그룹명입니다.");
            }
            else{
                while(true) {
                    System.out.println("수정하시겠습니까?(Y/N)\n>> ");
                    String decision = getUserInput().trim();
                    if(decision.equals("Y")) {
                        break;
                    }
                    else if(decision.equals("N")) {
                        return null;
                    }
                    else {
                        System.out.println("잘못된 입력 형식입니다.");
                        continue;
                    }
                }
                while(true) {
                    System.out.print("수정 후 그룹명을 입력하시오.('0': 그룹 관리 메뉴로 이동)\n>> ");
                    modifiedGroupName = getUserInput().trim();
                    if (modifiedGroupName.equals("0")) {
                        return null;
                    } else if (Validator.isValidGroupNameFormat(modifiedGroupName) == -1) {
                        continue;
                    } else if (contactRepository.isGroupNameUnique(modifiedGroupName)) {
                        System.out.println("이미 존재하는 그룹명입니다.");
                    } else {
                        while (true) {
                            System.out.print("수정하시겠습니까?(Y/N)\n>> ");
                            createDecision = getUserInput().trim();
                            if (createDecision.equals("Y")) {
                                for (Contact contact : queryCurrent) {
                                    if (contact.getGroupName().equals(inputGroupName)) {
                                        contact.setGroupName(modifiedGroupName);
                                    }
                                }
                                ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                                for (int i = 0; i < groupCurrent.size(); i++) {
                                    String currentGroupName = groupCurrent.get(i);
                                    if (currentGroupName.equals(inputGroupName)) {
                                        groupCurrent.set(i, modifiedGroupName);
                                    }
                                }
                                contactRepository.setGroupTable(groupCurrent);
                                break;
                            }
                            else if (createDecision.equals("N")) {
                                return null;
                            }
                            else {
                                System.out.println("잘못된 입력 형식입니다.");
                                continue;
                            }
                        }
                    }
                }
            }
            break;
        }
        return modifiedGroupName;
    }

    public void modifyConfig(int userInput, ContactRepository contactRepository) {
        int resultValue;
        switch(userInput) {
            case 1:
                resultValue = sortByKoreanName(contactRepository);
                contactRepository.setSortBy(resultValue);
                break;
            case 2:
                resultValue = sortByGroupName(contactRepository);
                contactRepository.setSortBy(resultValue);
                break;
            case 3:
                resultValue = sortByRecentStored(contactRepository);
                contactRepository.setSortBy(resultValue);
                break;
            default:
                break;
        }
    }

    public int sortByKoreanName(ContactRepository contactRepository) {
        String createDecision;
        while(true) {
            System.out.println("이름 가나다순으로 정렬하시겠습니까?(Y/N)\n>> ");
            createDecision = getUserInput().trim();
            if(createDecision.equals("Y")) {
                List<Contact> userTable = contactRepository.findAll();
                Collections.sort(userTable, (c1, c2) -> c1.getName().compareTo(c2.getName()));
                contactRepository.getSequencedUserTable().clear();
                for(Contact contact: userTable) {
                    contactRepository.getSequencedUserTable().add(contact);
                }
                break;
            }
            else if(createDecision.equals("N")) {
                return 0;
            }
            else {
                System.out.println("잘못된 입력 형식입니다.");
                continue;
            }
        }
        return 1;
    }

    public int sortByGroupName(ContactRepository contactRepository) {
        String createDecision;
        while(true) {
            System.out.print("그룹별로 정렬하시겠습니까?(Y/N)\n>> ");
            createDecision = getUserInput().trim();
            if(createDecision.equals("Y")) {
                List<Contact> userTable = contactRepository.findAll();
                Collections.sort(userTable, (c1, c2) -> c1.getGroupName().compareTo(c2.getGroupName()));
                int integerInfo = 1;
                contactRepository.getSequencedUserTable().clear();
                for(Contact contact : userTable) {
                    contactRepository.getSequencedUserTable().add(contact);
                }
                break;
            }
            else if(createDecision.equals("N")) {
                return 0;
            }
            else {
                System.out.println("잘못된 입력 형식입니다.");
                continue;
            }
        }
        return 1;
    }

    public int sortByRecentStored(ContactRepository contactRepository) {
        String createDecision;
        while(true) {
            System.out.print("최근 저장순으로 정렬하시겠습니까?(Y/N)\n>> ");
            createDecision = getUserInput().trim();
            if(createDecision.equals("Y")) {
                List<Contact> userTable = contactRepository.findAll();
                Collections.sort(userTable, (c1, c2) -> Integer.compare(c1.getPid(), c2.getPid()));
                contactRepository.getSequencedUserTable().clear();
                for(Contact contact : userTable) {
                    contactRepository.getSequencedUserTable().add(contact);
                }
                break;
            }
            else if(createDecision.equals("N")) {
                return 0;
            }
            else {
                System.out.println("잘못된 입력 형식입니다.");
                continue;
            }
        }
        return 1;
    }
    public int selectIndex(){
        String userInput=null;
        int index=0;
        try {
            System.out.print("인덱스 선택\n>> ");
            userInput = getUserInput().trim();
            index = Integer.parseInt(userInput);
        } catch(NumberFormatException e) {
            index = -1;
        }
        return index;
    }

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }

    public String printContact(Contact contact) {
        String sout;
        String none = "";
        if (!contact.getGroupName().equals("X")) {
            none = contact.getGroupName();
        }
        sout = "이름: " + contact.getName() + "\n";
        sout += "전화번호: " + contact.getPhoneNumber().toString() + "\n";
        sout += "그룹: " + none + "\n";
        sout += "메모: " + contact.getMemo() + "\n";
        return sout;
    }
}