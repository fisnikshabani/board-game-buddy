package com.example.board_game_buddy.controllers;

import com.example.board_game_buddy.Answer;
import com.example.board_game_buddy.Question;
import com.example.board_game_buddy.services.BoardGameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path = "/ask", produces = "application/ndjson")
    public Answer ask(@RequestBody @Valid Question question) {
        return boardGameService.askQuestion(question);
    }
}
