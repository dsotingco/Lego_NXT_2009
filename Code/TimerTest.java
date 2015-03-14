import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;

public class TimerTest implements TimerListener
{
	public static void main(String[] args) throws InterruptedException
	{
		Timer timer = new Timer(1000, new TimerTest());
		timer.start();
		Button.waitForPress();
		timer.stop();
	}
	
	public void timedOut()
	{
		Sound.beep();
	}
}