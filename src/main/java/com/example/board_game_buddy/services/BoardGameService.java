package com.example.board_game_buddy.services;

import com.example.board_game_buddy.Answer;
import com.example.board_game_buddy.Question;

public interface BoardGameService {

    Answer askQuestion(Question question);
}
