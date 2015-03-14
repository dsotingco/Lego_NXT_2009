import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;

public class gyroTest {
  public static void main(String[] args) throws Exception {
    GyroSensor theGyro = new GyroSensor(SensorPort.S3);

    while (true) {
      LCD.drawInt(theGyro.readValue(),4,0,1);
      LCD.drawInt(SensorPort.S1.readRawValue(), 4, 0, 2);
      LCD.drawInt(SensorPort.S1.readValue(), 4, 0, 3);
	if (Button.ENTER.isPressed()) break;

    }
  }
}