import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.util.Random;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;
/**
 * class Jordle is JavaFX class that runs game.
 * @author Shavon Edih
 * @version 1.0
 */
public class Jordle extends Application {
    int row = 0;
    int column = 0;
    StringBuilder guess = new StringBuilder("");
    String correct = new String("");
    /**
     * start method starts the application and controls gameplay.
     * @param primary stage for setting everything
     */
    public void start(Stage primary) {
        Label[][] jordle = new Label[5][6];
        GridPane grid = new GridPane();
        grid = getGrid(jordle);
        grid.setAlignment(Pos.CENTER);
        correct = new String(newWord(Words.list));

        HBox hbox = new HBox(10);
        Label lbl = new Label("Try guessing a word!");
        Button instruct = new Button("Instructions");
        Button restart = new Button("Restart");
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cleared(jordle, lbl);
                correct = newWord(Words.list);
                guess = new StringBuilder("");
            }
        });
        instruct.setOnAction(er -> {
            Label sl = new Label(
                    "How To Play:\n1.You have 6 chances to guess 6 words in a 6 x 5 grid\n2.You must enter a valid 5 letter word\n3.Press ENTER to evaluate word and \ncheck for correct/incorrect letters\n4.Yellow boxes mean incorrect placement but correct letter, green boxes mean correct \nletter and placement, and grey boxes mean incorrect letter\n5.If you cannot get the word in 6 chances, GAME OVER!\n6.Once the game is over either press restart or exit out");
            StackPane stack = new StackPane();
            stack.getChildren().add(sl);
            Scene sc = new Scene(stack, 500, 500);
            Stage newStage = new Stage();
            newStage.setTitle("Instructions");
            newStage.setScene(sc);
            newStage.show();
        });
        hbox.getChildren().addAll(lbl, restart, instruct);
        hbox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        Label title = new Label("Jordle!");
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
        title.setTextFill(Color.BLACK);
        pane.setTop(title);
        pane.setAlignment(title, Pos.TOP_CENTER);
        pane.setCenter(grid);
        pane.setBottom(hbox);
        pane.setAlignment(hbox, Pos.BOTTOM_CENTER);
        pane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().isLetterKey() && column < 5) {
                    jordle[column][row].requestFocus();
                    jordle[column][row].setText(e.getCode().toString());
                    jordle[column][row].setAlignment(Pos.CENTER);
                    jordle[column][row].setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
                    jordle[column][row].setTextFill(Color.BLACK);
                    guess.append(e.getCode().toString());
                    column++;
                } else if (e.getCode() == KeyCode.ENTER && column < 5 && row <= 6) {
                    Label lab = new Label("ERROR: Please enter a valid 5 letter word!");
                    StackPane sp = new StackPane();
                    sp.getChildren().add(lab);
                    Scene scenie = new Scene(sp, 300, 300);
                    Stage stage = new Stage();
                    stage.setTitle("Error");
                    stage.setScene(scenie);
                    stage.show();
                } else if (e.getCode() == KeyCode.ENTER && column == 5 && row < 5) {
                    if (guess.toString().equals(correct)) {
                        check(jordle, correct, row);
                        lbl.setText("Congratulations! You've guessed the word!");
                    } else {
                        check(jordle, correct, row);
                        row++;
                        column = 0;
                        guess = new StringBuilder("");
                    }
                } else if (e.getCode() == KeyCode.BACK_SPACE && column <= 5 && column > 0) {
                    guess.deleteCharAt(column - 1);
                    column--;
                    jordle[column][row].requestFocus();
                    jordle[column][row].setText("");
                } else if (e.getCode() == KeyCode.ENTER && row == 5 && !(guess.toString().equals(correct))) {
                    check(jordle, correct, row);
                    lbl.setText("Game Over. The word was " + correct);
                }
                e.consume();
            }
        });

        Scene scene = new Scene(pane, 700, 700);
        primary.setResizable(false);
        primary.setTitle("Jordle");
        primary.setScene(scene);
        primary.show();
    }
    /**
     * getGrid creates gridpane toattack to borderpane for game.
     * @param labels 2d array for gridpane
     * @return gridpane for game
     */
    public GridPane getGrid(Label[][] labels) {
        GridPane gp = new GridPane();
        gp.setVgap(4);
        gp.setHgap(4);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                labels[i][j] = new Label();
                labels[i][j].setStyle("-fx-background-color: white; -fx-border-color: black");
                labels[i][j].setPrefSize(50, 50);
                gp.add(labels[i][j], i, j);
            }
        }
        gp.setAlignment(Pos.CENTER);
        return gp;
    }
    /**
     * cleared method clears board and resets the game.
     * @param lg 2d label array for grid
     * @param lb text label updating status of the game
     */
    public void cleared(Label[][] lg, Label lb) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                lg[i][j].setText("");
                lg[i][j].setStyle("-fx-background-color: white; -fx-border-color: black");
            }
        }
        lb.setText("Try guessing a word!");
        row = 0;
        column = 0;

    }
    /**
     * check is static method that checks if the guess is right.
     * changes corresponding guess boxes in grid.
     * @param la 2d label array
     * @param cor correct word
     * @param rows current row for checking
     */
    public static void check(Label[][] la, String cor, int rows) {
        for (int i = 0; i < 5; i++) {
            la[i][rows].setStyle("-fx-background-color: grey");
            la[i][rows].setTextFill(Color.WHITE);
        }
        Map<String, Integer> hash = new HashMap<>();
        hash = hashie(cor);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < cor.length(); j++) {
                if (la[i][rows].getText().equals(cor.substring(j, j + 1)) && hash.get(cor.substring(j, j + 1)) > 0 ) {
                    la[i][rows].setStyle("-fx-background-color: yellow");
                    la[i][rows].setTextFill(Color.WHITE);
                    hash.put(cor.substring(j, j + 1), (hash.get(cor.substring(j, j + 1))) - 1);
            
                }
            }  
            
        }
        for (int i = 0; i < 5; i++) {
            if (la[i][rows].getText().equals(cor.substring(i, i + 1))) {
                la[i][rows].setStyle("-fx-background-color: green");
                la[i][rows].setTextFill(Color.WHITE);
                
            }
        }


    }
    /**
     * newWord generates a new word for user.
     * @param string arraylist of possible words to choose from
     * @return string for new word to guess
     */
    public String newWord(ArrayList<String> string) {
        Random rand = new Random();
        int num = rand.nextInt(string.size());
        String word = string.get(num);
        word = word.toUpperCase();
        return word;
    }
    public static Map<String , Integer> hashie(String correct) {
        Map<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Integer integer = hashMap.get(correct.substring(i, i + 1));
            if (integer == null) {
                hashMap.put(correct.substring(i, i + 1), 1);
            } else {
                hashMap.put(correct.substring(i, i + 1), integer + 1);
            }
        }
        return hashMap;
    }
    /**
     * main method for running program.
     * @param args argument for running program to launch
     */
    public static void main(String[] args) {
        launch(args);
    }
}