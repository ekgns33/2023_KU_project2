import utils.ContactMapper;
import utils.FileHandler;

public class ProjectManagerSupport {

    public ProjectManagerSupport(){
        this.fileHandler = new FileHandler();
        this.contactMapper = new ContactMapper();
    }
    FileHandler fileHandler;
    ContactMapper contactMapper;

}
