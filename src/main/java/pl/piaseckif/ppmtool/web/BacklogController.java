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
                                                     @PathVariable String backlogId) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
        if (errorMap!=null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask);
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<Iterable<ProjectTask>> getProjectBacklog(@PathVariable String backlogId) {
        return new ResponseEntity<Iterable<ProjectTask>>(projectTaskService.findBacklogById(backlogId), HttpStatus.OK);
    }
}
