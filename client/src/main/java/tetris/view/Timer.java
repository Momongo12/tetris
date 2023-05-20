package tetris.view;

public class Timer {
    private long startTime;
    private boolean timerIsRunning;

    public Timer() {
        startTime = 0;
        timerIsRunning = false;
    }

    public void startTime(){
        startTime = System.currentTimeMillis();
        timerIsRunning = true;
    }

    public String getCurrentTime(){
        StringBuilder stringBuilder = new StringBuilder();
        if (timerIsRunning){
            long curTimeInMillisecond = System.currentTimeMillis() - startTime;
            int hours = (int) curTimeInMillisecond / (3600 * 1000);
            int minutes = (int) (curTimeInMillisecond - (hours * 3600 * 1000)) / (60 * 1000);
            int seconds = (int) (curTimeInMillisecond - (minutes * 60 * 1000)) / 1000;
            stringBuilder.append(hours).append(":").append(minutes).append(":").append(seconds);
        }else {
            stringBuilder.append("00:00:00");
        }
        return stringBuilder.toString();
    }


}
