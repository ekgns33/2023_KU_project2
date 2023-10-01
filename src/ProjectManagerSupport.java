import utils.ContactMapper;
import utils.FileHandler;

public class ProjectManagerSupport {

    public ProjectManagerSupport(String path ){
        this.fileHandler = new FileHandler(path);
        this.contactMapper = new ContactMapper();
    }
    FileHandler fileHandler;
    ContactMapper contactMapper;

}
