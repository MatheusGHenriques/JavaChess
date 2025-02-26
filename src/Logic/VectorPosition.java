package Logic;

/**
 * Class that contains static methods to manipulate positions of the chess pieces.
 */
public class VectorPosition{
    /**
     * Sums two integer vectors.
     *
     * @param vec1 Integer vector of 2 elements.
     * @param vec2 Integer vector of 2 elements.
     * @return Integer vector of 2 elements, sum of the parameters.
     */
    public static int[] vectorSum(int[] vec1, int[] vec2){
        return new int[]{vec1[0] + vec2[0], vec1[1] + vec2[1]};
    }

    /**
     * Multiplies an integer vector by a integer.
     *
     * @param vec Integer vector of 2 elements.
     * @param a   Integer to multiply the vector by.
     * @return Integer vector of 2 elements, multiplication of the parameters.
     */
    public static int[] vectorMultiplication(int[] vec, int a){
        return new int[]{vec[0] * a, vec[1] * a};
    }

    /**
     * Verifies if the position given is within board bounds.
     *
     * @param vec1 Integer vector of 2 elements representing the position.
     * @return True if the position given is within board bounds, false if it's not.
     */
    public static boolean checkBoardBounds(int[] vec1){
        return !(vec1[0] > 7 || vec1[1] > 7 || vec1[0] < 0 || vec1[1] < 0);
    }

    /**
     * Verifies if the position given is within board bounds.
     *
     * @param position String representing a position.
     * @return True if the position given is within board bounds, false if it's not.
     */
    public static boolean checkBoardBounds(String position){
        return position.charAt(0) >= 'a' && position.charAt(0) <= 'h' && position.charAt(1) >= '0' && position.charAt(1) <= '7';
    }

    /**
     * Converts an integer vector representing a position to a String.
     *
     * @param vec Integer vector representing a position.
     * @return String representing the same position as the parameter.
     */
    public static String convertVectorToStringPosition(int[] vec){
        String s = Character.toString((char) ('a' + vec[0]));
        return s + (vec[1]);
    }

    /**
     * Converts a String representing a position to a integer vector.
     *
     * @param position String representing a position.
     * @return Integer vector representing the same position as the parameter.
     */
    public static int[] convertStringToVector(String position){
        return new int[]{((int) position.charAt(0) - 'a'), Integer.parseInt(String.valueOf(position.charAt(1)))};
    }
}