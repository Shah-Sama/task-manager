public class Task {
    private String id;
    private String title;
    private boolean completed;

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
        this.completed = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
