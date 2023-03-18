package ua.knu.rotan.sfft.alg;

public class CycleBuffer<T> {
  private final T[] array;
  private int shift;
  private final int size;

  public CycleBuffer(T[] array) {
    this.array = array;
    this.shift = 0;
    this.size = array.length;
  }

  public T get(int i) {
    return array[(shift + i) % shift];
  }

  public void addInEnd(T element) {
    array[shift] = element;
    shift = (shift + 1) % size;
  }
}
