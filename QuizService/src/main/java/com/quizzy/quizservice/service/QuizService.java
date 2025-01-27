package com.quizzy.quizservice.service;

import com.netflix.discovery.converters.Auto;
import com.quizzy.quizservice.dao.QuizRepo;
import com.quizzy.quizservice.feign.QuizInterface;
import com.quizzy.quizservice.model.QuestionWrapper;
import com.quizzy.quizservice.model.Quiz;
import com.quizzy.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizRepo quizRepo;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepo.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizRepo.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        quizInterface.getQuestionsFromId(questionIds);
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

        return questions;

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
      ResponseEntity<Integer> score = quizInterface.getScore(responses);

        return score;
    }
}
