package com.project.tysamurai.projectmusic20;

import java.util.concurrent.TimeUnit;

/**
 * Created by tanay on 11/7/17.
 */

public class Utls {
    public String timeFormatter(Integer millis){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return hms;
    }
}
