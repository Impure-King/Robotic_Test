// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  // Defining the controller:
  private GenericHID m_controller;

  // Crappy motors:
  private WPI_VictorSPX m_LeftFront;
  private WPI_VictorSPX m_RightFront;
  private WPI_VictorSPX m_RightBack;

  // Good motor:
  private CANSparkMax m_LeftBack;

  // Motor controllers:
  private MotorController m_left;
  private MotorController m_right;
  private DifferentialDrive m_robot;
  
  // Creating a Timer:
  public Timer m_timer;

  // Some helpful values:
  private float MovementSpeedMultiplier;
  private float TurningSpeedMultiplier;

  // Extra constants:
  private VictorSPXControlMode spxControlMode = VictorSPXControlMode.PercentOutput;

  public void setTopVictors(double left_speed, double right_speed){
    m_RightFront.set(spxControlMode, right_speed);
    m_LeftFront.set(spxControlMode, left_speed);

  }

  @Override
  public void robotInit() {
    // Starting the initialization:
    m_LeftFront = new WPI_VictorSPX(2);
    m_RightFront = new WPI_VictorSPX(1);
    m_RightBack = new WPI_VictorSPX(0);
    m_LeftBack = new CANSparkMax(4, MotorType.kBrushed);

    // Defining controller:
    m_controller = new GenericHID(0);

    // Defining MotorGroups:
    m_left = new MotorControllerGroup(m_LeftBack, m_LeftFront);
    m_right = new MotorControllerGroup(m_RightBack, m_RightFront);

    // Creating final robotic:
    m_robot = new DifferentialDrive(m_left, m_right);

    // Creating the timer:
    m_timer = new Timer();

    // Helpful Coefficients:
    MovementSpeedMultiplier = 0.70f;
    TurningSpeedMultiplier = 0.70f;
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    // Resetting the timer:
    m_timer.reset();
    m_timer.start();
    while (m_timer.get() < 3){
      m_robot.tankDrive(0.5, 0.5);
      setTopVictors(0.5, 0.5);
    }

    while (m_timer.get() <= 4){
      m_robot.tankDrive(-0.10, -0.1);
      setTopVictors(-0.10, -0.1);
    }

    setTopVictors(0, 0);
    m_robot.tankDrive(0,0);
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    // Checks every interval:
    m_robot.tankDrive(-m_controller.getRawAxis(1) * MovementSpeedMultiplier, m_controller.getRawAxis(2) * TurningSpeedMultiplier);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
