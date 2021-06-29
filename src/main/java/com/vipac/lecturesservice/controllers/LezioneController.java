package com.vipac.lecturesservice.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.vipac.lecturesservice.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.vipac.lecturesservice.domains.Lezione;
import com.vipac.lecturesservice.services.LezioneService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LezioneController {
	
	@Autowired
	private LezioneService lezioneService;
	final static String authServiceURL = "http://localhost:8081/";
	static final String bookingServiceURL = "https://localhost:10443/";
	

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ModelAndView getAll(String currentUserJSON, String integrityAuth, String esitoPrenotazione, HttpServletRequest request) {
		//Verifica checksum
		String integrityChecksum = org.apache.commons.codec.digest.DigestUtils.sha1Hex(currentUserJSON);
		ModelAndView modelAndView = new ModelAndView("getAll");

		//getCookie
		String name = "sessionID";
		Optional<String> sessionID = Arrays.stream(request.getCookies())
				.filter(cookie->name.equals(cookie.getName()))
				.map(Cookie::getValue)
				.findAny();

		System.out.println("sessionID: "+sessionID.get());
		System.out.println("integrity: "+integrityAuth);

		if(!integrityAuth.equals(integrityChecksum)){
			//Checksum non valida - Pagina di errore
			modelAndView.setViewName("error");
		} else {
			List<Lezione> list = lezioneService.getAll();
			Gson gson = new Gson();
			User currentUser = gson.fromJson(currentUserJSON, User.class);

			Boolean [] prenotato = new Boolean[list.size()];
			for (int i=0; i<list.size(); i++) {
				if (list.get(i).getIscritti().contains(currentUser.getMatricola())) {
					prenotato[i] = true;
				} else {
					prenotato[i] = false;
				}
			}
			modelAndView.addObject("listaLezioni", list);
			modelAndView.addObject("esitoPrenotazione", esitoPrenotazione);
			modelAndView.addObject("currentUser", currentUser);
			modelAndView.addObject("prenotato", prenotato);
		}

        return modelAndView;
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView method() {
	    return new ModelAndView("redirect:" + authServiceURL);
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("home");
    }

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		return new ModelAndView("home");
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        return new ModelAndView("redirect:" + authServiceURL + "logout");
    }
	
	@RequestMapping(value = "/prenota", method = RequestMethod.GET)
    public ModelAndView prenota(@RequestParam String idLezione, @RequestParam String currentUser) {
		ModelAndView modelAndView = new ModelAndView("redirect:" + bookingServiceURL+ "prenota");
		Lezione lezioneSel = lezioneService.getByLezioneId(idLezione);
		Gson gson = new Gson();
		User user = gson.fromJson(currentUser, User.class);
		String lezioneJSON = gson.toJson(lezioneSel);
		modelAndView.addObject("lezioneSel", lezioneJSON);
		modelAndView.addObject("userJSON", currentUser);

		String integrityPrenota = org.apache.commons.codec.digest.DigestUtils.sha1Hex(currentUser + lezioneSel);
		modelAndView.addObject("integrityPrenota", integrityPrenota);

		if(lezioneSel.getIscritti().contains(user.getMatricola())) {
			modelAndView.addObject("currentUserJSON", currentUser);
			modelAndView.addObject("esitoPrenotazione", "Non è possibile effettuare la prenotazione, risulti già prenotato alla seguente lezione");
			modelAndView.setViewName("redirect:/getAll");
		}

        return modelAndView;
    }
	
	@RequestMapping(value = "/updateLezione", method = RequestMethod.GET)
    public ModelAndView updateLezione(String idLezione, String currentUser, String riga, String colonna, String integrityPrenotazione) {
		ModelAndView modelAndView = new ModelAndView("redirect:/getAll");
		String integrityChecksum = org.apache.commons.codec.digest.DigestUtils.sha1Hex(currentUser + idLezione + riga + colonna);

		if (!integrityPrenotazione.equals(integrityChecksum)) {
			//Checksum non valida - Pagina di errore
			modelAndView.setViewName("error");
		} else {
			String integrityAuth = org.apache.commons.codec.digest.DigestUtils.sha1Hex(currentUser);
			modelAndView.addObject("currentUserJSON", currentUser);
			modelAndView.addObject("integrityAuth", integrityAuth);
			Gson gson = new Gson();
			User user = gson.fromJson(currentUser, User.class);
			lezioneService.updatePrenotati(idLezione, user.getMatricola(), riga, colonna);
			modelAndView.addObject("esitoPrenotazione", "La prenotazione per la lezione è andata a buon fine");
		}
		return modelAndView;
	}
	
}
