package com.example.ManyToManyJPA.repository;

import java.util.List;

import com.example.ManyToManyJPA.model.Tutorial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial,  Long> {
    
    List<Tutorial> findTutorialsByTagsId(Long tagId);

    List<Tutorial> findByTitleContaining(String title);

    List<Tutorial> findByPublished(boolean published);


}
