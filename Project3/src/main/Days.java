package main;

import java.util.EnumSet;

public enum Days {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY;

    public static final EnumSet<Days> EVERYDAY = EnumSet.allOf(Days.class);
}
