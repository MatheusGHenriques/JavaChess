package Logic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Logic.VectorPosition.*;

/**
 * Represents the piece Pawn.
 */
public class Pawn extends ChessPiece{
    protected String originalPosition;

    /**
     * Instantiates a Pawn.
     *
     * @param position String representing the original position of this pawn.
     * @param player   The player to which the piece belongs to.
     */
    public Pawn(String position, Player player){
        super(player);
        originalPosition = position;
        if(player.getColor().equals("white"))
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/wp.png")));
        else image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/bp.png")));
    }

    /**
     * Gets the possible moves of this piece.
     *
     * @param board Reference of the logical board.
     * @param ind   If true, only return moves that don't result in the king being killed in the next turn.
     * @return ArrayList of Strings with the possible moves for this Pawn, according to the board state.
     */
    @Override
    public ArrayList<String> getPossibleMoves(Board board, boolean ind){
        ArrayList<String> moves = new ArrayList<>();
        HashMap<String, ChessPiece> boardMap = board.getBoard();

        int[] position = board.getPosition(this);
        String newPosition;
        int i;

        if(originalPosition.charAt(0) == 'b')
            i = 2;
        else i = -2;

        if(originalPosition.equals(VectorPosition.convertVectorToStringPosition(board.getPosition(this))) && boardMap.get(convertVectorToStringPosition(vectorSum(position, new int[]{i / 2, 0}))) == null && boardMap.get(convertVectorToStringPosition(vectorSum(position, new int[]{i, 0}))) == null)
            if(VectorPosition.checkBoardBounds(vectorSum(vectorMultiplication(new int[]{1, 0}, i), position)))
                moves.add(convertVectorToStringPosition(vectorSum(vectorMultiplication(new int[]{1, 0}, i), position)));

        position = vectorSum(vectorMultiplication(new int[]{1, 0}, i / 2), position);

        if(boardMap.get(convertVectorToStringPosition(position)) == null && VectorPosition.checkBoardBounds(position))
            moves.add(convertVectorToStringPosition(position));

        for(int k = -1; k < 2; k++)
            if(k != 0){
                newPosition = convertVectorToStringPosition(vectorSum(vectorMultiplication(new int[]{0, 1}, k), position));
                if(boardMap.get(newPosition) != null && VectorPosition.checkBoardBounds(position))
                    if(!(boardMap.get(newPosition).getPlayer().equals(this.getPlayer())))
                        moves.add(newPosition);
            }
        if(ind)
            moves = getSafeCheckMoves(board, moves);
        return moves;
    }
}
