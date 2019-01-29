package fall2018.csc2017.GameCenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 * Also keeps track of the number of moves made and the boards corresponding to each move.
 */
class BoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * The number of moves played so far in the current instance of the game.
     */
    private int numMoves = 0;

    /**
     * Returns the list of saved boards for this BoardManager.
     *
     * @return this BoardManager's saved boards
     */
    ArrayList<Board> getSavedBoards() {
        return savedBoards;
    }

    /**
     * The list of each board that is associated with each move
     */
    private ArrayList<Board> savedBoards = new ArrayList<>();

    /**
     * Returns the complexity of this BoardManager.
     *
     * @return this BoardManager's complexity.
     */
    int getComplexity() {
        return complexity;
    }

    /**
     * The complexity of the current board being managed (the number of tiles on a side).
     */
    private int complexity;

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     * Determines whether jorjani mode is activated and the complexity of the board being managed.
     */
    BoardManager(int complexity, boolean jorjani) {
        Board.jorjani = jorjani;
        Board.numRows = Board.numCols = complexity;
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.numRows * Board.numCols;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        Collections.shuffle(tiles);
        this.board = new Board(tiles);
        savedBoards.add(board.copiedBoard(board));
        this.complexity = complexity;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        Iterator<Tile> iter = board.iterator();
        int prevId = 0;
        for (int a = 0; a != board.numTiles(); a++) {
            Tile currentTile = iter.next();
            if (currentTile.getId() != prevId + 1) {
                return false;
            }
            prevId = currentTile.getId();
        }
        return true;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {
        int row = position / Board.numCols;
        int col = position % Board.numCols;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == Board.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == Board.numCols - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     * Updates savedBoards with the current board instance each move.
     * Increments numMoves by 1.
     *
     * @param position the position
     */
    void touchMove(int position) {
        int row = position / Board.numCols;
        int col = position % Board.numCols;
        int blankId = board.numTiles();
        int blankRow = 0;
        int blankCol = 0;
        Iterator<Tile> iter = board.iterator();
        for (int rowIndex = 0; rowIndex != Board.numRows; rowIndex++) {
            for (int colIndex = 0; colIndex != Board.numCols; colIndex++) {
                if (iter.next().getId() == blankId) {
                    blankRow = rowIndex;
                    blankCol = colIndex;
                }
            }
        }
        addToSavedBoards();
        board.swapTiles(row, col, blankRow, blankCol);
        numMoves += 1;
    }

    /**
     * Adds a copy of the current instance of board to the savedBoards list.
     */
    private void addToSavedBoards() {
        savedBoards.add(board.copiedBoard(board));
    }

    /**
     * Returns the number of moves made in this game so far, which corresponds to the user's
     * score at the end of the game.
     *
     * @return the number of moves made so far in the game
     */
    int getMoves() {
        return numMoves;
    }

    /**
     * Sets the board of this BoardManager.
     *
     * @param board the BoardManager's new Board
     */
    private void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the BoardManager's current board to one of its past boards.
     *
     * @param numTurns the number of turns that are undone
     */
    void undo(int numTurns) {
        for (int i = 1; i != savedBoards.size(); i++) {
            if (i == numTurns) {
                this.setBoard(savedBoards.get(savedBoards.size() - i));
                break;
            }
        }
        for (int i = 0; i != numTurns; i++) {
            savedBoards.remove(savedBoards.size() - 1);
        }
    }
}
