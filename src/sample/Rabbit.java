package sample;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class Rabbit extends Player {

    Boolean isAlive;
    private Wolf wolf;

    Rabbit (int boardRowsCount, int boardColumnsCount, Wolf wolf, GameUI gameUI) {
        this.boardRowsCount = boardRowsCount;
        this.boardColumnsCount = boardColumnsCount;
        this.fieldsArray = gameUI.fieldsArray;
        this.wolf = wolf;
        type = PlayerType.RABBIT;
        this.gameUI = gameUI;
        this.isAlive = true;

        setPrimaryPosition(boardRowsCount, boardColumnsCount, gameUI);
    }

    void runFromTheWolf() {

        PlayerCoordinates wolfCoordinates = new PlayerCoordinates(wolf.playerCoordinates.getRowIndex(), wolf.playerCoordinates.getColumnIndex());
        ArrayList<GameField> reachableFields = new ArrayList<GameField>();

        for (int i = 0; i < boardRowsCount; i++) {
            for (int j = 0; j < boardColumnsCount; j++) {
                GameField currentField = fieldsArray[i][j];

                if (currentField.getColumnIndex() == playerCoordinates.getColumnIndex() || currentField.getColumnIndex() == playerCoordinates.getColumnIndex() - 1 || currentField.getColumnIndex() == playerCoordinates.getColumnIndex() + 1)
                    if (currentField.getRowIndex() == playerCoordinates.getRowIndex() || currentField.getRowIndex() == playerCoordinates.getRowIndex() - 1 || currentField.getRowIndex() == playerCoordinates.getRowIndex() + 1)
                        if (!currentField.getOccupiedStatus()) {
                            reachableFields.add(currentField);
                        }
            }
        }

        for (int i = 0; i < reachableFields.size(); i++) {

            GameField potentialField = reachableFields.get(i);

            double currentDistance = sqrt(pow(wolfCoordinates.getRowIndex() - playerCoordinates.getRowIndex(), 2) + pow(wolfCoordinates.getColumnIndex() - playerCoordinates.getColumnIndex(), 2));
            double newDistance = sqrt(pow(wolfCoordinates.getRowIndex() - potentialField.getRowIndex(), 2) + pow(wolfCoordinates.getColumnIndex() - potentialField.getColumnIndex(), 2));

            if (newDistance < currentDistance)
                reachableFields.remove(reachableFields.indexOf(potentialField));
        }

        RandomGenerator random = new RandomGenerator();
        int indexToMove = random.generateValueInRange(0, reachableFields.size());

        GameField chosenField = reachableFields.get(indexToMove);
        PlayerCoordinates newCoordinates = new PlayerCoordinates(chosenField.getRowIndex(), chosenField.getColumnIndex());

        move(newCoordinates);
    }
}
