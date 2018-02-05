package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;

public class GameUI extends Application {

    private static int columns = 16;
    private static int rows = 10;
    private static int fieldSize = 50;
    private static int fieldGap = 2;
    private static int rabbitsCount = 12;
    private static int waitTime = 5;

    GameField[][] fieldsArray;
    private GraphicsContext graphicsContext;
    private Canvas canvas;

    ArrayList<Rabbit> rabbits;

    @Override
    public void start(Stage primaryStage) throws Exception {
        configureUI(primaryStage);
    }

    private void configureUI(Stage stage) {

        Pane root = new Pane();

        canvas = new Canvas((fieldSize + fieldGap) * columns - fieldGap, (fieldSize + fieldGap) * rows - fieldGap);
        graphicsContext = canvas.getGraphicsContext2D();

        fieldsArray = new GameField[rows][columns];
        rabbits = new ArrayList<Rabbit>();

        configureGameBoard();
        drawGameBoard(graphicsContext);

        root.getChildren().add(canvas);

        Media nuPogodiTheme = new Media(GameUI.class.getResource("nu_pogod2.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(nuPogodiTheme);
        mediaPlayer.play();

        Wolf wolf = new Wolf(rows, columns, this);

        for (int i = 0; i < rabbitsCount; i++) {
            Rabbit rabbit = new Rabbit(rows, columns, wolf, this);

            Task rabbitTask = new Task() {
                @Override
                protected Object call() throws Exception {

                    while (rabbit.isAlive) {

                        rabbit.runFromTheWolf();
                        Thread.sleep(250);
                        Thread.yield();

                    }
                    return null;
                }
            };

            Thread rabbitThread = new Thread(rabbitTask);

            rabbitThread.start();
            rabbit.setPlayerThread(rabbitThread);
            rabbits.add(rabbit);
        }

        Task wolfTask = new Task() {
            @Override
            protected Object call() throws Exception {
                int rabbitCount = rabbitsCount;

                while (!wolf.hasCaughtAllTheRabbits) {

                    wolf.catchTheRabbits();
                    if (wolf.hasEatenTheRabbit) {
                        wolf.hasEatenTheRabbit = false;
                        Thread.sleep(1500);
                    }

                    Thread.sleep(250);
                    Thread.yield();

                }

                return null;
            }
        };

        Thread wolfThread = new Thread(wolfTask);
        wolf.setPlayerThread(wolfThread);
        wolfThread.start();

        Scene scene = new Scene(root, (fieldSize + fieldGap) * columns - fieldGap, (fieldSize + fieldGap) * rows - fieldGap, Color.WHITESMOKE);

        stage.setTitle("Catch them Rabbits, Wolf!");
        stage.setScene(scene);
        stage.show();
    }

    synchronized void drawGameBoard(GraphicsContext graphicsContext) {

        //clearGameBoard(graphicsContext, canvas);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                GameField currentField = fieldsArray[i][j];
                graphicsContext.drawImage(currentField.getImage(), currentField.getMinX(), currentField.getMinY(), currentField.getWidth(), currentField.getHeight());
            }
        }
    }

    private void configureGameBoard() {

        int fieldXPosition = 0;
        int fieldYPosition = 0;

        URL grassTexture = GameUI.class.getResource("grass.png");
        Image image = new Image(grassTexture.toExternalForm());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                Random random = new Random();
                float r = random.nextFloat();
                float g = random.nextFloat();
                float b = random.nextFloat();

                Color randomColor = new Color(r, g, b,1.0);

                fieldsArray[i][j] = new GameField(fieldXPosition, fieldYPosition, fieldSize, i, j);
                fieldXPosition = fieldXPosition + fieldSize + fieldGap;
            }
            fieldXPosition = 0;
            fieldYPosition = fieldYPosition + fieldSize + fieldGap;
        }

    }

    private void clearGameBoard(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }


    public static void main(String[] args) {
        launch(args);

        if (args.length > 3) {
            try {
                columns = Integer.parseInt(args[0]);
                rows = Integer.parseInt(args[1]);
                waitTime = Integer.parseInt(args[2]);
                rabbitsCount = Integer.parseInt(args[3]);
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }
}
