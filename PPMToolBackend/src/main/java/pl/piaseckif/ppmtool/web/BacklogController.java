package pl.piaseckif.ppmtool.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.piaseckif.ppmtool.domain.ProjectTask;
import pl.piaseckif.ppmtool.services.MapValidationErrorService;
import pl.piaseckif.ppmtool.services.ProjectTaskService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    private ProjectTaskService projectTaskService;
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    public BacklogController(ProjectTaskService projectTaskService, MapValidationErrorService mapValidationErrorService) {
        this.projectTaskService = projectTaskService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult bindingResult,
                                                     @PathVariable String backlogId, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
        if (errorMap!=null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<Iterable<ProjectTask>> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return new ResponseEntity<Iterable<ProjectTask>>(projectTaskService.findBacklogById(backlogId, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{projectTaskId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findProjectTaskByProjectSequence(backlogId, projectTaskId, principal.getName()), HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id, principal.getName());

        return new ResponseEntity<ProjectTask>(updatedTask,HttpStatus.OK);

    }

    @DeleteMapping("/{backlogId}/{projectTaskId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId, Principal principal) {
        projectTaskService.deleteProjectTaskByProjectSequence(backlogId, projectTaskId, principal.getName());

        return new ResponseEntity<String>("Project task deleted", HttpStatus.OK);
    }
}
