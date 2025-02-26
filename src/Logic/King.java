package Logic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Logic.VectorPosition.*;

/**
 * Represents the piece King.
 */
public class King extends ChessPiece{
    /**
     * Instantiates the piece King.
     *
     * @param player The player to which the piece belongs to.
     */
    public King(Player player){
        super(player);

        if(player.getColor().equals("white"))
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wk.png")));
        else image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bk.png")));
    }

    /**
     * Gets the possible moves of this piece.
     *
     * @param board Reference of the logical board.
     * @param ind   If true, only return moves that don't result in the king being killed in the next turn.
     * @return ArrayList of Strings with the possible moves for this King, according to the board state.
     */
    @Override
    public ArrayList<String> getPossibleMoves(Board board, boolean ind){
        ArrayList<String> moves = new ArrayList<>();
        HashMap<String, ChessPiece> boardMap = board.getBoard();

        int[] position = board.getPosition(this);
        String newPosition;

        int[][] directions = {
                {0, 1},
                {0, -1},
                {-1, 0},
                {1, 0},
                {1, 1},
                {1, -1},
                {-1, -1},
                {-1, 1}
        };

        for(int[] d : directions){
            newPosition = convertVectorToStringPosition(vectorSum(d, position));
            if(VectorPosition.checkBoardBounds(newPosition)){
                if(boardMap.get(newPosition) == null)
                    moves.add(newPosition);
                else if(!(boardMap.get(newPosition).getPlayer().getColor().equals(super.getPlayer().getColor())))
                    moves.add(newPosition);
            }
        }
        if(ind)
            moves = getSafeCheckMoves(board, moves);
        return moves;
    }

    /**
     * Verifies if the King is in check.
     *
     * @param board Reference of the logical board.
     * @return String that represents the King's position if it's in check, if it's not in check, returns null.
     */
    public String isInCheck(Board board){
        String currentPosition = VectorPosition.convertVectorToStringPosition(board.getPosition(this));
        String enemy = this.getPlayer().getColor().equals("white")? "black" : "white";

        ArrayList<ChessPiece> enemyPieces = board.getPiecesByColor(enemy);

        for(ChessPiece enemyPiece : enemyPieces)
            if(enemyPiece.getPossibleMoves(board, false) != null)
                if(enemyPiece.getPossibleMoves(board, false).contains(currentPosition))
                    return currentPosition;
        return null;
    }
}
