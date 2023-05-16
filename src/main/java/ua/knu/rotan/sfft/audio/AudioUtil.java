package ua.knu.rotan.sfft.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AudioUtil {

  int[][] parseAudioData(byte[] data, int dataSize, AudioFormat format) {
    int channels = format.getChannels();
    if (dataSize % (2 * channels) != 0)
      throw new IllegalArgumentException(
          "Wrong dataSize, should contain full number of frames for respective AudioFormat.");

    int chanelSize = dataSize / (2 * channels);
    int[][] channelsData = new int[channels][chanelSize];

    ByteBuffer bb = ByteBuffer.wrap(data, 0, dataSize);
    bb.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    for (int i = 0; i < chanelSize; i++)
      for (int c = 0; c < channels; c++) channelsData[c][i] = bb.getShort();

    return channelsData;
  }

  public static double[] lowPassFilterWithRMS(
      double[] input, double sampleRate, double cutoffFrequency) {
    double RC = 1.0 / (2.0 * Math.PI * cutoffFrequency);
    double dt = 1.0 / sampleRate; // sampleRate is assumed to be defined somewhere
    double alpha = dt / (RC + dt);

    double[] output = new double[input.length];
    output[0] = input[0];

    // Compute the unfiltered signal's RMS power
    double rmsUnfiltered = Math.sqrt(Arrays.stream(input).map(x -> x * x).average().getAsDouble());

    for (int i = 1; i < input.length; i++) {
      output[i] = alpha * input[i] + (1 - alpha) * output[i - 1];
    }

    // Compute the filtered signal's RMS power
    double rmsFiltered = Math.sqrt(Arrays.stream(output).map(x -> x * x).average().getAsDouble());

    // Scale the filtered signal by the ratio of RMS powers
    double normalizationFactor = rmsUnfiltered / rmsFiltered;
    output = Arrays.stream(output).map(x -> x * normalizationFactor).toArray();

    return output;
  }
}
