package revoblog.repository;

import org.springframework.data.repository.CrudRepository;
import revoblog.domain.person;

import java.util.Optional;

/**
 * Created by ashraf on 8/2/15.
 */
public interface userRepository extends CrudRepository<person,Long> {
   public Optional<person>  findByEmail(String email);
}
