package com.example;

import com.example.data.QuizRepository;
import com.example.engine.QuizEngine;
import com.example.model.Question;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Comprehensive Unit Test suite covering QuizEngine, Question model,
 * answer submission, duplicate score protection, progression, and serialization.
 */
public class QuizEngineTest {

    private List<Question> sampleQuestions;
    private QuizEngine engine;

    @Before
    public void setUp() {
        sampleQuestions = new ArrayList<>();
        sampleQuestions.add(new Question(
                "q1",
                "What is the capital of France?",
                Arrays.asList("Berlin", "Paris", "Rome", "Madrid"),
                1,
                "Geography",
                "Paris is the capital of France."
        ));
        sampleQuestions.add(new Question(
                "q2",
                "What is 2 + 2?",
                Arrays.asList("3", "4", "5", "6"),
                1,
                "Mathematics",
                "2 + 2 equals 4."
        ));
        sampleQuestions.add(new Question(
                "q3",
                "What is the chemical symbol for Water?",
                Arrays.asList("CO2", "H2O", "O2", "NaCl"),
                1,
                "Science",
                "H2O represents two hydrogen atoms and one oxygen atom."
        ));

        engine = new QuizEngine(sampleQuestions);
    }

    @Test
    public void test1_startingQuizDisplaysFirstQuestion() {
        assertEquals(0, engine.getCurrentIndex());
        assertNotNull(engine.getCurrentQuestion());
        assertEquals("What is the capital of France?", engine.getCurrentQuestion().getQuestionText());
    }

    @Test
    public void test2_everyQuestionContainsExactlyFourOptions() {
        List<Question> repoQuestions = QuizRepository.getAllQuestions();
        assertTrue("Repository must have at least 15 questions", repoQuestions.size() >= 15);
        for (Question q : repoQuestions) {
            assertNotNull(q.getId());
            assertNotNull(q.getQuestionText());
            assertEquals("Each question must contain exactly 4 options", 4, q.getOptions().size());
            assertTrue("Correct answer index must be 0..3", q.getCorrectAnswerIndex() >= 0 && q.getCorrectAnswerIndex() < 4);
            assertNotNull(q.getCorrectAnswerText());
            assertNotNull(q.getCategory());
            assertNotNull(q.getExplanation());
        }
    }

    @Test
    public void test3_correctAnswerIncreasesScoreByOne() {
        assertEquals(0, engine.getScore());
        boolean isCorrect = engine.submitAnswer(1); // Paris
        assertTrue(isCorrect);
        assertEquals(1, engine.getScore());
    }

    @Test
    public void test4_incorrectAnswerDoesNotIncreaseScore() {
        assertEquals(0, engine.getScore());
        boolean isCorrect = engine.submitAnswer(0); // Berlin (wrong)
        assertFalse(isCorrect);
        assertEquals(0, engine.getScore());
    }

    @Test
    public void test5_repeatedTapsCannotAwardDuplicatePoints() {
        assertEquals(0, engine.getScore());
        boolean firstTap = engine.submitAnswer(1); // Correct
        assertTrue(firstTap);
        assertEquals(1, engine.getScore());

        // Attempting to tap again on the same question
        boolean secondTap = engine.submitAnswer(1);
        assertEquals("Score must not increase on duplicate tap", 1, engine.getScore());

        // Attempting to switch answer
        boolean thirdTap = engine.submitAnswer(0);
        assertEquals("Answer must be locked", 1, engine.getScore());
        assertEquals(1, engine.getCurrentSelectedOption());
    }

    @Test
    public void test6_answerFeedbackShowsCorrectAndSelectedAnswers() {
        engine.submitAnswer(2); // Rome (wrong)
        assertTrue(engine.isCurrentQuestionAnswered());
        assertFalse(engine.isCurrentAnswerCorrect());
        assertEquals(2, engine.getCurrentSelectedOption());
        assertEquals(1, engine.getCurrentQuestion().getCorrectAnswerIndex());
        assertEquals("Paris", engine.getCurrentQuestion().getCorrectAnswerText());
    }

    @Test
    public void test7_nextAdvancesToCorrectQuestion() {
        assertFalse(engine.canAdvance()); // cannot advance before answering
        engine.submitAnswer(1);
        assertTrue(engine.canAdvance());

        boolean advanced = engine.advance();
        assertTrue(advanced);
        assertEquals(1, engine.getCurrentIndex());
        assertEquals("What is 2 + 2?", engine.getCurrentQuestion().getQuestionText());
    }

    @Test
    public void test8_finalQuestionMarksQuizFinished() {
        // Q1
        engine.submitAnswer(1);
        engine.advance();

        // Q2
        engine.submitAnswer(1);
        engine.advance();

        // Q3 (Last question)
        assertTrue(engine.isLastQuestion());
        assertFalse(engine.isQuizFinished()); // not finished until answered

        engine.submitAnswer(1);
        assertTrue(engine.isQuizFinished());
        assertFalse(engine.canAdvance());
    }

    @Test
    public void test9_finalScoreAndPercentageAreAccurate() {
        // 2 out of 3 correct: 66.67% -> rounds to 67%
        engine.submitAnswer(1); // Q1 Correct
        engine.advance();

        engine.submitAnswer(0); // Q2 Wrong
        engine.advance();

        engine.submitAnswer(1); // Q3 Correct

        assertEquals(2, engine.getScore());
        assertEquals(1, engine.getIncorrectCount());
        assertEquals(67, engine.getPercentage());
        assertEquals(QuizEngine.PerformanceTier.GOOD, engine.getPerformanceTier());
    }

    @Test
    public void test10_restartingResetsSession() {
        engine.submitAnswer(1);
        engine.advance();
        assertEquals(1, engine.getScore());

        // Fresh session
        QuizEngine freshEngine = new QuizEngine(sampleQuestions);
        assertEquals(0, freshEngine.getScore());
        assertEquals(0, freshEngine.getCurrentIndex());
        assertFalse(freshEngine.isCurrentQuestionAnswered());
        assertEquals(-1, freshEngine.getCurrentSelectedOption());
    }

    @Test
    public void test11_answerReviewTracksRealResponses() {
        engine.submitAnswer(0); // User picked 0 for Q1
        engine.advance();
        engine.submitAnswer(1); // User picked 1 for Q2
        engine.advance();
        engine.submitAnswer(2); // User picked 2 for Q3

        assertEquals(0, engine.getUserAnswerFor(0));
        assertEquals(1, engine.getUserAnswerFor(1));
        assertEquals(2, engine.getUserAnswerFor(2));
    }

    @Test
    public void test12_emptyOrMissingQuestionDataDoesNotCrash() {
        QuizEngine emptyEngine = new QuizEngine(Collections.<Question>emptyList());
        assertEquals(0, emptyEngine.getTotalQuestions());
        assertEquals(0, emptyEngine.getScore());
        assertEquals(0, emptyEngine.getPercentage());
        assertNull(emptyEngine.getCurrentQuestion());
        assertTrue(emptyEngine.isQuizFinished());
        assertFalse(emptyEngine.canAdvance());
        assertFalse(emptyEngine.submitAnswer(0));
    }

    @Test
    public void test13_serializationPreservesQuizStateAcrossRotation() throws Exception {
        engine.submitAnswer(1); // Q1 correct
        engine.advance();
        engine.submitAnswer(0); // Q2 wrong

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(engine);
        oos.close();

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        QuizEngine restored = (QuizEngine) ois.readObject();
        ois.close();

        assertEquals(engine.getCurrentIndex(), restored.getCurrentIndex());
        assertEquals(engine.getScore(), restored.getScore());
        assertEquals(engine.getUserAnswerFor(0), restored.getUserAnswerFor(0));
        assertEquals(engine.getUserAnswerFor(1), restored.getUserAnswerFor(1));
        assertEquals(engine.isCurrentQuestionAnswered(), restored.isCurrentQuestionAnswered());
    }
}
