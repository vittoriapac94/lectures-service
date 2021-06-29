package com.vipac.lecturesservice.domains;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;

@Document(collection = "lectures")
public class Lezione {
	@Id
	String id;
	@NotNull
	String lezioneId;
	@NotNull(message="Il nome non può essere nullo")
	String nome;
	@NotNull(message="Il professore non può essere nullo")
	String professore;
	String orario;
	List<String> iscritti;
	@NotNull
	String aula;
	String [][] postiOccupati;
	
	public String getNome() {
		return nome;
	}
	public void setName(String nome) {
		this.nome = nome;
	}
	public String getLezioneId() {
		return lezioneId;
	}
	public void setLezioneId(String lezioneId) {
		this.lezioneId = lezioneId;
	}
	public String getProfessore() {
		return professore;
	}
	public void setProfessore(String professore) {
		this.professore = professore;
	}
	public String getOrario() {
		return orario;
	}
	public void setOrario(String orario) {
		this.orario = orario;
	}
	public List<String> getIscritti() {
		return iscritti;
	}
	public void setIscritti(List<String> iscritti) {
		this.iscritti = iscritti;
	}
	
	public String getAula() {
		return aula;
	}
	public void setAula(String aula) {
		this.aula = aula;
	}
	
	public String[][] getPostiOccupati() {
		return postiOccupati;
	}
	
	public void setPostiOccupati(String[][] postiOccupati) {
		this.postiOccupati = postiOccupati;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public Lezione(@NotNull String lezioneId, @NotNull(message = "Il nome non può essere nullo") String nome,
			@NotNull(message = "Il professore non può essere nullo") String professore, String orario,
			List<String> iscritti, @NotNull String aula, String[][] postiOccupati) {
		super();
		this.lezioneId = lezioneId;
		this.nome = nome;
		this.professore = professore;
		this.orario = orario;
		this.iscritti = iscritti;
		this.aula = aula;
		this.postiOccupati = postiOccupati;
	}
	
	
	
	
}