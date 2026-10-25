package com.example.data;

import com.example.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Repository providing verified, curated multiple-choice questions
 * across Science, Geography, History, Technology, Nature, Mathematics,
 * and General Culture.
 */
public class QuizRepository {

    public static final int DEFAULT_QUIZ_SIZE = 15;

    private static final List<Question> ALL_QUESTIONS = new ArrayList<>();

    static {
        ALL_QUESTIONS.add(new Question(
                "q_sci_01",
                "What organelle is famously referred to as the powerhouse of the cell?",
                Arrays.asList("Ribosome", "Mitochondria", "Endoplasmic Reticulum", "Golgi Apparatus"),
                1,
                "Science",
                "Mitochondria generate most of the chemical energy needed to power the cell's biochemical reactions through ATP production."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_geo_01",
                "What is traditionally recognized as the longest river in the world by total length?",
                Arrays.asList("Amazon River", "Nile River", "Yangtze River", "Mississippi River"),
                1,
                "Geography",
                "The Nile River in Africa spans approximately 6,650 kilometers (4,132 miles), making it traditionally recognized as Earth's longest river."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_his_01",
                "In what year did the Apollo 11 lunar module successfully touch down on the Moon?",
                Arrays.asList("1965", "1969", "1971", "1975"),
                1,
                "History",
                "Commander Neil Armstrong and Buzz Aldrin landed the Apollo Lunar Module Eagle on the Moon on July 20, 1969."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_tech_01",
                "Who is widely credited with inventing the World Wide Web at CERN in 1989?",
                Arrays.asList("Bill Gates", "Tim Berners-Lee", "Steve Jobs", "Alan Turing"),
                1,
                "Technology",
                "British scientist Tim Berners-Lee invented the World Wide Web while working at CERN in 1989."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_nat_01",
                "What is the largest living mammal currently inhabiting planet Earth?",
                Arrays.asList("African Bush Elephant", "Blue Whale", "Colossal Squid", "Giraffe"),
                1,
                "Nature",
                "The Antarctic blue whale can reach lengths of up to 30 meters and weigh up to 200 metric tons, making it the largest animal known."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_math_01",
                "What is the only even prime number in mathematics?",
                Arrays.asList("0", "2", "4", "6"),
                1,
                "Mathematics",
                "2 is the smallest prime number and the only even prime number, as all higher even numbers are divisible by 2."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_cul_01",
                "Which Renaissance master painted the celebrated portrait known as the 'Mona Lisa'?",
                Arrays.asList("Michelangelo", "Leonardo da Vinci", "Raphael", "Caravaggio"),
                1,
                "General Culture",
                "Leonardo da Vinci painted the Mona Lisa in the early 16th century, currently showcased in Paris at the Louvre Museum."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_sci_02",
                "What is the standard chemical symbol for the element Gold?",
                Arrays.asList("Ag", "Au", "Fe", "Pb"),
                1,
                "Science",
                "The chemical symbol Au derives from 'aurum', the Latin word for gold meaning glowing or shining dawn."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_geo_02",
                "Mount Kilimanjaro, the highest free-standing mountain on Earth, is situated in which country?",
                Arrays.asList("Kenya", "Tanzania", "Ethiopia", "South Africa"),
                1,
                "Geography",
                "Mount Kilimanjaro is a majestic dormant volcano located in northeastern Tanzania, rising 5,895 meters above sea level."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_his_02",
                "The ancient Greek city-state of Athens is celebrated as the historic birthplace of which system?",
                Arrays.asList("Feudalism", "Democracy", "Oligarchy", "Absolute Monarchy"),
                1,
                "History",
                "Athenian democracy flourished around the 5th century BC, establishing fundamental concepts of civic voting and self-governance."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_tech_02",
                "In computer science and digital memory, how many bits make up one standard byte?",
                Arrays.asList("4 bits", "8 bits", "16 bits", "32 bits"),
                1,
                "Technology",
                "A standard byte is composed of exactly 8 contiguous binary digits (bits), representing values from 0 to 255."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_nat_02",
                "What primary green pigment in plant leaves captures sunlight for photosynthesis?",
                Arrays.asList("Carotenoid", "Chlorophyll", "Anthocyanin", "Hemoglobin"),
                1,
                "Nature",
                "Chlorophyll absorbs blue and red electromagnetic wavelengths from sunlight while reflecting green light."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_math_02",
                "What is the exact sum of all interior angles inside any planar Euclidean triangle?",
                Arrays.asList("90 degrees", "180 degrees", "270 degrees", "360 degrees"),
                1,
                "Mathematics",
                "In Euclidean planar geometry, the sum of all three angles inside any triangle is always strictly equal to 180 degrees."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_cul_02",
                "Who authored the renowned English literary masterworks 'Hamlet', 'Macbeth', and 'King Lear'?",
                Arrays.asList("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"),
                1,
                "General Culture",
                "William Shakespeare penned these timeless tragedies during the Elizabethan and Jacobean eras in England."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_sci_03",
                "What fundamental physical constant approximates the speed of light travelling in a vacuum?",
                Arrays.asList("30,000 km/s", "300,000 km/s", "3,000,000 km/s", "150,000 km/s"),
                1,
                "Science",
                "The speed of light in vacuum is exactly 299,792,458 meters per second, which rounds to approximately 300,000 km/s."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_geo_03",
                "Which vast geographic region is officially the largest hot desert on Earth?",
                Arrays.asList("Gobi Desert", "Sahara Desert", "Kalahari Desert", "Arabian Desert"),
                1,
                "Geography",
                "Covering more than 9.2 million square kilometers across Northern Africa, the Sahara is Earth's largest subtropical hot desert."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_tech_03",
                "Who initiated and created the open-source Linux operating system kernel in 1991?",
                Arrays.asList("Richard Stallman", "Linus Torvalds", "Dennis Ritchie", "Ken Thompson"),
                1,
                "Technology",
                "Finnish software engineer Linus Torvalds developed the Linux kernel and released it to the worldwide community."
        ));

        ALL_QUESTIONS.add(new Question(
                "q_nat_03",
                "Which magnificent feline is the fastest land animal, capable of sprinting up to 100 km/h?",
                Arrays.asList("African Lion", "Cheetah", "Bengal Tiger", "Leopard"),
                1,
                "Nature",
                "The cheetah can accelerate from 0 to 96 km/h in just three seconds, making it the undisputed sprint champion of the land."
        ));
    }

    /**
     * Returns a curated list of questions for a new quiz session.
     * Optionally shuffles the questions and returns up to the requested count.
     */
    public static List<Question> getQuizQuestions(int count, boolean shuffle) {
        List<Question> pool = new ArrayList<>(ALL_QUESTIONS);
        if (shuffle) {
            Collections.shuffle(pool);
        }
        int effectiveCount = Math.min(count, pool.size());
        if (effectiveCount <= 0) {
            effectiveCount = DEFAULT_QUIZ_SIZE;
        }
        return new ArrayList<>(pool.subList(0, effectiveCount));
    }

    /**
     * Returns all available questions in the repository.
     */
    public static List<Question> getAllQuestions() {
        return Collections.unmodifiableList(ALL_QUESTIONS);
    }
}
