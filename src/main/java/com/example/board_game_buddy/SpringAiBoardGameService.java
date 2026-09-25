package com.example.board_game_buddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private static final String questionPromptTemplate = """
           You are a helpful assistant, answering questions about tabletop games.
           If you don't know anything about the game or don't know the answer,
           say "I don't know".
           
           The game is {gameTitle}.
            
           The question is: {question}.
            """;

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(questionPromptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("question", question.question()))
                .call()
                .content();
        return new Answer(question.gameTitle(), answerText);
    }
}
