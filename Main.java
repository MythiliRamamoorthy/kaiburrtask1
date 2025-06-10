import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;

public class Main {
    static MongoCollection<Document> collection;

    public static void main(String[] args) throws Exception {
        MongoDatabase db = Database.getDatabase();
        collection = db.getCollection("tasks");

        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. Create Task\n2. Get All Tasks\n3. Search Task by Name\n4. Delete Task by ID\n5. Execute Task");
        System.out.print("Choose an option: ");
        int option = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (option) {
            case 1 -> createTask(sc);
            case 2 -> getAllTasks();
            case 3 -> searchByName(sc);
            case 4 -> deleteTask(sc);
            case 5 -> runTask(sc);
            default -> System.out.println("Invalid choice.");
        }
    }

    static void createTask(Scanner sc) {
        System.out.print("Enter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Owner: ");
        String owner = sc.nextLine();
        System.out.print("Enter Command: ");
        String command = sc.nextLine();

        if (!Validation.isCommandSafe(command)) {
            System.out.println("❌ Unsafe or invalid command. Try 'echo Hello'");
            return;
        }

        Document task = new Document("id", id)
                .append("name", name)
                .append("owner", owner)
                .append("command", command)
                .append("taskExecutions", new ArrayList<>());
        collection.insertOne(task);
        System.out.println("[✓] Task created.");
    }

    static void getAllTasks() {
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

    static void searchByName(Scanner sc) {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine();
        List<Document> results = collection.find(new Document("name", new Document("$regex", name))).into(new ArrayList<>());
        if (results.isEmpty()) {
            System.out.println("❌ No tasks found.");
        } else {
            results.forEach(doc -> System.out.println(doc.toJson()));
        }
    }

    static void deleteTask(Scanner sc) {
        System.out.print("Enter ID to delete: ");
        String id = sc.nextLine();
        long count = collection.deleteOne(new Document("id", id)).getDeletedCount();
        if (count > 0) System.out.println("[✓] Task deleted.");
        else System.out.println("❌ Task not found.");
    }

    static void runTask(Scanner sc) throws Exception {
        System.out.print("Enter task ID: ");
        String id = sc.nextLine();
        Document task = collection.find(new Document("id", id)).first();

        if (task == null) {
            System.out.println("❌ Task not found.");
            return;
        }

        String command = task.getString("command");
        System.out.println("🔍 Command: " + command);

        Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
        p.waitFor();

        // ✅ Fixed escape character
        Scanner outputScanner = new Scanner(p.getInputStream()).useDelimiter("\\A");
        String output = outputScanner.hasNext() ? outputScanner.next() : "";

        Scanner errorScanner = new Scanner(p.getErrorStream()).useDelimiter("\\A");
        String error = errorScanner.hasNext() ? errorScanner.next() : "";

        System.out.println("[✓] Task executed.");
        System.out.println("Output:\n" + output);
        if (!error.isEmpty()) {
            System.out.println("⚠️ Error:\n" + error);
        }
    }
}
