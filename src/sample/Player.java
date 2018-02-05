package sample;

class Player {

    static PlayerType type;
    PlayerCoordinates playerCoordinates;

    int boardRowsCount;
    int boardColumnsCount;
    private int boardFieldWidth;

    GameField[][] fieldsArray;
    GameUI gameUI;

    private Thread playerThread;

    void setPrimaryPosition(int boardRowsCount, int boardColumnsCount, GameUI gameUI) {

        Boolean hasFoundEmptyField = false;

        while (!hasFoundEmptyField) {
            RandomGenerator random = new RandomGenerator();
            playerCoordinates = new PlayerCoordinates(random.generateValueInRange(0, boardRowsCount - 1), random.generateValueInRange(0, boardColumnsCount - 1));

            int x = playerCoordinates.getRowIndex();
            int y = playerCoordinates.getColumnIndex();

            for (int i = 0; i < boardRowsCount; i++) {
                for (int j = 0; j < boardColumnsCount; j++) {
                    GameField currentField = fieldsArray[i][j];
                    if (!currentField.getOccupiedStatus())
                        hasFoundEmptyField = true;
                }
            }

            fieldsArray[playerCoordinates.getRowIndex()][playerCoordinates.getColumnIndex()].setOccupiedBy(type);
            fieldsArray[playerCoordinates.getRowIndex()][playerCoordinates.getColumnIndex()].setPlayer(this);
            gameUI.drawGameBoard(gameUI.getGraphicsContext());
        }
    }

    void move(PlayerCoordinates coordinatesToMove) {

        fieldsArray[coordinatesToMove.getRowIndex()][coordinatesToMove.getColumnIndex()].setOccupiedBy(type);
        fieldsArray[coordinatesToMove.getRowIndex()][coordinatesToMove.getColumnIndex()].setPlayer(this);
        fieldsArray[playerCoordinates.getRowIndex()][playerCoordinates.getColumnIndex()].setFree();

        playerCoordinates = coordinatesToMove;
        gameUI.drawGameBoard(gameUI.getGraphicsContext());
    }

    void setPlayerThread(Thread thread) {
        playerThread = thread;
    }

    void stopPlayerThread() {
        playerThread.stop();
    }


}
