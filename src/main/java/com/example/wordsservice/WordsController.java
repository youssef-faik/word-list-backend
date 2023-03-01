package com.example.wordsservice;


import com.example.wordsservice.data.entity.WordEntity;
import com.example.wordsservice.data.repository.WordsRepository;
import com.example.wordsservice.data.requests.WordRequest;
import com.example.wordsservice.exceptions.WordNotFoundException;
import com.example.wordsservice.messages.WordsUpdateMessage;
import com.example.wordsservice.validation.ErrorsMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class WordsController {
  private final Logger logger = LogManager.getLogger(WordsController.class);
  
  private final WordsRepository repository;
  
  private final SimpMessagingTemplate simpMessagingTemplate;
  
  @Autowired
  public WordsController(WordsRepository repository, SimpMessagingTemplate simpMessagingTemplate) {
    this.repository = repository;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }
  
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Iterable<WordEntity>> getClients() {
    return ResponseEntity.ok(repository.findAllByOrderByCreateAt());
  }
  
  @GetMapping(value = "/{wordId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> getWordById(@PathVariable UUID wordId) {
    WordEntity wordEntity = repository.findById(wordId)
                                      .get();
    if (wordEntity == null) {
      throw new WordNotFoundException(wordId);
    }
    return ResponseEntity.ok(wordEntity);
  }
  
  @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> saveWord(@Valid @RequestBody WordRequest wordRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest()
                           .body(ErrorsMapper.fromNativeErrors(errors));
    }
    
    final WordEntity entityToSave = wordRequest.toEntity();
    final WordEntity savedEntity = repository.save(wordRequest.toEntity());
    
    this.send(entityToSave.getId() == null
                      ? WordsUpdateMessage.added(savedEntity)
                      : WordsUpdateMessage.updated(savedEntity));
    
    return ResponseEntity.ok(savedEntity);
  }
  
  @DeleteMapping(value = "/{wordId}")
  public ResponseEntity<?> deleteWordById(@PathVariable UUID wordId) {
    if (repository.existsById(wordId)) {
      repository.deleteById(wordId);
    }
    
    this.send(WordsUpdateMessage.removed(wordId.toString()));
    
    return ResponseEntity.noContent()
                         .build();
  }
  
  private void send(WordsUpdateMessage message) {
    this.simpMessagingTemplate.convertAndSend("/topic/words", message);
  }
}
