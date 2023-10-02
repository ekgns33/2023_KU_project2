package contact;

import java.util.Map;

public class ContactService {

    public ContactService(int userInput, Map<Integer, Contact> userTable){
        switch(userInput){
            case 1:
                this.searchService(userTable);
            case 2:
                this.createService();
            case 3:
                this.deleteService();
            case 4:
                this.updateService();
            case 5:
                this.settingService();
            default:
                break;
        }
    }

    public void searchService(Map<Integer, Contact> userInfo){}
    public void createService(){}
    public void deleteService(){}
    public void updateService(){}
    public void settingService(){}
}
