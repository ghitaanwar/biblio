package controler.util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;
import java.io.File;

public class UploadUtil {


public static String rootPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")+"resources/upload/";
public static String extPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/resources/upload/";
    public static void upload(UploadedFile uploadedFile, String nameOfUploadeFile) {
        System.out.println("controler.util.UploadUtil.upload():"+rootPath);
        try {
            String fileSavePath = rootPath + nameOfUploadeFile;
            InputStream fileContent = uploadedFile.getInputstream();
            Path path = new File(fileSavePath).toPath();
            System.out.println(path);
            Files.copy(fileContent, path , StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.out.println("controler.util.UploadUtil.upload()"+e.getLocalizedMessage());
            JsfUtil.addErrorMessage(e, "Erreur Upload image");

        }

    }
    
    public static void delete(String nameOfUploadeFile) {
        try {
            String path = rootPath + "/" + nameOfUploadeFile;
            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erreur delete image");

        }

    }
    
    
    
    public static String ext(String filename) {
        
        if(filename==null) return null;
        
        String[] list = filename.split(".");
        
        if(list.length<2) return null;
        System.out.println("controler.util.UploadUtil.ext()"+list[list.length-1]);
        return list[list.length-1];
        
    }
    
}
