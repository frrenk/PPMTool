package pl.piaseckif.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.piaseckif.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
}
