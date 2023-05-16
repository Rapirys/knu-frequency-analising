package ua.knu.rotan.sfft;

import java.awt.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SFFTApplication {

  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(SFFTApplication.class);
    builder.headless(false);
    builder.run(args);
  }
}
