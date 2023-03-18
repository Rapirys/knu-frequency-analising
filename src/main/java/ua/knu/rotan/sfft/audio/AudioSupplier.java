package ua.knu.rotan.sfft.audio;

import static java.lang.Math.min;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class AudioSupplier {
  @Value("${audio.buffer.size:4096}")
  private int bufferSize;

  @Autowired private AudioFormat audioFormat;
  private TargetDataLine line = null;

  @PostConstruct
  @SneakyThrows
  public void init() {
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    line = (TargetDataLine) AudioSystem.getLine(info);
    line.open(audioFormat);
    line.start();
  }

  @PreDestroy
  public void destroy() {
    line.close();
  }

  public List<short[]> getAudioStream(int numberOfSamples) {
    List<short[]> channelsData = new ArrayList<short[]>();

    for (int c = 0; c < audioFormat.getChannels(); c++) {
      short[] channel = new short[numberOfSamples];
      channelsData.add(channel);
    }

    int leftToRead = 2 * numberOfSamples * audioFormat.getChannels();
    byte[] buffer = new byte[bufferSize];
    while (leftToRead != 0) {
      int numBytesRead = line.read(buffer, 0, min(buffer.length, leftToRead));

      ByteBuffer bb = ByteBuffer.wrap(buffer, 0, numBytesRead);
      bb.order(ByteOrder.LITTLE_ENDIAN);
      for (int i = 0; i < numBytesRead / 4; i++)
        for (int c = 0; c < audioFormat.getChannels(); c++) channelsData.get(c)[i] = bb.getShort();

      leftToRead -= numBytesRead;
    }

    return channelsData;
  }
}
