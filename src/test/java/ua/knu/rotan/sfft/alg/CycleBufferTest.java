package ua.knu.rotan.sfft.alg;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CycleBufferTest {

  @Test
  public void testGetWithValidOffsetAndLength() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3, 4, 5};
    buffer.addAllInEnd(elements, elements.length);

    int[] result = buffer.get(2, 3);

    assertThat(result).containsExactly(3, 4, 5);
  }

  @Test
  public void testGetWithInvalidOffset() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3, 4, 5};
    buffer.addAllInEnd(elements, elements.length);

    assertThatThrownBy(() -> buffer.get(6, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Not enough data in buffer.");
  }

  @Test
  public void testGetWithDeletedData() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3, 4, 5};
    buffer.addAllInEnd(elements, elements.length);
    buffer.addAllInEnd(elements, 2); // Add additional elements to shift the buffer

    assertThatThrownBy(() -> buffer.get(0, 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Data was deleted from buffer");
  }

  @Test
  public void testAddAllInEndWithValidElements() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3};

    buffer.addAllInEnd(elements, elements.length);

    assertThat(buffer.totalElementsAdded()).isEqualTo(3);
  }

  @Test
  public void testAddAllInEndWithInvalidLength() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3, 4, 5, 6};

    assertThatThrownBy(() -> buffer.addAllInEnd(elements, elements.length))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Not enough space in buffer.");
  }

  @Test
  public void testTotalElementsAdded() {
    CycleBuffer buffer = new CycleBuffer(5);
    int[] elements = {1, 2, 3, 4, 5};

    buffer.addAllInEnd(elements, elements.length);

    assertThat(buffer.totalElementsAdded()).isEqualTo(5);
  }
}
