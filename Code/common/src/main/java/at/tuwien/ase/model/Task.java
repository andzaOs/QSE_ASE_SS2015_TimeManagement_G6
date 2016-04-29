package at.tuwien.ase.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.json.JsonRootType;
@JsonRootType
@Entity
public class Task {

	@Column
	String description;

	@Id
	@GeneratedValue
	Long id;//I suggest we use long for id
	
	@Column
	Double expectedWorkHours;
	
	@OneToMany(mappedBy="task")
	List<TaskReport> taskReportList;
	
	@ManyToOne
	User worker;
	
	@OneToOne
	User approver;
	
	@Column
	Boolean requiresResources;
	

	@Column
	Boolean finished;
	
	@ManyToOne
	TaskType taskType;
	
	@ManyToOne
	WorkingObject workingObject;
	
	@OneToOne
	Project project;
	@Column
	Boolean deleted;
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Boolean getRequiresResources() {
		return requiresResources;
	}
	
	public void setRequiresResources(Boolean requiresResources) {
		this.requiresResources = requiresResources;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getExpectedWorkHours() {
		return expectedWorkHours;
	}
	public void setExpectedWorkHours(Double expectedWorkHours) {
		this.expectedWorkHours = expectedWorkHours;
	}
	public List<TaskReport> getTaskReportList() {
		return taskReportList;
	}
	public void setTaskReportList(List<TaskReport> taskReportList) {
		this.taskReportList = taskReportList;
	}
	public User getWorker() {
		return worker;
	}
	public void setWorker(User worker) {
		this.worker = worker;
	}
	public User getApprover() {
		return approver;
	}
	public void setApprover(User approver) {
		this.approver = approver;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public WorkingObject getWorkingObject() {
		return workingObject;
	}
	public void setWorkingObject(WorkingObject workingObject) {
		this.workingObject = workingObject;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof Task) {
			Task w = (Task) obj;
			if( w.id==null){
				return false;
			}
			return w.id.equals(id);
		}
		return false;
	}

}
