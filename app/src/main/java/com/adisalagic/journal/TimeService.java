package com.adisalagic.journal;

public class TimeService {
    private int hours = 0, minutes = 0, seconds = 0;
    private int totalHours;
    private long miliseconds = 0;

    public TimeService(int hours, int minutes, int seconds, int miliseconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.miliseconds = miliseconds;
        this.totalHours = hours;
        correctTime();
    }

    public TimeService(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.miliseconds = 0;
        this.totalHours = hours;
        correctTime();
    }

    public TimeService(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = 0;
        this.miliseconds = 0;
        this.totalHours = hours;
        correctTime();
    }

    public TimeService(int hours) {
        this.hours = hours;
        this.minutes = 0;
        this.seconds = 0;
        this.miliseconds = 0;
        this.totalHours = hours;
        correctTime();
    }

    TimeService() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.miliseconds = 0;
        this.totalHours = this.hours;
        correctTime();
    }

    public void setCurrentTime(){
        miliseconds = System.currentTimeMillis();
        correctTime();
    }

    private boolean checkTime() {
        return hours >= 0 && minutes >= 0 && seconds >= 0 && miliseconds >= 0
                && hours <= 23 && minutes < 60 && seconds < 60 && miliseconds < 1000;
    }

    private void correctTime() {
        while (!checkTime()) {
            if (hours >= 24) {
                hours -= 24;
            }
            if (minutes >= 60) {
                minutes -= 60;
                hours++;
            }
            if (seconds >= 60) {
                seconds -= 60;
                minutes++;
            }
            if (miliseconds >= 1000) {
                miliseconds -= 1000;
                seconds++;
            }
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public long getMiliseconds() {
        return miliseconds;
    }

    public void setMiliseconds(int miliseconds) {
        this.miliseconds = miliseconds;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public String toString(boolean onlyHoursAndMinutes) {
        String h = hours + "", m = minutes + "", s = seconds + "", ms = miliseconds + "";
        if (hours < 10) {
            h = "0" + hours;
        }
        if (seconds < 10) {
            s = "0" + seconds;
        }
        if (minutes < 10) {
            m = "0" + minutes;
        }
        if (miliseconds < 10){
            ms = "00" + miliseconds;
        }else if (miliseconds < 100) {
            ms = "0" + miliseconds;
        }

        if (onlyHoursAndMinutes) {
            return h + ":" + m;
        }else {
            return h + ":" + m + ":" + s + "." + ms;
        }
    }

    public int toMiliseconds() {
        return hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000 + (int) miliseconds;
    }

    public TimeService(String time){
        char[] chrs = time.toCharArray();
        try {
            hours = (int) chrs[0] * 10 + (int) chrs[1];
            minutes = (int) chrs[3] * 10 + (int) chrs[4];
        }catch (Exception ignored){}
    }
}
