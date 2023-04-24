package ua.knu.rotan.sfft.alg;

import lombok.*;

@Getter
@AllArgsConstructor
public class ValueRange {
  @NonNull private final int min, max;

  @Setter @NonNull private int value;

  public static ValueRange fixedValurOf(int value) {
    return new ValueRange(value, value, value);
  }
}
