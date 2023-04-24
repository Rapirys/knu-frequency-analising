package ua.knu.rotan.sfft.alg;

import static java.lang.Math.sqrt;

public record Complex(double real, double imag) {
  public Complex add(Complex other) {
    double real = this.real + other.real;
    double imag = this.imag + other.imag;
    return new Complex(real, imag);
  }

  public Complex subtract(Complex other) {
    double real = this.real - other.real;
    double imag = this.imag - other.imag;
    return new Complex(real, imag);
  }

  public Complex multiply(Complex other) {
    double real = this.real * other.real - this.imag * other.imag;
    double imag = this.real * other.imag + this.imag * other.real;
    return new Complex(real, imag);
  }

  public Complex divide(Complex other) {
    double denom = other.real * other.real + other.imag * other.imag;
    double real = (this.real * other.real + this.imag * other.imag) / denom;
    double imag = (this.imag * other.real - this.real * other.imag) / denom;
    return new Complex(real, imag);
  }

  public double magnitude() {
    return sqrt(real * real + imag * imag);
  }

  @Override
  public String toString() {
    if (imag < 0) {
      return String.format("%.2f - %.2fi", real, Math.abs(imag));
    } else {
      return String.format("%.2f + %.2fi", real, imag);
    }
  }

  public static Complex fromReal(double real) {
    return new Complex(real, 0);
  }

  public Complex conjugate() {
    return new Complex(real, -imag);
  }

  public Complex scale(double n) {
    return new Complex(real * n, -imag * n);
  }
}
