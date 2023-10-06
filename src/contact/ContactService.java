package contact;

import errors.exceptions.ApplicationException;
import errors.exceptions.ErrorCode;
import errors.exceptions.InvalidInputException;

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
        switch (userInput) {
            case 1:
                String inputName = getUserInput();
                queryResult = contactRepository.findByName(inputName);
                break;
            case 2:
                String inputPhoneNumber = getUserInput();
                queryResult = contactRepository.findByPhoneNumber(inputPhoneNumber);
                break;
            case 3:
                String inputGroup = getUserInput();
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
//                    targetIndex = Integer.parseInt(getUserInput());
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
                targetIndex = selectIndex();
            }
        }
        return null;
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
                        System.out.print("전화번호을 입력해주세요 (" + selectedContact.getPhoneNumber() + ") :");
                        String inputPhoneNumber = getUserInput();
                        //validate
                        if(inputPhoneNumber.equals("")) {
                            updateContact.setPhoneNumber(selectedContact.getPhoneNumber());
                            break;
                        }

                        updateContact.setPhoneNumber(inputPhoneNumber);
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

    public void delete(ContactRepository contactRepository) {
        List<Contact> queryResult = searchByInputType(1, contactRepository);
        if (queryResult.isEmpty()) {
            System.out.println("일치하는 항목이 없습니다.");
        }
        else {
            showContactList(queryResult);
            int targetIndex = selectIndex();
            if (targetIndex == 0) return; // detail을 안보겠다는 의미 -> 수정을 할 수가 없음 그냥 리턴한다.
            Contact deletedContact = queryResult.get(targetIndex - 1);
            System.out.print("삭제하시겠습니까?(Y/N)\n>> ");
            String updateDecision = getUserInput();
            if(updateDecision.equals("Y")) {
                contactRepository.deleteContact(deletedContact);
            }
            else if(updateDecision.equals("N")) {
                System.out.println("삭제 작업을 취소합니다.");
            } else {
                System.out.println("부적절 입력.");
            }
        }
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