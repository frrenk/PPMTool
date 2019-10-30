package pl.piaseckif.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piaseckif.ppmtool.domain.Backlog;
import pl.piaseckif.ppmtool.domain.ProjectTask;
import pl.piaseckif.ppmtool.exceptions.ProjectIdException;
import pl.piaseckif.ppmtool.exceptions.ProjectNotFoundException;
import pl.piaseckif.ppmtool.repositories.BacklogRepository;
import pl.piaseckif.ppmtool.repositories.ProjectTaskRepository;

import java.util.List;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
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
        Iterable<ProjectTask> projectTasks = projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId)

        if () {
            return projectTasks;
        } else {
        }
    }
}
