package revoblog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ashraf on 8/3/2015.
 */
@Entity
public class post {
    @Id
    @GeneratedValue
    Long id;
    @Column(length = 30, unique = true)
    @NotEmpty
    String title;
    @Column(length = 100)
    @URL
    String url;
    @Column(length = 100, unique = true)
    String path;
    @ManyToOne
    @JoinColumn
    @JsonBackReference
    person person;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    @JsonBackReference
    Set<part> parts = new HashSet<>();
    String postImg = "default.jpg";
    @Temporal(TemporalType.DATE)
    Date date = new Date();
    boolean publish;
    @Column(length = 120)
    String description;
    @Transient
    @JsonIgnore
    MultipartFile file;

    public Long getId() {
        return id;
    }

    public post setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public post setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPath() {
        return path;
    }

    public post setPath(String path) {
        this.path = path;
        return this;
    }

    public person getPerson() {
        return person;
    }

    public post setPerson(person person) {
        this.person = person;
        return this;
    }

    public Set<part> getParts() {
        return parts;
    }

    public post setParts(Set<part> parts) {
        this.parts = parts;
        return this;
    }

    public String getPostImg() {
        return postImg;
    }

    public post setPostImg(String postImg) {
        this.postImg = postImg;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public post setDate(Date date) {
        this.date = date;
        return this;
    }

    public boolean isPublish() {
        return publish;
    }

    public post setPublish(boolean publish) {
        this.publish = publish;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public post setDescription(String description) {
        this.description = description;
        return this;
    }

    public MultipartFile getFile() {
        return file;
    }

    public post setFile(MultipartFile file) {
        if (!file.isEmpty()) setPostImg(UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename());
        this.file = file;
        return this;
    }
}
