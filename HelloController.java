package com.example.gptdetector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class HelloController {

    @FXML
    private Label HumanScore, GptScore;
    @FXML
    private TextArea inputTextArea;

    @FXML
    public void getScoresButtonAction(ActionEvent event) {
        ApiService apiService = new ApiService();

        try {

            //what u did
            //String inputText = inputTextArea.getText();
            //String humanScore = apiService.getHumanScore(inputText);
            //String gptScore = apiService.getFakeScore(inputText);
            //HumanScore.setText("Human Score: " + humanScore+"%");
            //GptScore.setText("GPT Score: " + gptScore+"%");

            //what i did
            String inputText = inputTextArea.getText();
            String humanScore = apiService.getHumanScore(inputText);
            String gptScore = apiService.getFakeScore(inputText);

            int humanScoreInt = Integer.parseInt(humanScore);
            int gptScoreInt = Integer.parseInt(gptScore);

            System.out.println(humanScoreInt);
            System.out.println(gptScoreInt);

            if (humanScoreInt>65){
                HumanScore.setText("Human Generated");
                GptScore.setText("");
            }else if (humanScoreInt<35){
                GptScore.setText("GPT Generated");
                HumanScore.setText("");
            }
            else {
                HumanScore.setText("");
                GptScore.setText("Inconclusive");
            }
            //till here....converts string to int and no % make changes to html css
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while communicating with the API: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
