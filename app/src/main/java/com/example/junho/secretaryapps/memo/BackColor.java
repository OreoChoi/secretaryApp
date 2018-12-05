package com.example.junho.secretaryapps.memo;

import android.graphics.Color;

public enum BackColor {
    BLACK, WHITE, PALE_GREEN, YELLOW, PALE_YELLOW,
    APRICOT, PINK, PURPLE, PALE_BLUE, CYAN, GRAY, ORANGE;

    private final int BLACK_INT = Color.rgb(0, 0, 0),
            WHITE_INT = Color.rgb(255, 255, 255),
            PALE_GREEN_INT = Color.rgb(134, 229, 126),
            YELLOW_INT = Color.rgb(250, 237, 125),
            PALE_YELLOW_INT = Color.rgb(244, 244, 192),
            APRICOT_INT = Color.rgb(255, 167, 167),
            PINK_INT = Color.rgb(243, 97, 166),
            PURPLE_INT = Color.rgb(217, 65, 197),
            PALE_BLUE_INT = Color.rgb(178, 235, 244),
            CYAN_INT = Color.rgb(0, 153, 153),
            GRAY_INT = Color.rgb(234, 234, 234),
            ORANGE_INT = Color.rgb(255, 130, 36);

    public int getBackColor(BackColor backColor) {
        switch (backColor) {
            case BLACK:
                return BLACK_INT;
            case WHITE:
                return WHITE_INT;
            case PALE_GREEN:
                return PALE_GREEN_INT;
            case YELLOW:
                return YELLOW_INT;
            case PALE_YELLOW:
                return PALE_YELLOW_INT;
            case APRICOT:
                return APRICOT_INT;
            case PINK:
                return PINK_INT;
            case PURPLE:
                return PURPLE_INT;
            case PALE_BLUE:
                return PALE_BLUE_INT;
            case CYAN:
                return CYAN_INT;
            case GRAY:
                return GRAY_INT;
            case ORANGE:
                return ORANGE_INT;
            default:
                return 0;
        }
    }
}
