package socialnetwork.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableNode<T> {
  private Lock lock = new ReentrantLock();
  private T value;
  private int id;
  private LockableNode<T> next;

  public LockableNode(T value, int id) {
    this(value, null, id);
  }

  public LockableNode(T value, LockableNode<T> next, int id) {
    this.value = value;
    this.next = next;
    this.id = id;
  }

  public T getValue() {
    return value;
  }

  public int getId() {
    return id;
  }

  public LockableNode<T> getNext() {
    return next;
  }

  public void setNext(LockableNode<T> next) {
    this.next = next;
  }

  public void lock() {
    lock.lock();
  }

  public void unlock() {
    lock.unlock();
  }
}
