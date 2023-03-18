package ua.knu.rotan.sfft;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SFFTApplication {

  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(SFFTApplication.class);
    builder.headless(false);
    builder.run(args);
  }

  //    @Autowired
  //    AudioSupplier audioSupplier;
  //
  //    @Override
  //    public void run(ApplicationArguments args) throws Exception {
  //        int n = 1024, m = 256, k = 256;
  //        ApplicationFrame frame =new ApplicationFrame(n, k);
  //        while (true) {
  //            for (int i = 0; i < n; i++) {
  //                short[] a = audioSupplier.getAudioStream(m).get(0);
  //                frame.update(
  //
  // Arrays.stream(FFT.fft(a)).mapToDouble(Complex::magnitude).map(x->log(x+1)).limit(k).toArray()
  //                );
  //                frame.repaint();
  //            }
  //
  //        }
  //    }

  //    @Autowired
  //    AudioSupplier audioSupplier;
  //    protected static Rectangle DEFAULT_WINDOW = new Rectangle(200, 200, 600, 600);
  //    static int n = 1024, m = 512;
  //
  //    @SneakyThrows
  //    private void testSameThings() {
  //        double[][] array = new double[n][m];
  //        ArrayImage arrayImage = new ArrayImage(array);
  //        Chart chart = SwingChartFactory.chart(Quality.Advanced());
  //        buildChart(array,chart  );
  //
  ////        while (true) {
  //            for (int i = 0; i < n; i++) {
  //                List<short[]> a = audioSupplier.getAudioStream(m * 2);
  //                short[] channel = a.get(0);
  //                Complex[] b = FFT.fft(channel);
  //                double[] magnitudes =
  //
  // Arrays.stream(b).mapToDouble(Complex::magnitude).map(Math::log).toArray();
  //                array[i] = Arrays.copyOf(magnitudes, m);
  //            }
  //            new Thread(arrayImage::repaint).start();
  //            new Thread(()->{
  //                buildChart(array, chart);
  //            }).start();
  ////        }
  //    }
  //
  //    private static Chart buildChart(double[][] array, Chart chart) {
  //
  //        Mapper mapper = new Mapper() {
  //            public double f(double x, double y) {
  //                return array[(int) y][(int) x];
  //            }
  //        };
  //        Range xRange = new Range(0, m - 1);
  //        Range yRange = new Range(0, n - 1);
  //
  //
  //        Shape surface = new SurfaceBuilder().orthonormal(new OrthonormalGrid(xRange, n, yRange,
  // m), mapper);
  //        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), 0, 11.2, new Color(1, 1,
  // 1, .5f)));
  //        surface.setFaceDisplayed(true);
  //        surface.setWireframeDisplayed(false);
  //        surface.setWireframeColor(Color.BLACK);
  //
  //        chart.add(surface);
  //
  //        chart.render();
  //        chart.open("SFFT", DEFAULT_WINDOW);
  //
  //        return chart;
  //    }
}
