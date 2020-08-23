package com.aamaro.backend.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aamaro.backend.entities.Link;
import com.aamaro.common.dto.LinkProjection;

public interface LinkRepository extends JpaRepository<Link, Long> {
	
	/*
	 * Query used by the frontend to check if the processing
	 * has been completed for the url
	 */
	@Query("SELECT l1 FROM Link l1 WHERE l1.parent is null and l1.url IN :urls")
	List<LinkProjection> poll(@Param("urls") List<String> urls);
	
	/*
	 * Keep only one "session" in the database per url
	 */
	@Transactional
	Long deleteByUrl(String url);
	
	/*
	 * Query used by the frontend to generated the URL list including
	 * only the parent records
	 */
	
	@Query("SELECT l1 FROM Link l1 WHERE l1.parent is null order by l1.createdOn desc")
	List<LinkProjection> findAllProjectedBy();	
}
