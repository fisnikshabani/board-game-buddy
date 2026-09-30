package com.example.board_game_buddy.services;

import com.example.board_game_buddy.Question;
import reactor.core.publisher.Flux;

public interface BoardGameService {

    Flux<String> askQuestion(Question question);
}
