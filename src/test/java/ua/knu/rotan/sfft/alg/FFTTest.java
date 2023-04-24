package ua.knu.rotan.sfft.alg;

import static java.lang.Math.sin;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.knu.rotan.sfft.alg.FFT.*;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class FFTTest {

  public static final double EPS = 1e-2;

  private int complexEquals(Complex c1, Complex c2, double EPS) {
    return Math.abs(c1.magnitude() - c2.magnitude()) < EPS ? 0 : 1;
  }

  @Test
  public void testFFT() {
    Complex[] input = {
      new Complex(1, 0),
      new Complex(2, 0),
      new Complex(3, 0),
      new Complex(4, 0),
      new Complex(5, 0),
      new Complex(6, 0),
      new Complex(7, 0),
      new Complex(8, 0)
    };
    Complex[] expectedOutput = {
      new Complex(36, 0),
      new Complex(-4, 9.656),
      new Complex(-4, 4),
      new Complex(-4, 1.656),
      new Complex(-4, 0),
      new Complex(-4, -1.656),
      new Complex(-4, -4),
      new Complex(-4, -9.656)
    };

    Complex[] actualOutput = FFT.fft(input);

    assertThat(actualOutput)
        .usingElementComparator((c1, c2) -> complexEquals(c1, c2, 1e-2))
        .containsExactly(expectedOutput);
  }

  @Test
  public void testInverseFFT() {
    Complex[] input = {
      new Complex(36, 0),
      new Complex(-4, 9.656),
      new Complex(-4, 4),
      new Complex(-4, 1.656),
      new Complex(-4, 0),
      new Complex(-4, -1.656),
      new Complex(-4, -4),
      new Complex(-4, -9.656)
    };
    Complex[] expectedOutput = {
      new Complex(1, 0),
      new Complex(2, 0),
      new Complex(3, 0),
      new Complex(4, 0),
      new Complex(5, 0),
      new Complex(6, 0),
      new Complex(7, 0),
      new Complex(8, 0)
    };

    assertThat(FFT.ifft(input))
        .usingElementComparator((c1, c2) -> complexEquals(c1, c2, EPS))
        .containsExactly(expectedOutput);
  }

  @Test
  void testInverseOfDirectOfSequenceEqualsSequence() {
    Complex[] sampledSequence =
        IntStream.range(0, 1 << 10)
            .mapToDouble(i -> 4 * sin(i) + 6 * 4 * sin(3 * i))
            .mapToObj(Complex::fromReal)
            .toArray(Complex[]::new);

    assertThat(ifft(fft(sampledSequence)))
        .usingElementComparator((c1, c2) -> complexEquals(c1, c2, EPS))
        .containsExactly(sampledSequence);
  }
}
