package socialnetwork.domain;

import java.util.Optional;

public class BacklogClass implements Backlog {
  private FineSet<Task> taskFineSet;

  public BacklogClass() {
    taskFineSet = new FineSet<>();
  }

  @Override
  public boolean add(Task task) {
    return taskFineSet.add(task);
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    return taskFineSet.removeAndGetFirst();
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return taskFineSet.size();
  }
}
