package com.example.assignment2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    private Trivia trivia;
    private Label questionLabel;
    private Label remainingQuestionsLabel;
    private VBox questionContainer;
    private VBox optionsContainer;
    private Button submitButton;
    private Button backButton;
    private int currentQuestionIndex;
    private int score;
    private Label timerLabel;
    private Timeline timeline;
    private int timeLeftInSeconds = 60; // 3 minutes
    private Button startButton;
    private Stage primaryStage;
    // Flag to track if time is up
    private boolean timeIsUp = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        trivia = new Trivia(); // Create an instance of the Trivia class
        currentQuestionIndex = 0;
        score = 0;

        questionLabel = new Label();
        remainingQuestionsLabel = new Label();
        questionContainer = new VBox(questionLabel, remainingQuestionsLabel);
        questionContainer.getStyleClass().add("question-container");

        optionsContainer = new VBox();
        optionsContainer.getStyleClass().add("options-container");

        submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(event -> {
            try {
                moveToNextQuestion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            try {
                moveToPreviousQuestion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        startButton = new Button("Restart");
        startButton.getStyleClass().add("restart-button");
        startButton.setOnAction(event -> {
            try {
                startGame(primaryStage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Create a container for the buttons
        HBox buttonContainer = new HBox(submitButton, backButton, startButton);
        buttonContainer.getStyleClass().add("button-container");

        // Apply styling to create a border around the button container
        buttonContainer.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        // Add the timer label to the options container
        timerLabel = new Label();
        timerLabel.getStyleClass().add("timer-label");

        // Create a container for the timer label and options container
        VBox timerAndOptionsContainer = new VBox(timerLabel, optionsContainer);

        HBox root = new HBox(questionContainer, optionsContainer, buttonContainer,timerAndOptionsContainer);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Apply CSS styling

        // Display the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia");
        primaryStage.show();

        // Add the button container to the question container
        questionContainer.getChildren().add(buttonContainer);


        // Create a VBox to hold the question, image/video, and buttons
        VBox mainContainer = new VBox(questionContainer, optionsContainer, buttonContainer);

        // Add the mainContainer to the root HBox
        root.getChildren().add(mainContainer);

        // Display the first question when the application starts
        displayQuestion(trivia.getQuestion(currentQuestionIndex));

        // Create and configure the timer label
        timerLabel = new Label();
        timerLabel.getStyleClass().add("timer-label");

        // Add the timer label to the root HBox
        root.getChildren().add(timerLabel);

        // Create the timeline for the timer
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> updateTimer()));

        // Start the timeline
        timeline.play();
    }
    private void updateTimer() {
        if (timeLeftInSeconds > 0) {
            timeLeftInSeconds--;
            int minutes = timeLeftInSeconds / 60;
            int seconds = timeLeftInSeconds % 60;
            String formattedTime = String.format("%02d:%02d", minutes, seconds);

            // Update the timer label on the JavaFX Application Thread
            Platform.runLater(() -> timerLabel.setText(formattedTime));
        } else {
            // Stop the timeline
            timeline.stop();

            // Display "GAME OVER" message
            Platform.runLater(() -> {
                questionContainer.getChildren().clear();
                optionsContainer.getChildren().clear();
                questionContainer.getChildren().add(new Label("GAME OVER"));

                // Enable the button to start again
                ((HBox) primaryStage.getScene().getRoot()).getChildren().add(startButton);
            });

            // Set the flag to true indicating that time is up
            timeIsUp = true;
        }
    }
    private void startGame(Stage primaryStage) throws InterruptedException {
        // Clear previous content
        questionContainer.getChildren().clear();
        optionsContainer.getChildren().clear();

        // Reset the game variables
        currentQuestionIndex = 0;
        score = 0;

        // Reset the timer
        timeLeftInSeconds = 60; // 3 minutes
        timerLabel.setText("01:00"); // Reset the timer label

        // Restart the timer timeline
        timeline.play();

        // Start displaying the questions
        displayQuestion(trivia.getQuestion(currentQuestionIndex));

        // Remove the startButton from the root HBox
        ((HBox) primaryStage.getScene().getRoot()).getChildren().remove(startButton);
    }


    // Display the current question and options
    private void displayQuestion(Question question) throws InterruptedException  {
        questionLabel.setText(question.getQuestion());

        // Clear previous question's content
        questionContainer.getChildren().clear();
        optionsContainer.getChildren().clear();

        // Create a VBox to hold question information and message
        VBox questionInfoContainer = new VBox();
        questionInfoContainer.getStyleClass().add("question-info-container");

        // Add label for "Questions answered:" and its value
        Label answeredLabel = new Label("Questions answered: " + currentQuestionIndex);
        answeredLabel.getStyleClass().add("question-info-label");
        questionInfoContainer.getChildren().add(answeredLabel);

        // Add label for "Questions left:" and its value
        Label leftLabel = new Label("Questions left: " + (trivia.getQuestionCount() - currentQuestionIndex - 1));
        leftLabel.getStyleClass().add("question-info-label");
        questionInfoContainer.getChildren().add(leftLabel);

        // Add question number and question description below the message
        int questionNumber = currentQuestionIndex + 1; // Question numbers start from 1
        Label questionTextLabel = new Label("Question " + questionNumber + ": " + question.getQuestion());
        questionTextLabel.getStyleClass().add("question-text-label");
        questionInfoContainer.getChildren().add(questionTextLabel);

        // Add description below the question text if available
        if (question.getDescription() != null) {
            Label descriptionLabel = new Label("Description: " + question.getDescription());
            questionInfoContainer.getChildren().add(descriptionLabel);
        }

        // Set question info container to the question container
        questionContainer.getChildren().add(questionInfoContainer);

        // Create an HBox to contain the media and options
        HBox mediaAndOptionsContainer = new HBox();
        questionContainer.getChildren().add(mediaAndOptionsContainer);

        // Display image if available
        if (question.getImagePath() != null) {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(question.getImagePath())));
            imageView.setFitWidth(400); // Set the width as needed
            imageView.setPreserveRatio(true);
            mediaAndOptionsContainer.getChildren().add(imageView);
        }

        // Display video if available
        String videoPath = question.getVideoPath();
        if (videoPath != null && !videoPath.isEmpty()) {
            Media media = new Media(getClass().getResource(videoPath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(400); // Set the width as needed
            mediaView.setFitHeight(300); // Set the height as needed

            // Add the MediaView to the mediaAndOptionsContainer
            mediaAndOptionsContainer.getChildren().add(mediaView);

            // Start playing the video
            mediaPlayer.play();

            // Add a listener to stop the video when the media ends
            mediaPlayer.setOnEndOfMedia(() -> stopVideo());
        }

        // Display options container on the right side
        mediaAndOptionsContainer.getChildren().add(optionsContainer);

        // Display options
        for (int i = 0; i < question.getOptions().length; i++) {
            Button optionButton = new Button(question.getOptions()[i]);
            optionButton.getStyleClass().add("option-button");
            int optionIndex = i;
            optionButton.setOnAction(event -> {
                selectOption(optionIndex, optionsContainer);
                // Enable the submit button when an option is selected
                submitButton.setDisable(false);
            });
            optionsContainer.getChildren().add(optionButton);
        }
    }
    // Utility method to select an option
    private void selectOption(int optionIndex, VBox optionsBox) {
        // Clear previous selection
        optionsBox.getChildren().forEach(node -> node.setStyle("-fx-background-color: transparent;"));

        // Highlight the selected option
        Button selectedButton = (Button) optionsBox.getChildren().get(optionIndex);
        selectedButton.setStyle("-fx-background-color: #00ff00");
    }
    // Move to the next question
    private void moveToNextQuestion() throws InterruptedException {
        // Check if an option is selected
        if (optionsContainer.getChildren().stream().anyMatch(node -> node.getStyle().contains("-fx-background-color: #00ff00"))) {
            currentQuestionIndex++;
            // Update score if the selected option is correct
            if (trivia.getQuestion(currentQuestionIndex - 1).getCorrectOptionIndex() == getSelectedOptionIndex()) {
                score++;
            }
            if (currentQuestionIndex < trivia.getQuestionCount()) {
                // Clear previous question's content
                questionContainer.getChildren().clear();
                optionsContainer.getChildren().clear();

                displayQuestion(trivia.getQuestion(currentQuestionIndex));
                submitButton.setDisable(true); // Disable the submit button for the next question
            } else {
                // Display final score
                displayFinalScore();
            }
        }
    }
    // Move to the previous question
    private void moveToPreviousQuestion() throws InterruptedException {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(trivia.getQuestion(currentQuestionIndex));
            submitButton.setDisable(true); // Disable the submit button for the previous question
        }
    }
    // Get the index of the selected option
    private int getSelectedOptionIndex() {
        for (int i = 0; i < optionsContainer.getChildren().size(); i++) {
            Button optionButton = (Button) optionsContainer.getChildren().get(i);
            if (optionButton.getStyle().contains("-fx-background-color: #00ff00")) {
                return i;
            }
        }
        return -1;
    }
    // Display final score
    // Display final score
    private void displayFinalScore() {
        // Stop the timer
        timeline.stop();

        // Calculate the time taken
        int minutes = (60 - timeLeftInSeconds) / 60;
        int seconds = (60 - timeLeftInSeconds) % 60;
        String timeTaken = String.format("%02d:%02d", minutes, seconds);

        int totalQuestions = trivia.getQuestionCount();
        int correctAnswers = score;
        int wrongAnswers = totalQuestions - correctAnswers;
        int finalScore = 10 - wrongAnswers;

        // Clear previous content
        questionContainer.getChildren().clear();
        optionsContainer.getChildren().clear();

        // Create a new VBox for final score
        VBox finalScoreBox = new VBox();
        finalScoreBox.getStyleClass().add("final-score-box");
        Label finalScoreLabel = new Label("Your final score: " + finalScore + " out of 10");
        finalScoreLabel.getStyleClass().add("final-score-label");
        Label timeTakenLabel = new Label("Time taken to complete: " + timeTaken);
        timeTakenLabel.getStyleClass().add("time-taken-label");
        finalScoreBox.getChildren().addAll(finalScoreLabel, timeTakenLabel);

        // Add the final score box to the question container
        questionContainer.getChildren().add(finalScoreBox);

        // Disable submit button
        submitButton.setDisable(true);
    }

    // Method to stop the video playback
    private void stopVideo() {
        MediaPlayer mediaPlayer = getCurrentlyPlayingMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    private MediaPlayer getCurrentlyPlayingMediaPlayer() {
        for (Node node : questionContainer.getChildren()) {
            if (node instanceof MediaView) {
                MediaView mediaView = (MediaView) node;
                MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
                if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    return mediaPlayer;
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
