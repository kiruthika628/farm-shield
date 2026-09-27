import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an automatically generated biosecurity risk alert for a Farm.
 */
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Severity {
        LOW, MEDIUM, HIGH
    }

    private String farmId;
    private String farmName;
    private String message;
    private Severity severity;
    private String timestamp;

    public Alert(String farmId, String farmName, String message, Severity severity) {
        this.farmId = farmId;
        this.farmName = farmName;
        this.message = message;
        this.severity = severity;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFarmId() {
        return farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] (%s) Farm: %-15s | Severity: %-6s | %s",
                timestamp, farmId, farmName, severity, message);
    }
}
