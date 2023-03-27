package mz.org.fgh.hl7SearchApp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Hl7Form {

	@NotEmpty
	@Size(min = 11, max = 11) 
	private String partialNid;

	public String getPartialNid() {
		return partialNid;
	}

	public void setPartialNid(String partialNid) {
		this.partialNid = partialNid;
	}
}
