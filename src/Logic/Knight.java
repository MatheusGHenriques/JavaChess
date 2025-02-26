package Logic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Logic.VectorPosition.*;

/**
 * Represents the piece Knight.
 */
public class Knight extends ChessPiece{
    /**
     * Instantiates a Knight.
     *
     * @param player The player to which the piece belongs to.
     */
    public Knight(Player player){
        super(player);
        if(player.getColor().equals("white"))
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wn.png")));
        else image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bn.png")));
    }

    /**
     * Gets the possible moves of this piece.
     *
     * @param board Reference of the logical board.
     * @param ind   If true, only return moves that don't result in the king being killed in the next turn.
     * @return ArrayList of Strings with the possible moves for this Knight, according to the board state.
     */
    @Override
    public ArrayList<String> getPossibleMoves(Board board, boolean ind){
        ArrayList<String> moves = new ArrayList<>();
        HashMap<String, ChessPiece> boardMap = board.getBoard();

        int[] position = board.getPosition(this);
        String newPosition;

        int[][] directions = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for(int[] d : directions){
            newPosition = convertVectorToStringPosition(vectorSum(d, position));
            if(checkBoardBounds(newPosition)){
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
}
