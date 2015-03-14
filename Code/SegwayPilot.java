import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import lejos.navigation.Pilot;
import java.lang.Math;


public class SegwayPilot
{
	Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B, Motor.C);
	pilot.travel(50f);
}