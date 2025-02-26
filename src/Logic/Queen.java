package Logic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Logic.VectorPosition.*;

/**
 * Represents the piece Queen.
 */
public class Queen extends ChessPiece{
    /**
     * Instantiates a Queen.
     *
     * @param player The player to which the piece belongs to.
     */
    public Queen(Player player){
        super(player);
        if(player.getColor().equals("white"))
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wq.png")));
        else image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bq.png")));
    }

    /**
     * Gets the possible moves of this piece.
     *
     * @param board Reference of the logical board.
     * @param ind   If true, only return moves that don't result in the king being killed in the next turn.
     * @return ArrayList of Strings with the possible moves for this Queen, according to the board state.
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
            for(int i = 1; checkBoardBounds(vectorSum(vectorMultiplication(d, i), position)); i++){
                newPosition = convertVectorToStringPosition(vectorSum(vectorMultiplication(d, i), position));
                if(boardMap.get(newPosition) == null){
                    moves.add(newPosition);
                }else{
                    if(!(boardMap.get(newPosition).getPlayer().getColor().equals(super.getPlayer().getColor())))
                        moves.add(newPosition);
                    break;
                }
            }
        }
        if(ind)
            moves = getSafeCheckMoves(board, moves);
        return moves;
    }
}
