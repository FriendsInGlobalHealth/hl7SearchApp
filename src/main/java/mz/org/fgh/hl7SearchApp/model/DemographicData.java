package mz.org.fgh.hl7SearchApp.model;

import lombok.Data;

@Data
public class DemographicData {

	private String nid;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String dateOfBirth;
	
	private String gender;
	
	private String address;
	
	private String city;
	
	private String province;
}
