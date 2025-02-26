package Logic;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Abstract class that sets common behaviours to all chess pieces.
 */
public abstract class ChessPiece{
    private final Player player;
    protected ImageIcon image;

    /**
     * Instantiates a piece.
     *
     * @param player The player to which the piece belongs to.
     */
    public ChessPiece(Player player){
        this.player = player;
    }

    /**
     * Gets the player that owns this piece.
     *
     * @return Player that owns this piece.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Getst the icon of a piece.
     *
     * @return ImageIcon of this piece.
     */
    public ImageIcon getImage(){
        return image;
    }

    /**
     * Abstract method that gets the possible moves of a piece.
     *
     * @param board Reference of the logical board.
     * @param ind   If true, only return moves that don't result in the king being killed in the next turn.
     * @return ArrayList of Strings with the possible moves for the piece, according to the board state.
     */
    public abstract ArrayList<String> getPossibleMoves(Board board, boolean ind);

    /**
     * Gets the moves that don't result in the king being killed in the next turn.
     *
     * @param board    Reference of the logical board.
     * @param allMoves ArrayList of Strings containing all possible moves of this piece.
     * @return ArrayList of Strings containing all moves that don't result in the king being killed in the next turn.
     */
    public ArrayList<String> getSafeCheckMoves(Board board, ArrayList<String> allMoves){
        ArrayList<String> safeMoves = new ArrayList<>();

        String myColor = this.player.getColor();

        for(String move : allMoves){
            if(move != null){
                Board tempBoard = board.cloneBoard();

                tempBoard.movePiece(tempBoard.getPosition(this), VectorPosition.convertStringToVector(move), null);

                King tempKing = tempBoard.getMyKing(myColor);

                if(tempKing.isInCheck(tempBoard) == null)
                    safeMoves.add(move);
            }
        }
        if(safeMoves.isEmpty())
            return null;
        return safeMoves;
    }
}