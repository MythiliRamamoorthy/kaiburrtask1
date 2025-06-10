public class Validation {
    public static boolean isCommandSafe(String command) {
        return command != null && command.trim().toLowerCase().startsWith("echo ");
    }
}
