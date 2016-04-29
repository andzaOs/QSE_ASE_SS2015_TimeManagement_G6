package at.tuwien.ase.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.codehaus.enunciate.json.JsonRootType;
@JsonRootType
@Entity

public class Project {
	@Id
	@GeneratedValue
	Long id;
	@Column
	private
	String name;
	@ManyToMany
	Set<User> userList;
	@Column
	Double expectedWorkHours;

	@Column
	Double workHours;

	@ManyToMany
	Set<TaskType> taskTypeList;
	@ManyToOne
	ProjectType projectType;
	
	@Column
	String description;
	@Column
	Boolean deleted;
	@Column
	Date begin;
	@Column
	Date end;
	public Date getBegin() {
		return begin;
	}
	
	public Date getEnd() {
		return end;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getExpectedWorkHours() {
		return expectedWorkHours;
	}
	public void setExpectedWorkHours(Double expectedWorkHours) {
		this.expectedWorkHours = expectedWorkHours;
	}
	public Double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
	
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	public Set<TaskType> getTaskTypeList() {
		return taskTypeList;
	}
	public void setTaskTypeList(Set<TaskType> taskTypeList) {
		this.taskTypeList = taskTypeList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof Project) {
			Project w = (Project) obj;
			if( w.id==null){
				return false;
			}
			return w.id.equals(id);
		}
		return false;
	}

	public Set<User> getUserList() {
		return userList;
	}
	public void setUserList(Set<User> userList) {
		this.userList = userList;
	}
	
	
}
