package pl.piaseckif.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piaseckif.ppmtool.domain.Project;
import pl.piaseckif.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    public Project saveOrUpdateProject(Project project) {

        //Logic

        return projectRepository.save(project);
    }
}
