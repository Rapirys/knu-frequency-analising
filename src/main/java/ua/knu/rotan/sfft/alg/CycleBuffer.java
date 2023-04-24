package ua.knu.rotan.sfft.alg;

public class CycleBuffer {
  private final int[] array;
  private int shift;
  private final int size;

  public CycleBuffer(int size) {
    this.array = new int[size];
    this.shift = 0;
    this.size = array.length;
  }

  public synchronized int[] get(int offset, int length) {
    if (offset + length > shift) throw new IndexOutOfBoundsException("Not enough data in buffer.");
    if (offset < shift - size) throw new IndexOutOfBoundsException("Data was deleted from buffer");

    int[] result = new int[length];
    int start = offset % size;
    int end = (offset + length) % size;
    if (end > start) {
      System.arraycopy(array, start, result, 0, length);
    } else {
      int rightLen = size - start;
      System.arraycopy(array, start, result, 0, rightLen);
      System.arraycopy(array, 0, result, rightLen, end);
    }
    return result;
  }

  public synchronized void addAllInEnd(int[] elements, int length) {
    if (length > size) throw new IndexOutOfBoundsException("Not enough space in buffer.");

    int start = shift % size;
    if (start + length > size) {
      int rightLen = size - start;
      System.arraycopy(elements, 0, array, start, rightLen);
      System.arraycopy(elements, rightLen, array, 0, length - rightLen);
    } else {
      System.arraycopy(elements, 0, array, start, length);
    }
    shift += length;
  }

  public synchronized int totalElementsAdded() {
    return shift;
  }
}
