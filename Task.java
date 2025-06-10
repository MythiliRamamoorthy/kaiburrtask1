import java.util.ArrayList;
import java.util.List;

public class Task {
    public String id;
    public String name;
    public String owner;
    public String command;
    public List<TaskExecution> taskExecutions = new ArrayList<>();

    public Task(String id, String name, String owner, String command) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.command = command;
    }
}
