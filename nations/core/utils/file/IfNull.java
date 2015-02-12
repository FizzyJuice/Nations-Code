package nations.core.utils.file;

public enum IfNull {

    CREATE("Creates file if it's null"),
    IGNORE("Ignores file if it's null");

    private String description;

    private IfNull(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
