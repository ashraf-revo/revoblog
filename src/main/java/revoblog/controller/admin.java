package revoblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import revoblog.domain.part;
import revoblog.domain.post;
import revoblog.repository.partRepository;
import revoblog.repository.postRepository;
import revoblog.repository.userRepository;
import revoblog.service.FileServices;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ashraf on 8/3/2015.
 */
@Controller
@RequestMapping("/admin")
public class admin {
    @Autowired
    postRepository postRepository;
    @Autowired
    userRepository userRepository;
    @Autowired
    partRepository partRepository;
    @Autowired
    FileServices fileServices;
    @RequestMapping(value = {"","/"})
    public String index() {
        return "redirect:/admin/index.html";
    }

    @RequestMapping(value = "/CloneRemote", method = RequestMethod.POST)
    @ResponseBody
    public Long CloneRemoteRepository(Principal principal, @ModelAttribute post post, BindingResult bindingResult, HttpServletRequest request) {

        if (!bindingResult.hasErrors()) {
            return userRepository.findByEmail(principal.getName()).map(user -> {
                        post.setPerson(user);
                        if (!"".equals(post.getUrl().trim())) {
                            post.setPath(fileServices.CloneService(post));
                        }
                        fileServices.UploadFile(post.getFile(), post.getPostImg(), "images", request);
                        return postRepository.save(post).getId();
                    }
            ).orElseGet(() -> new Long(-1));
        }
        return new Long(-1);

    }


    @RequestMapping(value = "parts/displayAll/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Set<part> parts(@PathVariable("id") Long id) {
        return postRepository.findById(id).map(post ->
                        fileServices.FilesRecursive(post)
        ).orElseGet(() -> new HashSet<>());
    }

    @ResponseBody
    @RequestMapping(value = "/post/view")
    public List<post> viewpost(@RequestParam("page") Optional<Integer> page) {
        if (page.isPresent()) {
            return postRepository.findAll(new PageRequest(page.get() - 1, 4));
        }
        return postRepository.findAll(new PageRequest(0, 4));
    }


    @ResponseBody
    @RequestMapping(value = "/post/view/{id}")
    public Set<part> viewposts(@PathVariable("id") Long id) {
        Set<part> numberDesc = partRepository.findByPost_IdOrderByOrderNumberDesc(id);
     if (numberDesc.isEmpty()) {
            numberDesc.add(new part().setPost(postRepository.findOne(id)));
        }
        return numberDesc;
    }




    public void order_part(List<part> neededParts) {
        long maxValue = Long.MAX_VALUE;
        for (part part : neededParts) {
            partRepository.save(partRepository.findOne(part.getId()).setOrderNumber(--maxValue));
        }
    }


    @ResponseBody
    @RequestMapping(value = "/post/saveingParts/{id}", method = RequestMethod.POST)
    public void saveingParts(@PathVariable Long id, @RequestParam String[] parts) {
        Set<part> dbHave = viewposts(id);
        List<String> needed = Arrays.asList(parts);
        Set<String> stringStream = needed.stream().filter(s ->
                !dbHave.stream().anyMatch(part ->
                        {
                            if (part == null || part.getName() == null) return false;
                            return part.getName().equals(s);
                        }
                )).collect(Collectors.toSet());
        postRepository.findById(new Long(id)).ifPresent(one ->

                        fileServices.FilesRecursiveSaving(one, stringStream.stream().map(s ->
                                        new part().setName(s).setPost(one))
                                        .collect(Collectors.toSet()))

        );

        Set<part> afterdbHave = viewposts(id);


         List<part> sorted = needed.stream().map(
                 s -> afterdbHave.stream()
                         .filter(part -> part.getName().equals(s))
                         .findAny().get()).unordered()
                 .collect(Collectors.toList());

        order_part(sorted);
    }

    @ResponseBody
    @RequestMapping(value = "/post/edit/{id}", method = RequestMethod.POST)
    public void editposts(@PathVariable Long id, @ModelAttribute post post, HttpServletRequest request) {
        post one = postRepository.findOne(id);
        if (one != null) {
            post publish = one.
                    setDescription(post.getDescription())
                    .setPublish(post.isPublish())
                    .setPostImg(post.getPostImg());
            fileServices.UploadFile(post.getFile(), post.getPostImg(), "images", request);
            postRepository.save(publish);
        }
    }


    @ResponseBody
    @RequestMapping(value = "clone/{id}", method = RequestMethod.POST)
    public void clone(@PathVariable Long id, @ModelAttribute post post) {
        post publish = postRepository.findOne(id).setUrl(post.getUrl());
        if (!"".equals(post.getUrl().trim())) {
            publish.setPath(fileServices.CloneService(post));
            postRepository.save(publish);
        }


    }



    @ResponseBody
    @RequestMapping(value = "/post/count")
    public long postCount() {
        return postRepository.count();
    }

    @ResponseBody
    @RequestMapping(value = "/post/delete/{id}")
    public boolean postdelete(@PathVariable Long id) {
        post one = postRepository.findOne(id);
        if (one != null) {
            postRepository
                    .delete(one.getId());
            return true;
        }
        return false;
    }
}
