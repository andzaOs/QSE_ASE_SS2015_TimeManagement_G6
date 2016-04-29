package at.tuwien.ase.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.json.JsonRootType;
@Entity
@JsonRootType
public class WorkingObject {
	@Id
	@GeneratedValue
	Long id;
	@Column
	String description;
	@Column
	String  woNumber;
	
	@ManyToMany(mappedBy="workingObjectList")
	List<ProjectType> projectTypeList;
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

	public List<ProjectType> getProjectTypeList() {
		return projectTypeList;
	}

	public void setProjectTypeList(List<ProjectType> projectTypeList) {
		this.projectTypeList = projectTypeList;
	}
	
	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof WorkingObject) {
			WorkingObject w = (WorkingObject) obj;
			if( w.id==null){
				return false;
			}
			
			return w.id.equals(id);
		}
		return false;
	}
}
