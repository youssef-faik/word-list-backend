package com.example.wordsservice.data.repository;

import com.example.wordsservice.data.entity.WordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WordsRepository extends CrudRepository<WordEntity, UUID> {
  List<WordEntity> findAllByOrderByCreateAt();
}
