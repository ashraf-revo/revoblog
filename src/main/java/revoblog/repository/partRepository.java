package revoblog.repository;

import org.springframework.data.repository.CrudRepository;
import revoblog.domain.part;

import java.util.List;
import java.util.Set;

/**
 * Created by ashraf on 8/3/2015.
 */
public interface partRepository extends CrudRepository<part,Long> {
    public List<part> findByPost_IdAndPost_PublishOrderByOrderNumberDesc(Long id,boolean publish);
    public Set<part> findByPost_IdOrderByOrderNumberDesc(Long id);

}
