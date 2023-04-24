package ua.knu.rotan.sfft.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "view.ui")
public class UIProperties {
  public int width;
  public int height;

  public int renderWidth;
  public int renderHeight;
}
