package revoblog.service;

import org.springframework.web.multipart.MultipartFile;
import revoblog.domain.part;
import revoblog.domain.post;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by ashraf on 8/8/2015.
 */
public interface FileServices {
    public String CloneService(post post);

    public Set<part> FilesRecursive(post post);

    public void FilesRecursiveSaving(post post, Set<part> neededParts);

    public void UploadFile(MultipartFile file, String name,  String folder,HttpServletRequest request);

}
