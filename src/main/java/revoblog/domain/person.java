package revoblog.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ashraf on 8/2/15.
 */
@Entity
public class person {
    @Id
    @GeneratedValue
    Long id;
    @Column(length = 30)
    String userName;
    @Column(length = 30)
    String email;
    @Column(length = 60)
    String password;
    String imgPath;
    int role;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "person")
    Set<post> postSet=new HashSet<>();


    public Set<GrantedAuthority> grantedAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        if (role == 0) authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public person setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public person setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public person setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getImgPath() {
        return imgPath;
    }

    public person setImgPath(String imgPath) {
        this.imgPath = imgPath;
        return this;
    }

    public int getRole() {
        return role;
    }

    public person setRole(int role) {
        this.role = role;
        return this;
    }

    public Set<post> getPostSet() {
        return postSet;
    }

    public person setPostSet(Set<post> postSet) {
        this.postSet = postSet;
        return this;
    }
}
