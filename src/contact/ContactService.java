package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;
import utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactService {

    public ContactService(){
    }

    public Contact search(int userInput, ContactRepository contactRepository){
        List<Contact> queryResult = searchByInputType(userInput, contactRepository);
        if(queryResult == null || queryResult.isEmpty()){
            System.out.println("일치하는 항목이 없습니다.");
            return null;
        } else {
            showContactList(queryResult);
            Contact selectedContact = selectAndGetContact(queryResult);
            return selectedContact;
        }
    }
    public void showContactList(List<Contact> list) {
        int index = 1;
        for (Contact contact : list) {
            System.out.print("[" + index + "] ");
            System.out.println(contact.toString());
            index++;
        }
    }
    public List<Contact> searchByInputType(int userInput, ContactRepository contactRepository) {
        List<Contact> queryResult = null;
        String inputName, inputPhoneNumber, inputGroup;
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
                    break;
                }
                queryResult = contactRepository.findByName(inputName);
                break;
            case 2:
                inputPhoneNumber = getUserInput();
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case 3:
                inputGroup = getUserInput();
                queryResult = contactRepository.findByGroupName(inputGroup);
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
                System.out.print("저장 확인(Y,N) : ");
                String createDecision = getUserInput();
                if(createDecision.equals("Y")) {
                    contactRepository.save(contact);
                    break;
                }
                else if(createDecision.equals("N")){
                    return null;
                }
                else{
                    System.out.println("다시 입력해주세요.");
                }
            }
            return contact;
        }
    }

    public Contact createContactInfo(ContactRepository contactRepository){
        System.out.println("정보 입력");
        // 이름 입력 -> 에러 처리 구현 X
        System.out.print("이름>> ");
        String userNameInput = getUserInput();
        // 전화번호 입력 -> 에러 처리 구현 X

        PhoneNumber phoneNumber = new PhoneNumber();
        while(true) {
            System.out.print("전화번호>> ");
            String userPhoneNumberInput = getUserInput();
            if(Validator.isValidPhoneNumberFormat(userPhoneNumberInput) == -1) {
                System.out.println("잘못된 전화번호 형식입니다.");
                continue;
            }
            if(userPhoneNumberInput.matches(Validator.PHONENUM)) {
                //무선
                // set에 있는 애들이랑 비교해야되고
                if(contactRepository.isNumberUnique(userPhoneNumberInput)) {
                    System.out.println("이미 존재하는 휴대폰 번호입니다.");
                    continue;
                }
                phoneNumber.insertPhoneNumber(userPhoneNumberInput);
            }
            else {
                //유선
                // 자기 자신이랑만 비교하면 되고
            }

            System.out.print("더 이상 추가를 원하지 않으시면 Y를 입력해주세요>>");
            userPhoneNumberInput = getUserInput();
            if(userPhoneNumberInput.equals("Y")) break;
        }
        // 그룹 입력 -> 에러 처리 구현 X
        System.out.print("그룹>> ");
        String userGroupInput = getUserInput();
        // 메모 입력
        System.out.print("메모>> ");
        String userMemoInput = getUserInput();
        return new Contact(userNameInput, phoneNumber, userGroupInput, userMemoInput);
    }
    public void update(int userInput, ContactRepository contactRepository) {
        // 검색기능 그대로 사용한다.
        List<Contact> queryResult = searchByInputType(userInput, contactRepository);
        if (queryResult.isEmpty()) {
            System.out.println("일치하는 항목이 없습니다.");
        } else {
            showContactList(queryResult);
            int targetIndex = selectIndex();
            if (targetIndex == 0) return; // detail을 안보겠다는 의미 -> 수정을 할 수가 없음 그냥 리턴한다.

            Contact selectedContact = queryResult.get(targetIndex - 1); // 0-based index;
            System.out.print("수정하시겠습니까?(Y/N)\n>> ");
            String updateDecision = getUserInput();
            if (updateDecision.equals("Y")) {
                Contact updateContact = new Contact(selectedContact.getPid());
                while(true) {
                    try {
                        System.out.print("이름을 입력해주세요 (" + selectedContact.getName() + ") :");
                        String inputName = getUserInput();
                        //validate
                        if(inputName.equals("")) {
                            updateContact.setName(selectedContact.getName());
                            break;
                        }
                        updateContact.setName(inputName);
                        break;
                    } catch (ApplicationException e){
                        System.out.print("다시 입력해주세요 : ");
                    }
                }
                while(true) {
                    try {
                        System.out.print("그룹명을 입력해주세요 (" + selectedContact.getGroupName() + ") :");
                        String inputGroupName = getUserInput();
                        //validate
                        if(inputGroupName.equals("")) {
                            updateContact.setGroupName(selectedContact.getGroupName());
                            break;
                        }
                        updateContact.setGroupName(inputGroupName);
                        break;
                    } catch (ApplicationException e){
                        System.out.print("다시 입력해주세요 : ");
                    }
                }

                System.out.print("메모를 입력해주세요 (" + selectedContact.getMemo() + ") : ");
                String inputMemo = getUserInput();
                updateContact.setMemo(inputMemo);
                System.out.println("==========");
                System.out.println(updateContact.toString());
                System.out.println("==========");

                System.out.print("저장하시겠습니까? (Y or N) : ");
                String saveDecision = getUserInput();
                if(saveDecision.equals("Y")) {
                    contactRepository.updateContact(updateContact);
                } else if (saveDecision.equals("N")){
                    System.out.println("수정작업을 취소합니다.");
                } else {
                    System.out.println("부적절한 입력으로 수정내용 저장이 취소되었습니다.");
                    return;
                }

            } else  {
                return;
            }
        }
    }

    public void groupManagement(int userInput, ContactRepository contactRepository){
        String resultValue; // 각 groupManage 함수 내 리턴값을 담기 위한 변수
        switch (userInput) {
            case 1:
                resultValue = groupAdd(contactRepository);
                if(resultValue != null) {
                    System.out.println("<"+resultValue+"> 그룹 추가");
                }
                System.out.println(contactRepository.getGroupTable());
                break;
            case 2:
                resultValue = groupDelete(contactRepository);
                if(resultValue != null){
                    System.out.println("<"+resultValue+"> 그룹 삭제");
                }
                break;
            case 3:
                resultValue = groupModify(contactRepository);
                if(resultValue != null){
                    System.out.println("<"+resultValue+"> 그룹으로 수정");
                }
                break;
            default:
                break;
        }
    }
    public String groupAdd(ContactRepository contactRepository){
        String inputGroupName;
        String createDecision;
        while(true) {
            System.out.print("추가할 그룹명을 입력하세요 : ");
            inputGroupName = getUserInput();
            if(inputGroupName.equals("0")){
                return null;
            }
            if (Validator.isValidGroupNameFormat(inputGroupName) != 1) {
                System.out.println("잘못된 그룹명 형식입니다.");
            }
            else {
                if (contactRepository.isGroupNameUnique(inputGroupName)) {
                    System.out.println("이미 존재하는 그룹명입니다.");
                } else {
                    while(true) {
                        System.out.print("저장하시겠습니까? (Y or N) : ");
                        createDecision = getUserInput();
                        if (createDecision.equals("Y")) {
                            ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                            groupCurrent.add(inputGroupName);
                            contactRepository.setGroupTable(groupCurrent);
                            break;
                        } else if (createDecision.equals("N")) {
                            return null;
                        } else {
                            System.out.println("다시 입력해주세요.");
                        }
                    }
                    break;
                }
            }
        }
        return inputGroupName;
    }

    public String groupDelete(ContactRepository contactRepository){
        String inputGroupName;
        String createDecision;
        if(contactRepository.getGroupTable().isEmpty()){
            System.out.println("존재하는 그룹이 없습니다!");
            return null;
        }
        while(true) {
            System.out.print("삭제할 그룹명을 입력하세요 : ");
            inputGroupName = getUserInput();
            if(inputGroupName.equals("0")){
                return null;
            }
            if (!contactRepository.isGroupNameUnique(inputGroupName)) {
                System.out.println("존재하지 않는 그룹명입니다.");
            }
            else {
                while(true) {
                    System.out.print("저장하시겠습니까? (Y or N) : ");
                    createDecision = getUserInput();
                    if (createDecision.equals("Y")) {
                        List<Contact> queryCurrent = contactRepository.findAll();
                        for (Contact contact : queryCurrent) {
                            if (contact.getGroupName().equals(inputGroupName)) {
                                contact.setGroupName("");
                            }
                        }
                        ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                        groupCurrent.remove(inputGroupName);
                        contactRepository.setGroupTable(groupCurrent);
                        break;
                    }
                    else if (createDecision.equals("N")) {
                        return null;
                    }
                    else {
                        System.out.println("다시 입력해주세요.");
                    }
                }
                break;
            }
        }
        return inputGroupName;
    }/**/

    public String groupModify(ContactRepository contactRepository){
        String inputGroupName;
        String modifiedGroupName;
        String createDecision;
        List<Contact> queryCurrent = contactRepository.findAll();
        if(contactRepository.getGroupTable().isEmpty()){
            System.out.println("존재하는 그룹이 없습니다!");
            return null;
        }
        while(true){
            System.out.print("수정할 그룹명을 입력하세요 : ");
            inputGroupName = getUserInput();
            if(inputGroupName.equals("0")){
                return null;
            }
            if(!contactRepository.isGroupNameUnique(inputGroupName)){
                System.out.println("존재하지 않는 그룹명입니다.");
            }
            else{
                System.out.print("새로운 그룹명을 입력하세요 : ");
                modifiedGroupName = getUserInput();
                if(modifiedGroupName.equals("0")){
                    return null;
                }
                if(contactRepository.isGroupNameUnique(modifiedGroupName)){
                    System.out.println("이미 존재하는 그룹명입니다.");
                }
                else{
                    if(Validator.isValidGroupNameFormat(modifiedGroupName)==1){
                        while(true) {
                            System.out.print("저장하시겠습니까? (Y or N) : ");
                            createDecision = getUserInput();
                            if (createDecision.equals("Y")) {
                                for(Contact contact : queryCurrent){
                                    if(contact.getGroupName().equals(inputGroupName)){
                                        contact.setGroupName(modifiedGroupName);
                                    }
                                }
                                ArrayList<String> groupCurrent = contactRepository.getGroupTable();
                                for(int i=0;i<groupCurrent.size();i++){
                                    String currentGroupName = groupCurrent.get(i);
                                    if (currentGroupName.equals(inputGroupName)) {
                                        groupCurrent.set(i, modifiedGroupName);
                                    }
                                }
                                contactRepository.setGroupTable(groupCurrent);
                                break;
                            } else if (createDecision.equals("N")) {
                                return null;
                            } else {
                                System.out.println("다시 입력해주세요.");
                            }
                        }
                        break;
                    }
                    else{
                        System.out.println("잘못된 형식의 이름입니다.");
                    }
                }
            }
        }
        System.out.print("기존 : "+inputGroupName+" -> ");
        return modifiedGroupName;
    }
    public int selectIndex(){
        System.out.print("인덱스 선택 : ");
        String userInput = getUserInput();
        return Integer.parseInt(userInput);
    }

    public String getUserInput() {
        Scanner scan = new Scanner(System.in);
        String userInput;
        userInput = scan.nextLine();
        return userInput;
    }
}