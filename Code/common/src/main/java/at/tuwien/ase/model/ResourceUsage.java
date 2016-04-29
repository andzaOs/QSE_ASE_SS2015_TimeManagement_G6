package at.tuwien.ase.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.enunciate.json.JsonRootType;
@JsonRootType
@Entity
public class ResourceUsage {
	@Id
	@GeneratedValue
	Long id;
	@Column
	Double cost;
	@Column
	Double quantity;
	@Column
	Date begin;
	@Column
	Date end;
	@ManyToOne
	Resource resource;
	@Column
	Boolean deleted;
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToOne
	TaskReport taskReport;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public TaskReport getTaskReport() {
		return taskReport;
	}
	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		if (obj instanceof ResourceUsage) {
			ResourceUsage w = (ResourceUsage) obj;
			if( w.id==null){
				return false;
			}
			return w.id.equals(id);
		}
		return false;
	}
}
