package com.example.ManyToManyJPA.repository;

import java.util.List;

import com.example.ManyToManyJPA.model.Tag;
import com.example.ManyToManyJPA.model.Tutorial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
    List<Tag> findTagByTutorialsId(Long tutorialsId);

    void save(Tutorial tutorial);
}
