package at.tuwien.ase.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.json.JsonRootType;
@JsonRootType
@Entity
public class ProjectType {
	@Id
	@GeneratedValue
	Long id;
	@Column
	String description;

	@ManyToMany
	List<WorkingObject> workingObjectList;
	@Column
	Boolean deleted;
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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

	public List<WorkingObject> getWorkingObjectList() {
		return workingObjectList;
	}

	public void setWorkingObjectList(List<WorkingObject> workingObjectList) {
		this.workingObjectList = workingObjectList;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof ProjectType) {
			ProjectType w = (ProjectType) obj;
			if( w.id==null){
				return false;
			}
			return w.id.equals(id);
		}
		return false;
	}
}
