//THIS IS THE CODE

package org.usfirst.frc.team6352.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String driveAuto = "Driving Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	RobotDrive myRobot;
	Joystick joystick;
	Joystick controller;

	Victor winchMotor;
	
	Timer autoTimer = new Timer();
	
	enum AutoState {
		INITIAL,
		DRIVING,
		FINISH
	}
	
	AutoState autoState;
	
	@Override
	public void robotInit() {
		
		//chooser.addDefault("Default Auto", defaultAuto);
		//chooser.addObject(driveAuto, driveAuto);
		//SmartDashboard.putData("Auto choices", chooser);
		
		joystick = new Joystick(0);
		controller = new Joystick(1);
		
		CameraServer.getInstance().startAutomaticCapture();
		CameraServer.getInstance().startAutomaticCapture();
		
		myRobot = new RobotDrive(new Victor(0), new Victor(1));
		myRobot.setInvertedMotor(MotorType.kRearLeft, true);
		myRobot.setInvertedMotor(MotorType.kRearRight, true);
		myRobot.setSafetyEnabled(false);
		
		winchMotor = new Victor(2);
	}

	@Override
	public void autonomousInit() {
		autoSelected = driveAuto;
		System.out.println("Auto selected: " + autoSelected);
		autoState = AutoState.INITIAL;
	}

	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case driveAuto:
			switch (autoState) {
			case INITIAL:
				myRobot.drive(-0.3, 0);
				autoTimer.reset();
				autoTimer.start();
				autoState = AutoState.DRIVING;
				break;
				
			case DRIVING:
				if (autoTimer.get() > 1.5){	
					myRobot.drive(0, 0);
					autoState = AutoState.FINISH;
				}
				break;
			case FINISH:
				
				break;
			}
			break;
		case defaultAuto:
		default:
			break;
		}
	}

	@Override
	public void teleopPeriodic() {
		myRobot.arcadeDrive(joystick);		
	
		if (controller.getRawAxis(3) > 0.5) {
			winchMotor.set(0.2);
		} else if (controller.getRawAxis(2) > 0.5) {
			winchMotor.set(-0.2);
		} else {
			winchMotor.set(0);
		}
	}

	@Override
	public void testPeriodic() {
		
	}
}

