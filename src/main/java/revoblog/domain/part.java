package revoblog.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by ashraf on 8/3/2015.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class part {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    post post;
    String path;
    String name;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String content;
    Long orderNumber=new Long(0);


    public Long getId() {
        return id;
    }

    public part setId(Long id) {
        this.id = id;
        return this;
    }

    public revoblog.domain.post getPost() {
        return post;
    }

    public part setPost(revoblog.domain.post post) {
        this.post = post;
        return this;
    }

    public String getPath() {
        return path;
    }

    public part setPath(String path) {
        this.path = path;
        return this;
    }

    public String getContent() {
        return content;
    }

    public part setContent(String content) {
        this.content = content;
        return this;
    }

    public String getName() {
        return name;
    }

    public part setName(String name) {
        this.name = name;
        return this;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public part setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    @Override
    public String toString() {
        return name+"  "+id+"  "+content+"  "+path;
    }
}
