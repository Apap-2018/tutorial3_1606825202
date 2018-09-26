package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
					  @RequestParam(value = "licenseNumber", required = true) String licenseNumber,
					  @RequestParam(value = "name", required = true) String name,
					  @RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	/**
	 * Method View by licenseNumber
	 * untuk melihat data pilot berdasarkan licenseNumber
	 * @param licenseNumber
	 * @param model
	 * @return
	 */
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	/**
	 * Method View All
	 * untuk melihat seluruh data pilot tanpa memasukkan license number
	 * @param model
	 * @return
	 */
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
	 List<PilotModel> archive = pilotService.getPilotList();
	 
	 model.addAttribute("listPilot", archive);
	 return "viewall-pilot";
	}
	
	/**
	 * Latihan no 1
	 */
	@RequestMapping(value = {"/pilot/view/license-number",
							"/pilot/view/license-number/{licenseNumber}"})
	public String viewPilot(@PathVariable Optional<String> licenseNumber, Model model) {
		if (!licenseNumber.isPresent()) {
			return "error_empty_license_number";
		}
		else{
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if (archive == null) {
				String errorMsg = "Tidak ada pilot dengan license number " + licenseNumber.get();
				model.addAttribute("errorMsg", errorMsg);
				return "error_no_pilot";
			}
			else {
				model.addAttribute("pilot", archive);
				return "view-pilot";			
			}			
		}
	}
	
	/**
	 * Latihan no 2
	 */
	@RequestMapping(value = {"/pilot/update/license-number", 
							 "/pilot/update/license-number/{licenseNumber}/fly-hour/{newHour}"})
	public String updateFlyHour(@PathVariable Optional<String> licenseNumber, 
								@PathVariable Optional<Integer> newHour, Model model) {
		if (!licenseNumber.isPresent()) {
			return "error_empty_license_number";
		}
		else {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if (archive == null) {
				String errorMsg = "Tidak ada pilot dengan license number " + licenseNumber.get();
				model.addAttribute("errorMsg", errorMsg);
				return "error_no_pilot";
			}
			else {
				archive.setFlyHour(newHour.get());
				model.addAttribute("pilot", archive);
				return "update_fly_hour";
			}	
		}
	}
	
	/**
	 * Latihan no 3
	 */
	@RequestMapping(value = {"pilot/delete/id",
							 "pilot/delete/id/{licenseNumber}"})
	public String deletePilot(@PathVariable Optional<String> licenseNumber, Model model) {
		if (!licenseNumber.isPresent()) {
			return "error_empty_license_number";
		}
		else {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			if (archive == null) {
				String errorMsg = "Tidak ada pilot dengan license number " + licenseNumber.get();
				model.addAttribute("errorMsg", errorMsg);
				return "error_no_pilot"; 
			}
			else {
				pilotService.deletePilot(archive);
				model.addAttribute("deleteMsg", archive.getName() + " telah dihapus");
				return "delete_pilot";
			}
		}
	}
	
}
