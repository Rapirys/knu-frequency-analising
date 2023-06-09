package ua.knu.rotan.sfft.alg;

import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FFT {
  public static Complex[] ifft(Complex[] x) {
    int n = x.length;
    return Arrays.stream(fft(Arrays.stream(x).map(Complex::conjugate).toArray(Complex[]::new)))
        .map(Complex::conjugate)
        .map(c -> c.scale(1.0 / n))
        .toArray(Complex[]::new);
  }

  public static Complex[] fft(Complex[] x) {
    int n = x.length;

    // base case
    if (n == 1) {
      return new Complex[] {x[0]};
    }

    // split input into even and odd parts
    Complex[] even = new Complex[n / 2];
    Complex[] odd = new Complex[n / 2];
    for (int i = 0; i < n / 2; i++) {
      even[i] = x[2 * i];
      odd[i] = x[2 * i + 1];
    }

    // recursively compute FFT of even and odd parts
    Complex[] q = fft(even);
    Complex[] r = fft(odd);

    // combine results
    Complex[] y = new Complex[n];
    for (int i = 0; i < n / 2; i++) {
      double kth = -2 * i * Math.PI / n;
      Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
      y[i] = q[i].add(wk.multiply(r[i]));
      y[i + n / 2] = q[i].subtract(wk.multiply(r[i]));
    }
    return y;
  }
}
