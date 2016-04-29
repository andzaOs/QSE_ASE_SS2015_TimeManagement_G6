package at.tuwien.ase.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.json.JsonRootType;
@JsonRootType
@Entity
public class TaskReport {
	@Id
	@GeneratedValue
	Long id;
	@Enumerated
	TaskReportStatus status;
	@Column
	String description;
	@Column
	Date begin;
	@Column
	Date end;
	@OneToMany(mappedBy="taskReport")
	List<ResourceUsage> resourceUsageList;	
	@Column
	String rejectMessage;
	@Column
	Boolean deleted;
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getRejectMessage() {
		return rejectMessage;
	}
	
	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}
	
	@ManyToOne
	Task task;

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


	public Date getBegin() {
		return begin;
	}


	public void setBegin(Date begin) {
		this.begin = begin;
	}


	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
		this.end = end;
	}


	public List<ResourceUsage> getResourceUsageList() {
		return resourceUsageList;
	}


	public void setResourceUsageList(List<ResourceUsage> resourceUsageList) {
		this.resourceUsageList = resourceUsageList;
	}


	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof TaskReport) {
			TaskReport w = (TaskReport) obj;
			if( w.id==null){
				return false;
			}
			
			return w.id.equals(id);
		}
		return false;
	}

	public TaskReportStatus getStatus() {
		return status;
	}

	public void setStatus(TaskReportStatus status) {
		this.status = status;
	}
	
	
}
