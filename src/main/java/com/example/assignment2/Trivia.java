package com.example.assignment2;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

class Trivia {
    private List<Question> questions;
    private int score;
    private MediaPlayer mediaPlayer;
    private ScheduledExecutorService executorService;
    private Runnable timerTask;
    private boolean isTimeUp;
    public Trivia() {
        questions = new ArrayList<>();
        score = 0;
        initializeQuestions();
        playBackgroundMusic(); // Call the method to play background music
    }
    private void playBackgroundMusic() {
        String musicPath = "C:/Users/khatleli/IdeaProjects/Assignment2/src/main/resources/music/Background Music.mp3";
        Media media = new Media(new File(musicPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
        mediaPlayer.play(); // Start playing the background music
    }
    // Initialize trivia questions
    private void initializeQuestions() {
        // Add Lesotho trivia questions with options and correct answers
        questions.add(new Question("What is the capital city of Lesotho?",
                new String[]{"Mokhotlong", "Leribe", "Maseru", "Thaba Tseka"}, 2, "/Images/maseru.jpg", null));
        questions.add(new Question("Which mountain range forms much of Lesotho's western border?",
                new String[]{"Drakensberg", "Thabana Ntlenyana", "Thaba Bosiu", "Qiloane"}, 0, "/Images/drakensberg.jpg", null));
        questions.add(new Question("What is the highest mountain in Lesotho?",
                new String[]{"Thabana Ntlenyana", "Qiloane", "Thaba Bosiu", "Drakensberg"}, 0, "/Images/ThabanaNtlenyana.jpeg", null));
        questions.add(new Question("What is the highest pub in Lesotho?",
                new String[]{"Masoba soba Logde", " Sani Pass", "Sani Stone Logde", "Sani Mountain Lodge"}, 3, null, "/Videos/Mountain.mp4"));
        questions.add(new Question("The Longest River found in Lesotho?",
                new String[]{"Khubelu", "Senqu", "Maliba Matsho", "Sehong hong"}, 1, "/Images/senqu.jpg", null));
        questions.add(new Question("Which mountain is an image of Basotho hat?",
                new String[]{"Qiloane", "Thabana Ntlenyana", "Thaba Bosiu", "Sehong gong"}, 0, "/Images/Qiloane.jpg", null));
        questions.add(new Question("Biggest waterfall found in Lesotho?",
                new String[]{"Maletsunyane Falls", "Semonkong Falls", "Katse Dam Waterfall", "Mohale Dam Waterfall"}, 0, null, "/Videos/Maletsunyane.mp4"));
        questions.add(new Question("Who is the current King of Lesotho?",
                new String[]{"King Letsie III", "Mohato Bereng Seeiso", "Sam Ntsokoane Matekane", "Mathibeli Mokhothu"}, 0, null, "/Videos/Motlotlehi.mp4"));
        questions.add(new Question("When did Lesotho obtain/gain its independence?",
                new String[]{"October 4, 2010", "October 4, 1960", "October 4, 2000", "October 4, 1966"}, 3, "/Images/independence.jpg", null));
        questions.add(new Question("Who was the first Prime Minister of Lesotho?",
                new String[]{"Dr. Ntsu Mokhehle", "Chief Leabua Jonathan", "Dr. Pakalitha Mosisili", "Dr. Motsoahae Thomas Thabana"}, 1, "/Images/primeMinisters.jpg", null));
    }
    // Get the current question
    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        } else {
            // Handle the case when the index is out of bounds
            return null;
        }
    }
    // Get the total number of questions
    public int getQuestionCount() {
        return questions.size();
    }
}
    class Question {
        private String question;
        private String[] options;
        private int correctOptionIndex;
        private String imagePath;
        private String videoPath;
    public Question(String question, String[] options, int correctOptionIndex, String imagePath, String videoPath) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
    }
    public String getQuestion() {
        return question;
    }
    public String[] getOptions() {
        return options;
    }
    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getVideoPath() {
        return videoPath;
    }
    public String getDescription() {
        return null;
    }

    }
