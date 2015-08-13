package revoblog.service.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import revoblog.domain.part;
import revoblog.domain.post;
import revoblog.repository.partRepository;
import revoblog.service.FileServices;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ashraf on 8/8/2015.
 */
@Service
@Transactional
public class FileServicesImpl implements FileServices {
    @Autowired
    partRepository partRepository;
    @Override
    public String CloneService(post post) {
        File localPath = null;
        try {
            localPath = File.createTempFile("TestGitRepository", "");
            localPath.delete();
            post.setPath(localPath.getPath());
            Git.cloneRepository()
                    .setURI(post.getUrl())
                    .setDirectory(localPath)
                    .call();
            return post.getPath();
        } catch (GitAPIException | IOException e) {
        }
        return "";
    }

    @Override
    public Set<part> FilesRecursive(post post) {
        try {
            if (post.getPath().trim().equals("")) return new HashSet<part>();
            return Files.walk(Paths.get(post.getPath()))
                    .filter(Files::isRegularFile)
                    .map(path -> new part().setName(path.toString().replace(post.getPath(),""))
                    .setPath(path.toString())).collect(Collectors.toSet());
        } catch (IOException e) {
            return new HashSet<part>();
        }

    }

    @Override
    public void FilesRecursiveSaving(post post, Set<part> neededParts) {
        try {
            Files.walk(Paths.get(post.getPath()))
                    .filter(Files::isRegularFile)
                    .map(path -> new part().
                            setName(path.toString().replace(post.getPath(),""))
                            .setPath(path.toString()))
                    .filter(part -> neededParts.
                            stream().
                            anyMatch(part1 -> part.getName().
                                    equals(part1.getName())))
                    .map(part2 -> {
                        String collect = "";
                        try {

                            collect = Files.lines(
                                    Paths.get(part2.getPath(), "")
                            ).collect(Collectors.joining("\n"));
                        } catch (IOException e) {
                        }
                        return part2.setContent(collect);
                    }).map(p -> p.setPost(post)

            ).forEach(pa3 -> partRepository.save(pa3) );

        } catch (IOException e) {
        }


    }

    public void UploadFile(MultipartFile file, String name, String folder, HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {
                String sp = File.separator;
                String pathname = request.getRealPath("")
                        + "WEB-INF" + sp +
                        "classes" + sp + "static" +
                        sp + folder + sp + name;
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File(pathname)));
                stream.write(file.getBytes());
                stream.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

    }

}
