package com.vipac.lecturesservice.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vipac.lecturesservice.domains.Lezione;

@Repository
public interface LezioneRepository extends MongoRepository<Lezione, String> {
	
	public Lezione findByLezioneId(String lezioneId);
	
	public List<Lezione> findByProfessore(String professore);

}
