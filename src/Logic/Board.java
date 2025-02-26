package Logic;

import GUI.GamePanel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the chess board, storing pieces and their positions on the board with a HashMap.
 */
public class Board{
    private HashMap<String, ChessPiece> board;
    private final Player white = new Player(0);
    private final Player black = new Player(1);

    /**
     * Instantiates the Board.
     *
     * @param fill If true, the board should be filled with the default pieces in their respective positions.
     */
    public Board(boolean fill){
        board = new HashMap<>();
        if(fill)
            fillBoard();
    }

    /**
     * Fill the board with the default pieces in their respective positions.
     */
    public void fillBoard(){
        board.put("a0", new Rook(black));
        board.put("a1", new Knight(black));
        board.put("a2", new Bishop(black));
        board.put("a3", new Queen(black));
        board.put("a4", new King(black));
        board.put("a5", new Bishop(black));
        board.put("a6", new Knight(black));
        board.put("a7", new Rook(black));
        board.put("b0", new Pawn("b0", black));
        board.put("b1", new Pawn("b1", black));
        board.put("b2", new Pawn("b2", black));
        board.put("b3", new Pawn("b3", black));
        board.put("b4", new Pawn("b4", black));
        board.put("b5", new Pawn("b5", black));
        board.put("b6", new Pawn("b6", black));
        board.put("b7", new Pawn("b7", black));

        board.put("h0", new Rook(white));
        board.put("h1", new Knight(white));
        board.put("h2", new Bishop(white));
        board.put("h3", new Queen(white));
        board.put("h4", new King(white));
        board.put("h5", new Bishop(white));
        board.put("h6", new Knight(white));
        board.put("h7", new Rook(white));
        board.put("g0", new Pawn("g0", white));
        board.put("g1", new Pawn("g1", white));
        board.put("g2", new Pawn("g2", white));
        board.put("g3", new Pawn("g3", white));
        board.put("g4", new Pawn("g4", white));
        board.put("g5", new Pawn("g5", white));
        board.put("g6", new Pawn("g6", white));
        board.put("g7", new Pawn("g7", white));
    }

    /**
     * Gets the reference of the board.
     *
     * @return The reference of the current board.
     */
    public HashMap<String, ChessPiece> getBoard(){
        return board;
    }

    /**
     * Get the piece in a board position.
     *
     * @param position String with the position.
     * @return The piece in the given position, or null if there isn't a piece there.
     */
    public ChessPiece getPiece(String position){
        return board.get(position);
    }

    /**
     * Get the piece in a board position.
     *
     * @param position Integer vector with the position.
     * @return The piece in the given position, or null if there isn't a piece there.
     */
    public ChessPiece getPiece(int[] position){
        return board.get(VectorPosition.convertVectorToStringPosition(position));
    }

    /**
     * Moves the piece in a given position to another given position.
     *
     * @param from      Integer vector with the current position of the piece.
     * @param to        Integer vector with the new position of the piece.
     * @param gamePanel Reference of the GamePanel.
     */
    public void movePiece(int[] from, int[] to, GamePanel gamePanel){
        ChessPiece piece = board.get(VectorPosition.convertVectorToStringPosition(from));
        piece = checkPromotion(piece, to, gamePanel);
        board.remove(VectorPosition.convertVectorToStringPosition(from));
        board.put(VectorPosition.convertVectorToStringPosition(to), piece);
    }

    /**
     * Gets the position of a given piece.
     *
     * @param piece A ChessPiece.
     * @return Integer vector with the position of the given piece, or null if the piece is not on the board or does not exist.
     */
    public int[] getPosition(ChessPiece piece){
        if(piece == null)
            return null;
        for(int i = 0; i < 8; i++)
            for(int k = 0; k < 8; k++){
                String checkingPiece = VectorPosition.convertVectorToStringPosition(new int[]{i, k});
                if(board.containsKey(checkingPiece) && board.get(checkingPiece) != null)
                    if(board.get(checkingPiece).equals(piece))
                        return new int[]{i, k};
            }
        return null;
    }

    /**
     * Gets all pieces of a given player color.
     *
     * @param color String representing the player color ("white" or "black").
     * @return ArrayList of ChessPiece with all pieces of the given player color.
     */
    public ArrayList<ChessPiece> getPiecesByColor(String color){
        ArrayList<ChessPiece> pieces = new ArrayList<>();
        for(int i = 0; i < 8; i++)
            for(int k = 0; k < 8; k++){
                ChessPiece piece = board.get(VectorPosition.convertVectorToStringPosition(new int[]{i, k}));
                if(piece != null)
                    if(piece.getPlayer().getColor().equals(color))
                        pieces.add(piece);
            }
        return pieces;
    }

    /**
     * Gets the King of a given player color.
     *
     * @param color String representing the color of a player ("white" or "black").
     * @return King of the specified player color.
     */
    public King getMyKing(String color){
        for(int i = 0; i < 8; i++)
            for(int k = 0; k < 8; k++){
                ChessPiece piece = board.get(VectorPosition.convertVectorToStringPosition(new int[]{i, k}));
                if(piece != null)
                    if(piece.getPlayer().getColor().equals(color) && piece instanceof King)
                        return (King) piece;
            }
        return null;
    }

    /**
     * Clones the current board, creating a copy of it.
     *
     * @return A clone (copy) of the current board.
     */
    public Board cloneBoard(){
        Board clonedBoard = new Board(false);

        for(int i = 0; i < 8; i++)
            for(int k = 0; k < 8; k++){
                int[] position = new int[]{i, k};
                ChessPiece piece = this.getPiece(position);
                if(piece != null)
                    clonedBoard.board.put(VectorPosition.convertVectorToStringPosition(position), piece);
            }
        return clonedBoard;
    }

    /**
     * Verify if there is a checkmate.
     *
     * @param isWhiteTurn If true, it's white's turn; if false, it's black's turn.
     * @return True if the King of the current player is in checkmate, false if it isn't.
     */
    public boolean checkMate(boolean isWhiteTurn){
        String color;
        if(isWhiteTurn)
            color = "white";
        else color = "black";

        ArrayList<ChessPiece> pieces = getPiecesByColor(color);
        for(ChessPiece piece : pieces)
            if(piece.getPossibleMoves(this, true) != null)
                return false;
        return true;
    }

    /**
     * Verify if a ChessPiece can be promoted.
     *
     * @param piece     A ChessPiece.
     * @param to        The ChessPiece's new position.
     * @param gamePanel Reference of GamePanel.
     * @return The new ChessPiece, or the given piece (if it can't be promoted).
     */
    public ChessPiece checkPromotion(ChessPiece piece, int[] to, GamePanel gamePanel){
        if(piece instanceof Pawn && gamePanel != null){
            int[] originalPos = VectorPosition.convertStringToVector(((Pawn) piece).originalPosition);
            if((originalPos[0] == 1 && to[0] == 7) || (originalPos[0] == 6 && to[0] == 0))
                return promote(piece, gamePanel);
        }
        return piece;
    }

    /**
     * Promotes a Pawn to another piece.
     *
     * @param piece     The Pawn that will be promoted.
     * @param gamePanel Reference of the GamePanel.
     * @return The new ChessPiece that the Pawn was promoted to.
     */
    private ChessPiece promote(ChessPiece piece, GamePanel gamePanel){
        return switch(gamePanel.showImageSelectionOverlay()){
            case "q" -> new Queen(gamePanel.isWhiteTurn()? white : black);
            case "b" -> new Bishop(gamePanel.isWhiteTurn()? white : black);
            case "r" -> new Rook(gamePanel.isWhiteTurn()? white : black);
            case "n" -> new Knight(gamePanel.isWhiteTurn()? white : black);
            case null, default -> piece;
        };
    }

    /**
     * Converts the board state to a String.
     *
     * @return String representing the current board state.
     */
    public String boardToString(){
        String boardString = "";
        for(int i = 0; i < 8; i++)
            for(int k = 0; k < 8; k++){
                String position = VectorPosition.convertVectorToStringPosition(new int[]{i, k});
                ChessPiece piece = board.get(position);
                boardString += position + pieceToString(piece) + "\n";
            }
        return boardString;
    }

    /**
     * Converts a piece to a String.
     *
     * @param piece A ChessPiece.
     * @return String representing the given piece.
     */
    private String pieceToString(ChessPiece piece){
        String pieceString = "";
        if(piece != null)
            if(piece.getPlayer().getColor().equals("white"))
                pieceString = "w";
            else pieceString = "b";

        switch(piece){
            case Bishop bishop -> pieceString += "b  ";
            case King king -> pieceString += "k  ";
            case Knight knight -> pieceString += "n  ";
            case Pawn pawn -> pieceString += "p" + pawn.originalPosition;
            case Queen queen -> pieceString += "q  ";
            case Rook rook -> pieceString += "r  ";
            case null, default -> pieceString = "    ";
        }
        return pieceString;
    }

    /**
     * Loads the board state written as a String.
     *
     * @param boardString String representing a board state.
     */
    public void stringToBoard(String boardString){
        board = new HashMap<>();
        for(String s : boardString.split("\n"))
            board.put(s.substring(0, 2), stringToPiece(s.substring(2)));
    }

    /**
     * Converts a String to a ChessPiece.
     *
     * @param pieceString String that represents a ChessPiece.
     * @return ChessPiece equivalent to the given String.
     */
    private ChessPiece stringToPiece(String pieceString){
        Player player;
        ChessPiece piece;
        if(pieceString.startsWith("w"))
            player = white;
        else player = black;

        piece = switch(pieceString.charAt(1)){
            case 'b' -> new Bishop(player);
            case 'k' -> new King(player);
            case 'n' -> new Knight(player);
            case 'p' -> new Pawn(pieceString.substring(2), player);
            case 'q' -> new Queen(player);
            case 'r' -> new Rook(player);
            default -> null;
        };
        return piece;
    }
}