package com.websystique.springmvc.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.websystique.springmvc.model.Note;
import com.websystique.springmvc.model.Registration;
import com.websystique.springmvc.service.NoteService;
import com.websystique.springmvc.service.RegistrationService;

@Controller
@RequestMapping("/user")
public class UserController {

	// create_reg
	// edit_reg
	// show_regs_list
	// reg_details
	// add_note

	@Autowired
	RegistrationService regsService;

	@Autowired
	NoteService noteService;

	@Autowired
	MessageSource messageSource;

	private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";

	private static final String JSP_PAGE_REGISTRATION_DETAIL_FORM = "registration";
	private static final String JSP_PAGE_REGISTRATION_DETAIL = "registrationDetail";
	private static final String JSP_PAGE_ACTION_SUCCESS = "success";
	private static final String JSP_PAGE_ACTION_FAILED = "failed";
	private static final String JSP_PAGE_REGISTRATIONS_LIST = "registrationsList";
	private static final String JSP_PAGE_NOTE_DETAIL_FORM = "note";
	private static final String JSP_PAGE_NOTE_DETAIL = "noteDetail";

	@RequestMapping(value = { "/create_reg" }, method = RequestMethod.GET)
	public String createRegistration(ModelMap model) {
		Registration newReg = new Registration();
		model.addAttribute("registration", newReg);
		return JSP_PAGE_REGISTRATION_DETAIL_FORM;
	}

	@RequestMapping(value = { "/create_reg", "/save_reg" }, method = RequestMethod.POST)
	public String saveRegistration(@Valid Registration registration,
			BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return JSP_PAGE_REGISTRATION_DETAIL_FORM;

		registration.setReg_date(LocalDate.now());

		// TODO test if the registration is unique - check the ico and date
		if (!regsService.isRegistrationUnique(registration.getIco(),
				registration.getReg_date())) {
			FieldError error = new FieldError("registration", "key",
					messageSource.getMessage(
							"non.unique.registration",
							new String[] {
									registration.getIco(),
									registration.getReg_date().toString(
											DATE_FORMAT_PATTERN) },
							Locale.getDefault()));
			result.addError(error);
			return JSP_PAGE_REGISTRATION_DETAIL_FORM;
		}

		regsService.saveRegistration(registration);
		model.addAttribute("success", "Registration of company (ico: "
				+ registration.getIco() + ") was created with date "
				+ registration.getReg_date().toString("dd.MM.yyyy"));

		return JSP_PAGE_ACTION_SUCCESS;
	}

	@RequestMapping(value = { "/edit_reg_{ico}_{regDate}" })
	public String editRegistration(ModelMap model, @PathVariable String ico,
			@PathVariable String regDateString) {

		findAndSetReg(model, ico, regDateString);

		return JSP_PAGE_REGISTRATION_DETAIL_FORM;
	}

	private void findAndSetReg(ModelMap model, String ico, String regDateString) {
		LocalDate regDate = LocalDate.parse(regDateString,
				DateTimeFormat.forPattern(DATE_FORMAT_PATTERN));

		Registration reg = regsService.findByKey(ico, regDate);
		if (reg != null) {
			model.addAttribute("registration", reg);
		}
	}

	@RequestMapping(value = { "/reg_detail_{ico}_{regDate}" })
	public String showRegistrationDetail(ModelMap model,
			@PathVariable String ico, @PathVariable String regDateString) {

		findAndSetReg(model, ico, regDateString);

		return JSP_PAGE_REGISTRATION_DETAIL;
	}

	@RequestMapping(value = { "show_regs_list" })
	public String showRegistrations(ModelMap model) {
		List<Registration> regs = getRegistrations();

		model.addAttribute("registrations_list", regs);

		return JSP_PAGE_REGISTRATIONS_LIST;
	}

	/**
	 * gets the all registration from DB
	 * 
	 * @return
	 */
	protected List<Registration> getRegistrations() {
		return regsService.findAllRegistrations();
	}

	@RequestMapping(value = { "/del_reg_{ico}_{regDate}" }, method = RequestMethod.GET)
	public void deleteRegistration(ModelMap model, @PathVariable String ico,
			@PathVariable String regDateString/* , BindingResult result */) {

		LocalDate regDate = LocalDate.parse(regDateString,
				DateTimeFormat.forPattern(DATE_FORMAT_PATTERN));

		Registration reg = regsService.findByKey(ico, regDate);
		// if (reg == null) {
		// FieldError error = new FieldError("registration", "key",
		// messageSource.getMessage("not.found.registration",
		// new String[] { ico, regDateString },
		// Locale.getDefault()));
		// result.addError(error);
		// return JSP_PAGE_ACTION_FAILED;
		// }

		regsService.deleteRegistration(ico, regDate.toDate());
		// model.addAttribute(
		// "success",
		// "Registration of company (ico: " + reg.getIco()
		// + ", registration date: "
		// + reg.getReg_date().toString("dd.MM.yyyy")
		// + " was deleted.");
		//
		// return JSP_PAGE_ACTION_SUCCESS;
		// TODO find the way what to do after delete
		System.out.println("ahoj");
	}

	@RequestMapping(value = { "/create_note" }, method = RequestMethod.POST)
	public String createNote(ModelMap model, Registration registration) {
		Note newNote = new Note();
		newNote.setRegistration(registration);
		model.addAttribute("note", newNote);
		return JSP_PAGE_NOTE_DETAIL_FORM;
	}

	@RequestMapping(value = { "/edit_note_{noteId}" }, method = RequestMethod.GET)
	public String editNote(ModelMap model, @PathVariable int noteId) {
		Note note = noteService.findById(noteId);
		model.addAttribute("note", note);
		return JSP_PAGE_NOTE_DETAIL_FORM;
	}

	@RequestMapping(value = { "/save_note" }, method = RequestMethod.POST)
	public String saveNote(ModelMap model, @Valid Note note) {
		noteService.saveNote(note);
		return JSP_PAGE_NOTE_DETAIL;
	}

	@RequestMapping("/del_note_{noteId}")
	public String deleteNote(ModelMap model, @PathVariable int noteId) {
		Note note = noteService.findById(noteId);
		Registration reg = (note == null) ? null : note.getRegistration();
		noteService.deleteNote(note);

		model.addAttribute("registration", reg);
		return JSP_PAGE_REGISTRATION_DETAIL;
	}
}
