package passoff.chess.piece;

import chess.ChessPosition;
import org.junit.jupiter.api.Test;

import static passoff.chess.TestUtilities.validateMoves;

public class KingMoveTests {

    @Test
    public void kingMoveUntilEdge() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | |K| | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new ChessPosition(3, 6),
                new int[][]{{4, 6}, {4, 7}, {3, 7}, {2, 7}, {2, 6}, {2, 5}, {3, 5}, {4, 5}}
        );
    }


    @Test
    public void kingCaptureEnemy() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |N|n| | | |
                        | | | |k| | | | |
                        | | |P|b|p| | | |
                        | | | | | | | | |
                        """,
                new ChessPosition(3, 4),
                new int[][]{{4, 4}, {3, 5}, {2, 3}, {3, 3}, {4, 3}}
        );
    }


    @Test
    public void kingBlocked() {
        validateMoves("""
                        | | | | | | |r|k|
                        | | | | | | |p|p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                new ChessPosition(8, 8),
                new int[][]{}
        );
    }
//
//    @Test
//    public void myOwnKingMove() {
//        validateMoves("""
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | | | | | | | | |
//                        | |K| | | | | | |
//                        """,
//                new ChessPosition(1, 2),
//                new int[][]{{1, 1}, {2, 1}, {2, 2}, {2, 3}, {1, 3}}
//        );
//    }


}





