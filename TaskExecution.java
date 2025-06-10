import java.util.Date;

public class TaskExecution {
    public Date startTime;
    public Date endTime;
    public String output;

    public TaskExecution(Date startTime, Date endTime, String output) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.output = output;
    }
}
