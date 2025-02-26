package Files;

import GUI.GamePanel;
import GUI.MenuPanel;
import Logic.Board;

import java.io.*;

/**
 * Manipulates the text file javaChess.txt.
 */
public class MatchSaveAndPreferences{
    private static final String FILE = File.separator + "javaChess" + File.separator + "javaChess.txt";

    private static String getFilePath(){
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win"))
            return System.getenv("APPDATA") + FILE;
        else if(os.contains("nix") || os.contains("nux") || os.contains("aix") || os.contains("mac"))
            return System.getProperty("user.home") + File.separator + ".config" + FILE;
        else
            return System.getProperty("java.io.tmpdir") + FILE;
    }

    /**
     * Save the current game state and the user's preferences in the file.
     *
     * @param board     Reference of the current board.
     * @param gamePanel Reference of the game panel.
     */
    public static void saveGame(Board board, GamePanel gamePanel){
        File file = new File(getFilePath());

        try{
            file.getParentFile().mkdirs();
        }catch(SecurityException e){
            System.err.println("Error creating directory: " + e.getMessage());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(board.boardToString());

            writer.write("#Timers\n");
            writer.write("w " + gamePanel.getWhiteTimeRemaining() + " \n");
            writer.write("b " + gamePanel.getBlackTimeRemaining() + " \n");

            writer.write(gamePanel.isWhiteTurn()? "true\n" : "false\n");
            writer.write(gamePanel.isVisualAssists()? "true\n" : "false\n");
            writer.write(gamePanel.isColorContrast()? "true\n" : "false\n");
            writer.write(gamePanel.getChessApp().isLightMode()? "true\n" : "false\n");

        }catch(IOException e){
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    /**
     * Load the saved game and user's preferences.
     *
     * @param gamePanel Reference of the game panel.
     * @param menuPanel Reference of the menu panel.
     * @return String representing the saved board state.
     */
    public static String loadGame(GamePanel gamePanel, MenuPanel menuPanel){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(getFilePath()));
            StringBuilder boardBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                if(line.equals("#Timers"))
                    break;
                boardBuilder.append(line).append("\n");
            }

            while((line = reader.readLine()) != null){
                if(line.startsWith("w"))
                    gamePanel.setWhiteTimeRemaining(Integer.parseInt(line.split(" ")[1]));
                else if(line.startsWith("b"))
                    gamePanel.setBlackTimeRemaining(Integer.parseInt(line.split(" ")[1]));
                else{
                    gamePanel.setWhiteTurn(Boolean.parseBoolean(line));
                    line = reader.readLine();
                    gamePanel.setVisualAssists(Boolean.parseBoolean(line));
                    line = reader.readLine();
                    gamePanel.setColorContrast(Boolean.parseBoolean(line));
                    line = reader.readLine();
                    gamePanel.getChessApp().setLightMode(Boolean.parseBoolean(line));
                }
            }

            menuPanel.enableContinueButton();
            return boardBuilder.toString();
        }catch(IOException e){
            menuPanel.disableContinueButton();
            return null;
        }
    }
}
