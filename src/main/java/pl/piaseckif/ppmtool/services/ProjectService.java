package pl.piaseckif.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piaseckif.ppmtool.domain.Backlog;
import pl.piaseckif.ppmtool.domain.Project;
import pl.piaseckif.ppmtool.domain.User;
import pl.piaseckif.ppmtool.exceptions.ProjectIdException;
import pl.piaseckif.ppmtool.repositories.BacklogRepository;
import pl.piaseckif.ppmtool.repositories.ProjectRepository;
import pl.piaseckif.ppmtool.repositories.ProjectTaskRepository;
import pl.piaseckif.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.userRepository = userRepository;
    }




    public Project saveOrUpdateProject(Project project, String username) {

        String projectIdentifier = project.getProjectIdentifier().toUpperCase();

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(projectIdentifier);
            if (project.getId()==null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project Id "+projectIdentifier+" already exists!");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project==null) {
            throw new ProjectIdException("Project with identifier \'"+ projectIdentifier.toUpperCase()+ "\' not found");
        } else {
            return project;
        }

    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project==null) {
            throw new ProjectIdException("Couldn't delete; Project with identifier \'"+ projectIdentifier.toUpperCase()+ "\' not found");
        } else {
           projectRepository.delete(project);
        }

    }
}
