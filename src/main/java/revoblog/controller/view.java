package revoblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import revoblog.domain.part;
import revoblog.domain.post;
import revoblog.repository.partRepository;
import revoblog.repository.postRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ashraf on 8/3/2015.
 */
@Controller
public class view {
    @Autowired
    postRepository postRepository;
    @Autowired
    partRepository partRepository;

    @ResponseBody
    @RequestMapping(value = "/post/view")
    public List<post> viewpost(@RequestParam("page") Optional<Integer> page) {

        List<post> byPublish;
        if (page.isPresent()) {
            byPublish = postRepository.findByPublish(true, new PageRequest(page.get() - 1, 4));
        } else {
            byPublish = postRepository.findByPublish(true, new PageRequest(0, 4));
        }

        return byPublish.stream().map(post ->
                new post().setId(post.getId()).setTitle(post.getTitle())
                        .setPostImg(post.getPostImg()
                        ).setDescription(post.getDescription()).setDate(post.getDate())).
                collect(Collectors.toList());

    }

    @ResponseBody
    @RequestMapping(value = "/post/view/{id}")
    public List<part> viewposts(@PathVariable("id") Long id) {

        List<part> orderNumberDesc = partRepository.findByPost_IdAndPost_PublishOrderByOrderNumberDesc(id, true);
        if (orderNumberDesc.isEmpty()) {
            orderNumberDesc.add(new part().setPost(postRepository.findOne(id)));
        }

        orderNumberDesc = orderNumberDesc.stream().map(part -> new part()
                .setName(part.getName()).setId(part.getId())
                .setContent(part.getContent())).collect(Collectors.toList());

        if (orderNumberDesc.isEmpty()) {
            orderNumberDesc.add(new part().setPost(postRepository.findOne(id)));
        } else {
            orderNumberDesc.get(0).setPost(postRepository.findOne(id));
        }

        return orderNumberDesc;
    }

    @ResponseBody
    @RequestMapping(value = "/post/count")
    public long postCount() {
        return postRepository.countByPublish(true);
    }


}
