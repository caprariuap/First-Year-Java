package socialnetwork;

import socialnetwork.domain.Board;
import socialnetwork.domain.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class User extends Thread {

  private static final AtomicInteger nextId = new AtomicInteger(0);

  protected final SocialNetwork socialNetwork;
  private final int id;
  private final String name;

  public User(String username, SocialNetwork socialNetwork) {
    this.name = username;
    this.id = User.nextId.getAndIncrement();
    this.socialNetwork = socialNetwork;
  }

  public int getUserId() {
    return id;
  }

  @Override
  public void run() {
    // Implement here
    Random generator = new Random();
    // Executes one or more of the 3 actions mentioned in the spec sheet
    if (generator.nextInt(100) < 70) {
      socialNetwork.getAllUsers();
    }
    if (generator.nextInt(100) < 50) {
      Set<User> possibleRecipients = socialNetwork.getAllUsers();
      Set<User> recipients = ConcurrentHashMap.newKeySet();
      for (User user : possibleRecipients) {
        if (generator.nextInt(100) < 40) {
          recipients.add(user);
        }
      }
      socialNetwork.postMessage(
          this, recipients, toString() + "sends a message to " + recipients.size() + " users.");
    }
    if (generator.nextInt(100) < 20) {
      Board board = socialNetwork.userBoard(this);
      List<Message> messagesToDelete = new LinkedList<>();
      for (Message message : board.getBoardSnapshot()) {
        if (generator.nextInt(100) < 0 && message.getSender() == this) {
          messagesToDelete.add(message);
        }
      }
      for (Message message : messagesToDelete) {
        socialNetwork.deleteMessage(message);
      }
    }
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public int hashCode() {
    return id;
  }
}
