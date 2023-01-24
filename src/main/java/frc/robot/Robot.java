// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Component.Data.WheelData;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input.XboxAxis;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve.DrivingMode;
import frc.robot.Library.FRC_3117_Tools.Wrapper.ADIS16448_IMU_Gyro;
import frc.robot.RobotConstant.RobotConstant;
import frc.robot.SubSystems.LinearRobotArm;

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
    //Front Left
    var swerveModuleFL = new WheelData();
    swerveModuleFL.DriveController = RobotConstant.SWERVE_FRONT_LEFT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleFL.DirectionController = RobotConstant.SWERVE_FRONT_LEFT_STEER_CONTROLLER.ToMotorController();
    swerveModuleFL.DirectionEncoderChannel = RobotConstant.SWERVE_FRONT_LEFT_STEER_ENCODER;
    swerveModuleFL.WheelPosition = RobotConstant.SWERVE_FRONT_LEFT_POSITION;
    swerveModuleFL.AngleOffset = RobotConstant.SWERVE_FRONT_LEFT_STEER_ENCODER_OFFSET;

    //Front Right
    var swerveModuleFR = new WheelData();
    swerveModuleFR.DriveController = RobotConstant.SWERVE_FRONT_RIGHT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleFR.DirectionController = RobotConstant.SWERVE_FRONT_RIGHT_STEER_CONTROLLER.ToMotorController();
    swerveModuleFR.DirectionEncoderChannel = RobotConstant.SWERVE_FRONT_RIGHT_STEER_ENCODER;
    swerveModuleFR.WheelPosition = RobotConstant.SWERVE_FRONT_RIGHT_POSITION;
    swerveModuleFR.AngleOffset = RobotConstant.SWERVE_FRONT_RIGHT_STEER_ENCODER_OFFSET;

    //Rear Left
    var swerveModuleRL = new WheelData();
    swerveModuleRL.DriveController = RobotConstant.SWERVE_REAR_LEFT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleRL.DirectionController = RobotConstant.SWERVE_REAR_LEFT_STEER_CONTROLLER.ToMotorController();
    swerveModuleRL.DirectionEncoderChannel = RobotConstant.SWERVE_REAR_LEFT_STEER_ENCODER;
    swerveModuleRL.WheelPosition = RobotConstant.SWERVE_REAR_LEFT_POSITION;
    swerveModuleRL.AngleOffset = RobotConstant.SWERVE_REAR_LEFT_STEER_ENCODER_OFFSET;

    //Rear Left
    var swerveModuleRR = new WheelData();
    swerveModuleRR.DriveController = RobotConstant.SWERVE_REAR_RIGHT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleRR.DirectionController = RobotConstant.SWERVE_REAR_RIGHT_STEER_CONTROLLER.ToMotorController();
    swerveModuleRR.DirectionEncoderChannel = RobotConstant.SWERVE_REAR_RIGHT_STEER_ENCODER;
    swerveModuleRR.WheelPosition = RobotConstant.SWERVE_REAR_RIGHT_POSITION;
    swerveModuleRR.AngleOffset = RobotConstant.SWERVE_REAR_RIGHT_STEER_ENCODER_OFFSET;

    var swerveModules = new WheelData[]
    {
      swerveModuleFL,
      swerveModuleFR,
      swerveModuleRL,
      swerveModuleRR
    };

    var swerve = new Swerve(swerveModules, new ADIS16448_IMU_Gyro());

    swerve.SetPIDGain(0, 1, 0, 0);
    swerve.SetPIDGain(1, 1, 0, 0);
    swerve.SetPIDGain(2, 1, 0, 0);
    swerve.SetPIDGain(3, 1, 0, 0);

    swerve.SetCurrentMode(DrivingMode.Local);
    swerve.SetHeadingOffset(Math.PI / 2);

    AddComponent("Swerve", swerve);
  }

  @Override
  public void CreateInput()
  {
    //Swerve
    Input.CreateAxis("Horizontal", 0, XboxAxis.LEFTX, false);
    Input.CreateAxis("Vertical", 0, XboxAxis.LEFTY, true);
    Input.CreateAxis("Rotation", 0, XboxAxis.LEFT_TRIGGER, false);

    Input.SetAxisNegative("Rotation", 0, XboxAxis.RIGHT_TRIGGER, false);

    Input.SetAxisDeadzone("Horizontal", 0.15);
    Input.SetAxisDeadzone("Vertical", 0.15);
    Input.SetAxisDeadzone("Rotation", 0.15);
  }
}