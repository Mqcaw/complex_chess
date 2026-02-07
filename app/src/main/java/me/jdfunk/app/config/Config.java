package me.jdfunk.app.config;

import java.awt.*;

//TODO: make a config loader
public class Config {
    public static final String TITLE = "Chess";
    public static final int BOARD_SIZE_IN_SQUARES = 8;
    public static final int BOARD_TILE_SIZE_IN_PIXELS = 96;
    public static final int BOARD_SIZE_IN_PIXELS = BOARD_SIZE_IN_SQUARES * BOARD_TILE_SIZE_IN_PIXELS;
    public static final String BOARD_THEME = "brown";
    public static final String PIECE_SET = "maestro";

    public static final Color MOVE_OPTION_COLOR = new Color(15, 81, 35, 124);
    public static final Color HOVER_COLOR = new Color(15, 81, 35, 124);

    public static final int MOVE_OPTION_RADIUS = BOARD_TILE_SIZE_IN_PIXELS / 7;
}
