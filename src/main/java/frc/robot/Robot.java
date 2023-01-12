// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.SubSystems.LinearRobotArm;
import frc.robot.SubSystems.Data.LinearRobotArmData;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends RobotBase 
{
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  
   public LinearRobotArm RobotArm;

  @Override
  public void robotInit() 
  {
    super.robotInit();
  }

  @Override
  public void robotPeriodic() 
  {
    super.robotPeriodic();
  }

  @Override
  public void autonomousInit() 
  {
    super.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() 
  {
    super.autonomousPeriodic();
  }

  @Override
  public void teleopInit() 
  {
    super.teleopInit();
  }

  @Override
  public void teleopPeriodic() 
  {
    super.teleopPeriodic();
  }

  @Override
  public void disabledInit() 
  {
    super.disabledInit();
  }

  @Override
  public void disabledPeriodic() 
  {
    super.disabledPeriodic();
  }

  @Override
  public void testInit() 
  {
    super.testInit();
  }

  @Override
  public void testPeriodic() 
  {
    super.testPeriodic();
  }

  @Override
  public void simulationInit() 
  {
    super.simulationInit();
  }

  @Override
  public void simulationPeriodic() 
  {
    super.simulationPeriodic();
  }

  @Override
  public void CreateComponentInstance()
  {
    //Robot Arm
    var armData = new LinearRobotArmData();
 
    armData.VerticalTranslationController = new WPI_TalonSRX(10); //1
    armData.ArmRotationController = new WPI_TalonSRX(9); //2
    armData.WristRotationController = new WPI_TalonSRX(8); //3

    armData.VerticalTranslationEncoder = new AnalogEncoder(0);
    armData.ArmRotationEncoder = new AnalogEncoder(1);
    armData.WristRotationEncoder = new AnalogEncoder(2);

    RobotArm = new LinearRobotArm(armData);
    AddComponent("RobotArm", RobotArm);
  }

  @Override
  public void CreateInput()
  {
    Input.CreateAxis("VerticalTranslation", 0, Input.XboxAxis.RIGHT_TRIGGER, false);
    Input.SetAxisNegative("VerticalTranslation", 0, Input.XboxAxis.LEFT_TRIGGER, false);

    Input.CreateAxis("ArmRotation", 0, Input.XboxAxis.RIGHTY, false);
    Input.SetAxisDeadzone("ArmRotation", 0.15);

    Input.CreateAxis("WristRotation", 0, Input.XboxAxis.LEFTY, false);
    Input.SetAxisDeadzone("WristRotation", 0.15);
  }
}
