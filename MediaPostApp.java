// 12/14/23
// Pierce Mohney
// SDEV200
// Final Project
// IMPORTANT change the filepath for the default image path to your own device's filepath.
// This program is a media post application that contains the options to view different media posts and upload your own from your device.
// Uploaded posts will contain a user inputted username and a timestamp on each of their respective posts.
// Posts are separated by chronological order, once you post your image press the next button to see it.
// Picture frame images used for testing from Clipart.

package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediaPostApp extends Application {

    private List<MediaPost> mediaPosts;
    private int currentMediaPostIndex;
    private ImageView imageView;
    private Label userInfoLabel;
//Lists for post data

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Media Post Viewer");

        mediaPosts = new ArrayList<>();
        mediaPosts.add(new ImagePost("c:/Users/example/Desktop/example.jpg", "testuser", new Date()));
  //IMPORTANT Change the above media path to your own device's image path
        currentMediaPostIndex = 0;

        Image image = new Image(mediaPosts.get(currentMediaPostIndex).getImagePath());
        imageView = new ImageView(image);

        userInfoLabel = new Label();
        updateUserInfoLabel();

        Button nextButton = new Button("Next");
        Button lastButton = new Button("Last");
        Button uploadButton = new Button("Upload");

        nextButton.setOnAction(e -> showNextMediaPost());
        lastButton.setOnAction(e -> showLastMediaPost());
        uploadButton.setOnAction(e -> showUploadWindow(primaryStage));

        HBox topHBox = new HBox();
        topHBox.getChildren().add(uploadButton);
        topHBox.setAlignment(Pos.TOP_RIGHT);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(userInfoLabel, imageView, nextButton, lastButton);
        vbox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topHBox);
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 1000, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
//Creates main GUI window setup along with methods for buttons

    private void showNextMediaPost() {
        currentMediaPostIndex = (currentMediaPostIndex + 1) % mediaPosts.size();
        updateImageView();
        updateUserInfoLabel();
    }

    private void showLastMediaPost() {
        currentMediaPostIndex = (currentMediaPostIndex - 1 + mediaPosts.size()) % mediaPosts.size();
        updateImageView();
        updateUserInfoLabel();
    }

    private void updateImageView() {
        Image newImage = new Image(mediaPosts.get(currentMediaPostIndex).getImagePath());
        imageView.setImage(newImage);
    }

    private void updateUserInfoLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uploadDateStr = dateFormat.format(mediaPosts.get(currentMediaPostIndex).getUploadDate());
        userInfoLabel.setText("User: " + mediaPosts.get(currentMediaPostIndex).getUsername() +
                "\nUploaded: " + uploadDateStr);
    }
//Methods to iterate through media path index with buttons and change GUI window image

    private void showUploadWindow(Stage primaryStage) {
        Stage uploadStage = new Stage();
        uploadStage.setTitle("Upload Media");

        Label usernameLabel = new Label("Username");
        TextField usernameField = new TextField();

        Label mediaLabel = new Label("Select Media");
        ComboBox<String> mediaComboBox = new ComboBox<>();
        configureMediaComboBox(mediaComboBox);

        Button postButton = new Button("Post");
        postButton.setOnAction(e -> {
            String username = usernameField.getText();
            Date currentDate = new Date();
            mediaPosts.add(new ImagePost(mediaComboBox.getValue(), username, currentDate));
            updateImageView();
            updateUserInfoLabel();
            uploadStage.close();
        });

        VBox uploadVBox = new VBox();
        uploadVBox.getChildren().addAll(usernameLabel, usernameField, mediaLabel, mediaComboBox, postButton);

        Scene uploadScene = new Scene(uploadVBox, 300, 200);

        uploadStage.setScene(uploadScene);
        uploadStage.show();
    }
//Method for displaying upload window along with user input options

    private void configureMediaComboBox(ComboBox<String> mediaComboBox) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.*"));

        mediaComboBox.setPromptText("Select Media");

        mediaComboBox.setEditable(true);

        mediaComboBox.setOnMouseClicked(event -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                mediaComboBox.getItems().add(selectedFile.getAbsolutePath());
                mediaComboBox.setValue(selectedFile.getAbsolutePath());
            }
        });
    }
//Method for selecting local media from device in upload window
}

abstract class MediaPost {
    protected String username;
    protected Date uploadDate;

    public MediaPost(String username, Date uploadDate) {
        this.username = username;
        this.uploadDate = uploadDate;
    }

    public String getUsername() {
        return username;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public abstract String getImagePath();
}
//Gets image for GUI window along with respected details

class ImagePost extends MediaPost {
    private String imagePath;

    public ImagePost(String imagePath, String username, Date uploadDate) {
        super(username, uploadDate);
        this.imagePath = imagePath;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }
    //Class to give MediaPost class the correct post data
}
