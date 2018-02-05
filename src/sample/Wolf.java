package sample;

import javafx.scene.control.Alert;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class Wolf extends Player {

    Boolean hasCaughtAllTheRabbits;
    Boolean hasEatenTheRabbit;

    Wolf (int boardRowsCount, int boardColumnsCount, GameUI gameUI) {
        this.boardRowsCount = boardRowsCount;
        this.boardColumnsCount = boardColumnsCount;
        this.fieldsArray = gameUI.fieldsArray;
        type = PlayerType.WOLF;
        this.gameUI = gameUI;
        this.hasCaughtAllTheRabbits = false;
        this.hasEatenTheRabbit = false;

        setPrimaryPosition(boardRowsCount, boardColumnsCount, gameUI);
    }

    void catchTheRabbits() {

        ArrayList<GameField> catchableFields = new ArrayList<GameField>();
        ArrayList<Double> distances = new ArrayList<Double>();
        ArrayList<GameField> reachableFields = new ArrayList<GameField>();

        for (int i = 0; i < boardRowsCount; i++) {
            for (int j = 0; j < boardColumnsCount; j++) {
                GameField currentField = fieldsArray[i][j];

                if (currentField.getColumnIndex() == playerCoordinates.getColumnIndex() || currentField.getColumnIndex() == playerCoordinates.getColumnIndex() - 1 || currentField.getColumnIndex() == playerCoordinates.getColumnIndex() + 1)
                    if (currentField.getRowIndex() == playerCoordinates.getRowIndex() || currentField.getRowIndex() == playerCoordinates.getRowIndex() - 1 || currentField.getRowIndex() == playerCoordinates.getRowIndex() + 1)
                        if (currentField.getPlayerTypeOnField() != PlayerType.WOLF)
                            reachableFields.add(currentField);
            }
        }

        for (Rabbit rabbit: gameUI.rabbits) {
            double distance;
            distance = sqrt(pow(playerCoordinates.getRowIndex() - rabbit.playerCoordinates.getRowIndex(), 2) + pow(playerCoordinates.getColumnIndex() - rabbit.playerCoordinates.getColumnIndex(), 2));

            GameField currentField = fieldsArray[rabbit.playerCoordinates.getRowIndex()][rabbit.playerCoordinates.getColumnIndex()];

            catchableFields.add(currentField);
            distances.add(distance);
        }

        System.out.println("Rabbits to catch: " + catchableFields.size());

        if (gameUI.rabbits.size() > 0) { // Check if there are no rabbits, cause then the wolf won't run!

            PlayerCoordinates reachableRabbitCoordinates = new PlayerCoordinates();
            Boolean hasReachableRabbit = false;

            for (GameField field : catchableFields) {
                if (reachableFields.contains(field)) { // The wolf always catches whatever it sees first!
                    reachableRabbitCoordinates.setNewCoordinates(field.getRowIndex(), field.getColumnIndex());
                    hasReachableRabbit = true;
                    System.out.println("There's a rabbit in the reachable area of the wolf! His coordinates: X: " + reachableRabbitCoordinates.getRowIndex() + ", Y: " + reachableRabbitCoordinates.getColumnIndex());
                }
            }

            if (hasReachableRabbit) {
                eatTheRabbit(reachableRabbitCoordinates);
                System.out.println("Has rabbit nearby; proceeding to eating it now...");
            }

            else {
                System.out.println("No rabbit nearby; moving on...");

                int indexOfNearestCatchableRabbit;
                Double shortestDistanceToRabbit = Double.MAX_VALUE;

                for (Double distance : distances)
                    if (distance < shortestDistanceToRabbit)
                        shortestDistanceToRabbit = distance;

                System.out.println("Shortest distance to the rabbit: " + shortestDistanceToRabbit);

                indexOfNearestCatchableRabbit = distances.indexOf(shortestDistanceToRabbit); // Wolf always tries to catch the nearest rabbit. He's a smart one, you see!
                GameField nearestCatchableRabbitField = catchableFields.get(indexOfNearestCatchableRabbit);

                Double shortestDistanceToRabbitFromNewField = Double.MAX_VALUE;
                PlayerCoordinates bestCoordinatesOfFieldToMoveTo = new PlayerCoordinates();

                System.out.println("Field with nearest rabbit: X: " + nearestCatchableRabbitField.getRowIndex() + ", Y: " + nearestCatchableRabbitField.getColumnIndex());

                for (GameField currentField : reachableFields) {
                    double currentDistanceToRabbit;
                    currentDistanceToRabbit = sqrt(pow(currentField.getRowIndex() - nearestCatchableRabbitField.getRowIndex(), 2) + pow(currentField.getColumnIndex() - nearestCatchableRabbitField.getColumnIndex(), 2));

                    if (currentDistanceToRabbit < shortestDistanceToRabbitFromNewField) {
                        shortestDistanceToRabbitFromNewField = currentDistanceToRabbit;
                        bestCoordinatesOfFieldToMoveTo.setNewCoordinates(currentField.getRowIndex(), currentField.getColumnIndex());
                    }
                }
                System.out.println("Shortest distance to rabbit from new field: " + shortestDistanceToRabbitFromNewField);
                System.out.println("The final chosen field to move to: X: " + bestCoordinatesOfFieldToMoveTo.getRowIndex() + ", Y: " + bestCoordinatesOfFieldToMoveTo.getColumnIndex());

                move(bestCoordinatesOfFieldToMoveTo);
            }
        }
        else {
            System.out.println("The wolf has completed his supper!");

            Alert waitForIt = new Alert(Alert.AlertType.WARNING);
            waitForIt.setTitle("Wait for it, Rabbit!");
            waitForIt.setHeaderText("Wait for it, Rabbit!");
            waitForIt.setContentText("I've caught you all, you little fools!\nMwahahahahahahahahahahahahaha!");
            waitForIt.show();
            hasCaughtAllTheRabbits = true;
        }

        System.out.println();

    }

    private void eatTheRabbit(PlayerCoordinates coordinatesToEat) {

        System.out.println("We're starting the supper...");

        ArrayList<Rabbit> rabbitsToEat = new ArrayList<Rabbit>();

        for (Rabbit rabbit: gameUI.rabbits) {

            System.out.println("Rabbit coordinates X: " + rabbit.playerCoordinates.getRowIndex() + " Y: " + rabbit.playerCoordinates.getColumnIndex());

            if (rabbit.playerCoordinates.getRowIndex() == coordinatesToEat.getRowIndex() && rabbit.playerCoordinates.getColumnIndex() == coordinatesToEat.getColumnIndex()) {
                rabbit.stopPlayerThread();
                rabbitsToEat.add(rabbit);
            }
        }

        gameUI.rabbits.removeAll(rabbitsToEat);
        move(coordinatesToEat);
        hasEatenTheRabbit = true;
    }
}
