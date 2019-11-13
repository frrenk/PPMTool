package pl.piaseckif.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piaseckif.ppmtool.domain.Backlog;
import pl.piaseckif.ppmtool.domain.Project;
import pl.piaseckif.ppmtool.domain.ProjectTask;
import pl.piaseckif.ppmtool.exceptions.ProjectIdException;
import pl.piaseckif.ppmtool.exceptions.ProjectNotFoundException;
import pl.piaseckif.ppmtool.repositories.BacklogRepository;
import pl.piaseckif.ppmtool.repositories.ProjectRepository;
import pl.piaseckif.ppmtool.repositories.ProjectTaskRepository;

import java.util.List;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

            Backlog backlog = getBacklogWithUserValidation(projectIdentifier, username);
            projectTask.setBacklog(backlog);

            Integer backlogSequence = backlog.getPTSequence();
            backlog.setPTSequence(++backlogSequence);
            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority()==null||projectTask.getPriority()==0) {
                projectTask.setPriority(3);
            }
            if (projectTask.getStatus()==null||projectTask.getStatus().equals("")) {
                projectTask.setStatus(("TO_DO"));
            }
            return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String backlogId, String username) {
        Project project = projectService.findProjectByIdentifier(backlogId, username);

        if (project==null) {
            throw new ProjectNotFoundException("Project Id "+backlogId+" not found");
        } else {
            return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
        }
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlogId, String projectSequence, String username) {
        return validateProjectTask(backlogId, projectSequence, username);
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String projectSequence, String username){
        validateProjectTask(backlogId, projectSequence, username);
        return projectTaskRepository.save(updatedTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlogId, String projectSequence, String username) {
        projectTaskRepository.delete(validateProjectTask(backlogId, projectSequence, username));
    }

    public ProjectTask validateProjectTask(String backlogId, String projectSequence, String username) {
        Backlog backlog = getBacklogWithUserValidation(backlogId, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
        if (backlog == null) {
            throw new ProjectNotFoundException("Project Id "+backlogId+" not found");
        } else if (projectTask==null) {
            throw new ProjectNotFoundException("Projec Task Id "+projectSequence+" not found");
        } else if (!backlogId.toUpperCase().equals(projectTask.getProjectIdentifier().toUpperCase())) {
            throw new ProjectNotFoundException("Project Task doesn't belong to this project");
        } else {
            return projectTask;
        }
    }

    public Backlog getBacklogWithUserValidation(String projectIdentifier, String username) {
        return projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
    }
}
