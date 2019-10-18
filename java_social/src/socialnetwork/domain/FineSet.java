package socialnetwork.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class FineSet<T> {

  private AtomicInteger size = new AtomicInteger(0);
  private LockableNode<T> head, tail;

  public FineSet() {
    head = new LockableNode<>(null, null, Integer.MIN_VALUE);
    tail = new LockableNode<>(null, null, Integer.MAX_VALUE);
    head.setNext(tail);
  }

  public boolean add(T value) {
    LockableNode<T> node = new LockableNode<T>(value, value.hashCode());
    LockableNode<T> pred = null, curr = null;
    try {
      Pair<LockableNode<T>> position = find(head, node.getId());
      pred = position.first;
      curr = position.second;
      if (curr.getId() == node.getId()) {
        return false;
      } else {
        pred.setNext(node);
        node.setNext(curr);
        size.getAndIncrement();
        return true;
      }
    } finally {
      pred.unlock();
      curr.unlock();
    }
  }

  public boolean delete(T value) {
    LockableNode<T> pred = null, curr = null;
    try {
      Pair<LockableNode<T>> position = find(head, value.hashCode());
      pred = position.first;
      curr = position.second;
      if (curr.getId() > value.hashCode()) {
        return false;
      } else {
        pred.setNext(curr.getNext());
        size.getAndDecrement();
        return true;
      }
    } finally {
      pred.unlock();
      curr.unlock();
    }
  }

  public Pair<LockableNode<T>> find(LockableNode<T> start, int key) {
    LockableNode<T> pred, curr;
    pred = start;
    pred.lock();
    curr = pred.getNext();
    curr.lock();
    while (curr.getId() < key) {
      pred.unlock();
      pred = curr;
      curr = curr.getNext();
      curr.lock();
    }
    return new Pair<>(pred, curr);
  }

  public int size() {
    return size.get();
  }

  public List<T> getSnapshot() {
    List<T> snapshot = new LinkedList<>();
    LockableNode<T> pred, curr;
    pred = head;
    pred.lock();
    curr = pred.getNext();
    curr.lock();
    while (curr.getId() < tail.getId()) {
      ((LinkedList<T>) snapshot).addFirst(curr.getValue());
      pred.unlock();
      pred = curr;
      curr = curr.getNext();
      curr.lock();
    }
    pred.unlock();
    curr.unlock();
    return snapshot;
  }

  public Optional<T> removeAndGetFirst() {
    LockableNode<T> next = null;
    try {
      head.lock();
      next = head.getNext();
      next.lock();
      if (next.getValue() == null) {
        return Optional.empty();
      }
      head.setNext(next.getNext());
      size.getAndDecrement();
      return Optional.of(next.getValue());
    } finally {
      head.unlock();
      next.unlock();
    }
  }

  private class Pair<T> {
    public final T first, second;

    Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }
  }
}
