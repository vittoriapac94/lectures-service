package com.vipac.lecturesservice.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vipac.lecturesservice.domains.Lezione;
import com.vipac.lecturesservice.repositories.LezioneRepository;

@Service
public class LezioneService {
	
	@Autowired
	private LezioneRepository lezioneRepository;
	
	public Lezione creaLezione(@NotNull String lezioneId, @NotNull(message = "Il nome non può essere nullo") String nome,
			@NotNull(message = "Il professore non può essere nullo") String professore, String orario,
			List<String> iscritti, @NotNull String aula, String[][] postiOccupati) {
		return lezioneRepository.save(new Lezione(lezioneId, nome, professore, orario, iscritti, aula, postiOccupati));
	}
	
	public List<Lezione> getAll(){
		return lezioneRepository.findAll();
	}
	
	public Lezione getByLezioneId(String LezioneId) {
		return lezioneRepository.findByLezioneId(LezioneId);
	}
	
	public List<Lezione> getByProfessore(String professore) {
		return lezioneRepository.findByProfessore(professore);
	}
	
	public Lezione update(String lezioneId, int righe, int colonne) {
		Lezione lez = getByLezioneId(lezioneId);
		String [][] matriceBase = new String[righe][colonne];
		for(int i=0;i<matriceBase.length;i++) {
			for(int j=0;j<matriceBase[i].length;j++) {
				if((i+j)%2 == 0) 
					matriceBase[i][j]="libero";
				else
					matriceBase[i][j]="non_disponibile";
			}
		}	
		lez.setPostiOccupati(matriceBase);
		return lezioneRepository.save(lez);
	}
	
	public void deleteAll() {
		lezioneRepository.deleteAll();
	}
	
	public List<Lezione> getAllForUser(String username) {
		ArrayList<Lezione> list = new ArrayList<Lezione>();
		for (Lezione l : getAll()) {
			if (l.getIscritti().contains(username)) {
				list.add(l);
			}
		}
		return list;
	}

	public Lezione updatePrenotati(String idLezione, String matricola, String riga, String colonna) {
		Lezione lez = lezioneRepository.findByLezioneId(idLezione);
		String [][] postiOccupati = lez.getPostiOccupati();
		postiOccupati[Integer.parseInt(riga)][Integer.parseInt(colonna)] = "occupato";
		lez.setPostiOccupati(postiOccupati);
		List<String> iscritti = lez.getIscritti();
		iscritti.add(matricola);
		lez.setIscritti(iscritti);
		return lezioneRepository.save(lez);
	}
}
