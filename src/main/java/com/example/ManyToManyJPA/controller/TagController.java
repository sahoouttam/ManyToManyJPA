package com.example.ManyToManyJPA.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.ManyToManyJPA.exception.ResourceNotFoundException;
import com.example.ManyToManyJPA.model.Tag;
import com.example.ManyToManyJPA.model.Tutorial;
import com.example.ManyToManyJPA.repository.TagRepository;
import com.example.ManyToManyJPA.repository.TutorialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TagController {
    
    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
    
        List<Tag> tags = new ArrayList<>();
        tagRepository.findAll().forEach(tags::add);

        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("tutorials/{tutorialId}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByTutorialId(@PathVariable("id") Long tutorialId) {
        if (!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id:" + tutorialId);
        }

        List<Tag> tags = tagRepository.findTagByTutorialsId(tutorialId);

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagsById(@PathVariable("id") Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Not found with Tag with id =" ));

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("tags/{tagId}/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorialsByTagId(@PathVariable("id") Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("Not found Tag with id:" + tagId);
        }

        List<Tutorial> tutorials = tutorialRepository.findTutorialsByTagsId(tagId);

        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<Tag> addTag(@PathVariable("tutorialId") Long tutorialId, @RequestBody Tag tag) {
        Tag new_tag = tutorialRepository.findById(tutorialId).map(tutorial -> {
            Long tagId = tag.getId();
            if (tagId != 0L) {
                Tag _tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id: " + tagId));
                tutorial.addTag(_tag);
                tagRepository.save(tutorial);
                return _tag;
            }

            tutorial.addTag(tag);
            return tagRepository.save(tag);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

        return new ResponseEntity<>(new_tag, HttpStatus.CREATED);
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") Long id, @RequestBody Tag tag) {
        Tag new_tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + id));
        
        new_tag.setName(tag.getName());

        return new ResponseEntity<>(tagRepository.save(new_tag), HttpStatus.OK);
    }

    @DeleteMapping("/tutorials/{tutorialId}/tags/{tagId}")
    public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable("tutorialId") Long tutorialId, @PathVariable("tagId") Long tagId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id:" + tutorialId));

        tutorial.removeTag(tagId);
        tagRepository.save(tutorial);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
        tagRepository.deleteById(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
