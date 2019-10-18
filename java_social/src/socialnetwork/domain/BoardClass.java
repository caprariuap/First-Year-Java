package socialnetwork.domain;

import java.util.List;

public class BoardClass implements Board {
  private FineSet<Message> messageFineSet;

  public BoardClass() {
    messageFineSet = new FineSet<>();
  }

  @Override
  public boolean addMessage(Message message) {
    return messageFineSet.add(message);
  }

  @Override
  public boolean deleteMessage(Message message) {
    return messageFineSet.delete(message);
  }

  @Override
  public int size() {
    return messageFineSet.size();
  }

  @Override
  public List<Message> getBoardSnapshot() {
    return messageFineSet.getSnapshot();
  }
}
