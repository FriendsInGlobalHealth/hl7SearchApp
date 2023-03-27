package mz.org.fgh.hl7SearchApp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A24;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

import mz.org.fgh.hl7SearchApp.dto.Hl7Form;
import mz.org.fgh.hl7SearchApp.model.DemographicData;

@Controller
@RequestMapping("search")
public class SearchController {
	
    @Value("${app.hl7SearchApp.folder}") 
    private String hl7LocationFolder;
    
    @Value("${app.hl7SearchApp.locationUuid}") 
    private String healthFacilityUuid;

	@GetMapping
	public String getForm(Hl7Form Hl7Form) {
		return "home";
	}
	
	@PostMapping("getPatient") 
	public String searchPatient(@Valid Hl7Form Hl7Form, BindingResult bindingResult, Model model) throws FileNotFoundException {
		
		if (bindingResult.hasErrors()) { 
			return "home"; 
		}
		
		File hlfF = new File(Paths.get(hl7LocationFolder,String.valueOf(new StringBuilder(healthFacilityUuid).append(".hl7"))).toString());
		
		InputStream inputStream = new FileInputStream(hlfF);

		inputStream = new BufferedInputStream(inputStream);

        Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(inputStream);
        
        List<DemographicData> demographicData = new ArrayList<DemographicData>();
        
        while (iter.hasNext()) {

            Message hapiMsg = iter.next();
            
            ADT_A24 adtMsg = (ADT_A24) hapiMsg;
            PID pid = adtMsg.getPID();
            
            DemographicData data = new DemographicData();
            
    		data.setNid(pid.getPatientID().getIDNumber().getValue().trim());
    		data.setFirstName(pid.getPatientName(0).getGivenName().getValue());
    		data.setMiddleName(pid.getPatientName(0).getSecondAndFurtherGivenNamesOrInitialsThereof().getValue());
            data.setLastName(pid.getPatientName(0).getFamilyName().getSurname().getValue());
            data.setDateOfBirth(pid.getDateTimeOfBirth().getTime().getValue());
            data.setGender(pid.getAdministrativeSex().getValue());
            data.setAddress(pid.getPatientAddress(0).getStreetAddress().getStreetName().getValue());
            data.setCity(pid.getPatientAddress(0).getCity().getValue());
            data.setProvince(pid.getPatientAddress(0).getStateOrProvince().getValue());
            
            demographicData.add(data);
        }
        
        List<DemographicData> filteredDemo = new ArrayList<DemographicData>();
        
        for (DemographicData data : demographicData) {
			if (data.getNid().contains(Hl7Form.getPartialNid())) {
				filteredDemo.add(data);
			}
		}
        
        if (filteredDemo.isEmpty()) {
        	model.addAttribute("errorMessage", "Não encontramos nenhum item que corresponda à sua consulta de pesquisa.");
		} else {
			
			model.addAttribute("hl7Patients", filteredDemo);
		}
        
        return "home";
        
	}
}
