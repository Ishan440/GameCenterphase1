package fall2018.csc2017.GameCenter;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * Checks whether Jorjani mode is activated
     */
    static boolean jorjani;

    /**
     * The number of rows.
     */
    static int numRows;

    /**
     * The number of cols.
     */
    static int numCols;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[numRows][numCols];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    Board(List<Tile> tiles) {
        Iterator<Tile> iter = tiles.iterator();
        for (int row = 0; row != Board.numRows; row++) {
            for (int col = 0; col != Board.numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Returns the 2d array of tiles of this board.
     *
     * @return this board's 2d array of tiles
     */
    private Tile[][] getTiles() {
        return tiles;
    }

    /**
     * A new board of tiles that copies the tiles of another given board.
     *
     * @param boardToBeCopied the board whose tiles are copied
     */
    Board copiedBoard(Board boardToBeCopied) {
        List<Integer> tileNums = new ArrayList<>();
        Tile[][] tilesToBeCopied = boardToBeCopied.getTiles();
        for (int row = 0; row != Board.numRows; row++) {
            for (int col = 0; col != Board.numCols; col++) {
                tileNums.add(tilesToBeCopied[row][col].getId() - 1);
            }
        }
        List<Tile> copiedTileList = new ArrayList<>();
        for (Integer tileNum : tileNums) {
            copiedTileList.add(new Tile(tileNum));
        }
        return new Board(copiedTileList);
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        return numRows * numCols;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new TileIterator();
    }

    /**
     * Iterates over the tiles of the board.
     */
    private class TileIterator implements Iterator<Tile> {

        /**
         * The index of the next tile to iterate over.
         */
        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex <= numTiles();
        }

        @Override
        public Tile next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                        String.format(
                                "Next tile index [%s .. %s] is out of range.",
                                nextIndex, numTiles()));
            }
            Tile t = tiles[nextIndex / numCols][nextIndex % numCols];
            nextIndex++;
            return t;
        }
    }
}
