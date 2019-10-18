package socialnetwork.domain;

import java.util.Optional;

public class Worker extends Thread {

  private final Backlog backlog;
  private boolean interrupted = false;

  public Worker(Backlog backlog) {
    this.backlog = backlog;
  }

  @Override
  public void run() {
    while (!interrupted) {
      // implement here
      Optional<Task> task = backlog.getNextTaskToProcess();
      if (task.isPresent()) {
        process(task.get());
      } else {
        try {
          sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void interrupt() {
    this.interrupted = true;
  }

  public void process(Task nextTask) {
    if (nextTask.command == Task.Command.POST) {
      assert nextTask.board.addMessage(nextTask.message) : "Message wasn't added";
      return;
    }
    if (!nextTask.board.deleteMessage(nextTask.message)) {
      backlog.add(nextTask);
    }
  }
}
