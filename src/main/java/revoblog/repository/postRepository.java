package revoblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import revoblog.domain.post;


import java.util.List;
import java.util.Optional;

/**
 * Created by ashraf on 8/3/2015.
 */
public interface postRepository extends CrudRepository<post, Long> {
    public Optional<post> findByTitle(String title);

    public Optional<post> findById(Long id);

    public List<post> findByPublish(boolean publish, Pageable pageable);

    public List<post> findAll(Pageable pageable);

    public int countByPublish(boolean b);
}
