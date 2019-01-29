package fall2018.csc2017.GameCenter;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Comparable<Tile>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public Tile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId
     */
    public Tile(int backgroundId) {
        id = backgroundId + 1;
        // This looks so ugly.
        switch (backgroundId + 1) {
            case 1:
                if (!Board.jorjani) {
                    background = R.drawable.tile_1;
                } else {
                    background = R.drawable.row1col1;
                }
                break;
            case 2:
                if (!Board.jorjani) {
                    background = R.drawable.tile_2;
                } else {
                    background = R.drawable.row1col2;
                }
                break;
            case 3:
                if (!Board.jorjani) {
                    background = R.drawable.tile_3;
                } else {
                    background = R.drawable.row1col3;
                }
                break;
            case 4:
                if (!Board.jorjani) {
                    background = R.drawable.tile_4;
                } else {
                    background = R.drawable.row1col4;
                }
                break;
            case 5:
                if (!Board.jorjani) {
                    background = R.drawable.tile_5;
                } else {
                    background = R.drawable.row1col5;
                }
                break;
            case 6:
                if (!Board.jorjani) {
                    background = R.drawable.tile_6;
                } else {
                    background = R.drawable.row2col1;
                }
                break;
            case 7:
                if (!Board.jorjani) {
                    background = R.drawable.tile_7;
                } else {
                    background = R.drawable.row2col2;
                }
                break;
            case 8:
                if (!Board.jorjani) {
                    background = R.drawable.tile_8;
                } else {
                    background = R.drawable.row2col3;
                }
                break;
            case 9:
                if (Board.numRows == 3) {
                    background = R.drawable.blank;
                } else {
                    if (!Board.jorjani) {
                        background = R.drawable.tile_9;
                    } else {
                        background = R.drawable.row2col4;
                    }
                    break;
                }
                break;
            case 10:
                if (!Board.jorjani) {
                    background = R.drawable.tile_10;
                } else {
                    background = R.drawable.row2col5;
                }
                break;
            case 11:
                if (!Board.jorjani) {
                    background = R.drawable.tile_11;
                } else {
                    background = R.drawable.row3col1;
                }
                break;
            case 12:
                if (!Board.jorjani) {
                    background = R.drawable.tile_12;
                } else {
                    background = R.drawable.row3col2;
                }
                break;
            case 13:
                if (!Board.jorjani) {
                    background = R.drawable.tile_13;
                } else {
                    background = R.drawable.row3col3;
                }
                break;
            case 14:
                if (!Board.jorjani) {
                    background = R.drawable.tile_14;
                } else {
                    background = R.drawable.row3col4;
                }
                break;
            case 15:
                if (!Board.jorjani) {
                    background = R.drawable.tile_15;
                } else {
                    background = R.drawable.row3col5;
                }
                break;
            case 16:
                if (Board.numRows == 4) {
                    background = R.drawable.blank;
                } else {
                    if (!Board.jorjani) {
                        background = R.drawable.tile_16_real;
                    } else {
                        background = R.drawable.row4col1;
                    }
                    break;
                }
                break;
            case 17:
                if (!Board.jorjani) {
                    background = R.drawable.tile_17;
                } else {
                    background = R.drawable.row4col2;
                }
                break;
            case 18:
                if (!Board.jorjani) {
                    background = R.drawable.tile_18;
                } else {
                    background = R.drawable.row4col3;
                }
                break;
            case 19:
                if (!Board.jorjani) {
                    background = R.drawable.tile_19;
                } else {
                    background = R.drawable.row4col4;
                }
                break;
            case 20:
                if (!Board.jorjani) {
                    background = R.drawable.tile_20;
                } else {
                    background = R.drawable.row4col4;
                }
                break;
            case 21:
                if (!Board.jorjani) {
                    background = R.drawable.tile_21;
                } else {
                    background = R.drawable.row4col5;
                }
                break;
            case 22:
                if (!Board.jorjani) {
                    background = R.drawable.tile_22;
                } else {
                    background = R.drawable.row5col1;
                }
                break;
            case 23:
                if (!Board.jorjani) {
                    background = R.drawable.tile_23;
                } else {
                    background = R.drawable.row5col2;
                }
                break;
            case 24:
                if (!Board.jorjani) {
                    background = R.drawable.tile_24;
                } else {
                    background = R.drawable.row5col3;
                }
                break;
            case 25:
                background = R.drawable.blank;
                break;
            default:
                background = R.drawable.blank;
        }
    }

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull Tile o) {
        return o.id - this.id;
    }
}
