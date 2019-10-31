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

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProjectNotFoundException("Project Id "+projectIdentifier+" not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String backlogId) {
        Project project = projectRepository.findByProjectIdentifier(backlogId);

        if (project==null) {
            throw new ProjectNotFoundException("Project Id "+backlogId+" not found");
        } else {
            return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
        }
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlogId, String projectSequence) {
        return validateProjectTask(backlogId, projectSequence);
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String projectSequence){
        validateProjectTask(backlogId, projectSequence);
        return projectTaskRepository.save(updatedTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlogId, String projectSequence) {
        projectTaskRepository.delete(validateProjectTask(backlogId, projectSequence));
    }

    public ProjectTask validateProjectTask(String backlogId, String projectSequence) {
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
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
}
