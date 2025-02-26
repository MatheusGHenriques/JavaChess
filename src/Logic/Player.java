package Logic;

/**
 * Represents a player that owns the pieces of the color "white" or "black".
 */
public class Player{
    private final String color;

    /**
     * Instantiates a player.
     *
     * @param num Integer that will be converted into the player's color (0 = "white", 1 = "black").
     */
    public Player(int num){
        if(num == 0)
            color = "white";
        else color = "black";
    }

    /**
     * Gets the color of the player.
     *
     * @return String with the player's color.
     */
    public String getColor(){
        return color;
    }
}