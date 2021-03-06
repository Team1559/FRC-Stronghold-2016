package org.usfirst.frc.team1559.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class SerialClient {
	private final int BAUDRATE = 115200;
	private final String sendChar = "s";
	SerialPort sp;

	public SerialClient() {
		sp = new SerialPort(BAUDRATE, Port.kMXP);
	}

	public double grabAngle() {
		double result = -2000;
		try {
			String in = sp.readString();
			System.out.println("its a string and its" + in);
			if (in.endsWith("t")) {
				in = in.substring(0, in.indexOf("t"));
				try {
					result = Double.parseDouble(in);
				} catch (Exception e) {
					System.out.println("SerialClient: Pi's not passing back a number. Bad pi.");
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("PLUG IN THE DAMN CABLE");
			e.printStackTrace();
		}

		return result;

		// well everyone else was re-writing this so i figured why not

		// double ang = -2000; //return a -2000 if the pi goes rip
		// String str = null;
		//
		// for(int i = 1; i < 11; i++){
		// String read = sp.readString(1);
		//
		// if (!read.equals("t")){
		// str = str + read;
		// }else{
		// ang = Double.parseDouble(str);
		// str = null;
		// }
		// }
		// return ang;

		// while (true) {
		// in = sp.readString(1);
		// if (!in.equals("t")) {
		// str = str + in;
		// } else {
		// double ret = Double.parseDouble(str);
		// str = "";
		// return ret;
		// }
		// }
		// } else {
		// return -1000;// -1000 means we don't see anything
		// }

	}

	public double getAdjustedAngle(double xPos, double yPos) {

		double d = Wiring.CAMERA_ROTATION_OFFSET;
		double camAngle = grabAngle() + Wiring.SHOOTER_SUCKS_CONSTANT; //degrees
		double ret = 0.0;

		ret = Math.sqrt(Math.pow(yPos - d, 2) + Math.pow(xPos, 2));
		ret *= Math.sin(Math.toRadians(180 - camAngle));
		ret /= Math.sqrt(Math.pow(xPos, 2) + Math.pow(yPos, 2));
		ret = Math.asin(ret);

//		===========THIS MATH ONLY WORKS IF WFFL GIVES THE DISTANCE TO CAMERA===================
//		ret = Math.sqrt(Math.pow(xPos, 2) + Math.pow(yPos, 2));
//		ret *= Math.sin(Math.toRadians(180 - camAngle));
//		ret /= Math.sqrt(Math.pow(xPos, 2) + (Math.pow(yPos + d, 2)));
//		ret = Math.asin(ret);
//		
		return Math.toDegrees(ret);
	}
	
	public void send(String msg) {
		sp.writeString(msg);
	}

	public void reset() {
		sp.flush();
	}
}
