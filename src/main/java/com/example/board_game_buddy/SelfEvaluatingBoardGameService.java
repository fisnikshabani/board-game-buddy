package com.example.board_game_buddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SelfEvaluatingBoardGameService implements BoardGameService {

    private final ChatClient chatClient;
    private final RelevancyEvaluator relevancyEvaluator;
    private final RetryTemplate retryTemplate;

    public SelfEvaluatingBoardGameService(ChatClient.Builder chatClientBuilder) {
        var chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini");
        this.chatClient = chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.retryTemplate = new RetryTemplate(RetryPolicy.builder()
                .includes(AnswerNotRelevantException.class)
                .build());
    }


    @Override
    public Answer askQuestion(Question question) {
        try {
            return retryTemplate.invoke(() -> {
                var answerText = chatClient.prompt()
                        .user(question.question())
                        .call()
                        .content();
                evaluateRelevancy(question, answerText);
                return new Answer(answerText);
            });
        }
        catch (AnswerNotRelevantException e) {
            return recover(e);
        }
    }

    public Answer recover(AnswerNotRelevantException e) {
        return new Answer("I'm sorry, I wasn't able to answer the question.");
    }

    private void evaluateRelevancy(Question question, String answerText) {
        var evaluationRequest = new EvaluationRequest(question.question(), answerText);
        var evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            throw new AnswerNotRelevantException(question.question(), answerText);
        }
    }
}
