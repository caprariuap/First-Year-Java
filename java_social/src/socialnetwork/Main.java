package socialnetwork;

import socialnetwork.domain.Backlog;
import socialnetwork.domain.BacklogClass;
import socialnetwork.domain.BoardClass;
import socialnetwork.domain.Worker;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    // Implement logic here following the steps described in the specs
    Backlog backlog = new BacklogClass();
    SocialNetwork socialNetwork = new SocialNetwork(backlog);
    List<Worker> workers = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      workers.add(new Worker(backlog));
    }
    for (Worker worker : workers) {
      worker.start();
    }
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      users.add(new User("User" + i, socialNetwork));
    }
    for (User user : users) {
      socialNetwork.register(user, new BoardClass());
      user.start();
    }
    for (User user : users) {
      try {
        user.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(backlog.numberOfTasksInTheBacklog());
    while (backlog.numberOfTasksInTheBacklog() != 0) {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (Worker worker : workers) {
      worker.interrupt();
    }
    for (Worker worker : workers) {
      try {
        worker.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Simulation ended");
  }
}
