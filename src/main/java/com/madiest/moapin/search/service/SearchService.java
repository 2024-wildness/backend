package com.madiest.moapin.search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchService {

  @Transactional(readOnly = true)
  public void search(String query) {
    // TODO: Implement search logic
  }
}
