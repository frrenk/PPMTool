package pl.piaseckif.ppmtool.domain;

import javax.persistence.*;

@Entity
public class Backlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer PTSequence = 0;
    private String projectIdentifier;

    @OneToOne
    private Project project;

    @OneToMany
    private ProjectTask projectTask;

    public Backlog() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPTSequence() {
        return PTSequence;
    }

    public void setPTSequence(Integer PTSequence) {
        this.PTSequence = PTSequence;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectTask getProjectTask() {
        return projectTask;
    }

    public void setProjectTask(ProjectTask projectTask) {
        this.projectTask = projectTask;
    }

    @Override
    public String toString() {
        return "Backlog{" +
                "id=" + id +
                ", PTSequence=" + PTSequence +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", project=" + project +
                ", projectTask=" + projectTask +
                '}';
    }
}
