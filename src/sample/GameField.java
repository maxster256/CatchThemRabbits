package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;

/**
 * Created by pawelnarolski on 24.05.2017.
 */
public class GameField extends Rectangle2D {

    private Image fieldImage;
    private Image wolfImage;
    private Image rabbitImage;
    private Image grassImage;

    private int rowIndex;
    private int columnIndex;

    private Boolean isOccupied;
    private PlayerType playerType;
    private Player player;

    /**
     * Creates a new instance of {@code Rectangle2D}.
     *
     * @param minX   The x coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param minY   The y coordinate of the upper-left corner of the {@code Rectangle2D}
     * @param width  The width of the {@code Rectangle2D}
     * @param height The height of the {@code Rectangle2D}
     */

    public GameField(double minX, double minY, double size, int rowIndex, int columnIndex) {
        super(minX, minY, size, size);
        this.isOccupied = false;
        this.playerType = PlayerType.NO_PLAYER;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.grassImage = new Image(GameUI.class.getResource("grass.png").toExternalForm());
        this.wolfImage = new Image(GameUI.class.getResource("wolf.png").toExternalForm());
        this.rabbitImage = new Image(GameUI.class.getResource("rabbit.gif").toExternalForm());
        this.fieldImage = grassImage;
    }

    public Player getPlayer() {
        return player;
    }

    public synchronized Image getImage() {

        if (player instanceof Rabbit)
            return rabbitImage;
        else if (player instanceof Wolf)
            return wolfImage;
        else
            return grassImage;
    }

    public Boolean getOccupiedStatus() {
        return isOccupied;
    }

    public PlayerType getPlayerTypeOnField() {
        return playerType;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setOccupiedBy(PlayerType playerType) {
        isOccupied = true;
        this.playerType = playerType;
    }

    public void setWolfImage() {
        this.fieldImage = wolfImage;
    }

    public void setFree() {
        isOccupied = false;
        playerType = PlayerType.NO_PLAYER;
        fieldImage = grassImage;
        player = null;
    }
}
