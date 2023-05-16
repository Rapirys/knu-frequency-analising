package ua.knu.rotan.sfft.audio;

public interface AudioSupplier {
  int[] getAudioChunk(int offset, int numberOfSamples, int channel);

  int getPointerPosition(int channel);

  void start();

  void stop();

  void switchRunningState();
}
