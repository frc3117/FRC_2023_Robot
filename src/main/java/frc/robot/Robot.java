package frc.robot;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Component.Data.WheelData;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input.XboxAxis;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve.DrivingMode;
import frc.robot.Library.FRC_3117_Tools.Wrapper.ADIS16448_IMU_Gyro;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.AnalogAbsoluteEncoder;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.DutyCycleAbsoluteEncoder;
import frc.robot.RobotConstant.RobotConstant;
import frc.robot.SubSystems.Data.ManipulatorSegmentData;
import frc.robot.SubSystems.Manipulator;
import frc.robot.SubSystems.Data.ManipulatorData;

public class Robot extends RobotBase 
{
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
    // Front Left
    var swerveModuleFL = new WheelData();
    swerveModuleFL.DriveController = RobotConstant.SWERVE_FRONT_LEFT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleFL.DirectionController = RobotConstant.SWERVE_FRONT_LEFT_STEER_CONTROLLER.ToMotorController();
    swerveModuleFL.DirectionEncoder = new AnalogAbsoluteEncoder(RobotConstant.SWERVE_FRONT_LEFT_STEER_ENCODER);
    swerveModuleFL.WheelPosition = RobotConstant.SWERVE_FRONT_LEFT_POSITION;
    swerveModuleFL.AngleOffset = RobotConstant.SWERVE_FRONT_LEFT_STEER_ENCODER_OFFSET;

    // Front Right
    var swerveModuleFR = new WheelData();
    swerveModuleFR.DriveController = RobotConstant.SWERVE_FRONT_RIGHT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleFR.DirectionController = RobotConstant.SWERVE_FRONT_RIGHT_STEER_CONTROLLER.ToMotorController();
    swerveModuleFR.DirectionEncoder = new AnalogAbsoluteEncoder(RobotConstant.SWERVE_FRONT_RIGHT_STEER_ENCODER);
    swerveModuleFR.WheelPosition = RobotConstant.SWERVE_FRONT_RIGHT_POSITION;
    swerveModuleFR.AngleOffset = RobotConstant.SWERVE_FRONT_RIGHT_STEER_ENCODER_OFFSET;

    // Rear Left
    var swerveModuleRL = new WheelData();
    swerveModuleRL.DriveController = RobotConstant.SWERVE_REAR_LEFT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleRL.DirectionController = RobotConstant.SWERVE_REAR_LEFT_STEER_CONTROLLER.ToMotorController();
    swerveModuleRL.DirectionEncoder = new AnalogAbsoluteEncoder(RobotConstant.SWERVE_REAR_LEFT_STEER_ENCODER);
    swerveModuleRL.WheelPosition = RobotConstant.SWERVE_REAR_LEFT_POSITION;
    swerveModuleRL.AngleOffset = RobotConstant.SWERVE_REAR_LEFT_STEER_ENCODER_OFFSET;

    // Rear Left
    var swerveModuleRR = new WheelData();
    swerveModuleRR.DriveController = RobotConstant.SWERVE_REAR_RIGHT_DRIVE_CONTROLLER.ToMotorController();
    swerveModuleRR.DirectionController = RobotConstant.SWERVE_REAR_RIGHT_STEER_CONTROLLER.ToMotorController();
    swerveModuleRR.DirectionEncoder = new AnalogAbsoluteEncoder(RobotConstant.SWERVE_REAR_RIGHT_STEER_ENCODER);
    swerveModuleRR.WheelPosition = RobotConstant.SWERVE_REAR_RIGHT_POSITION;
    swerveModuleRR.AngleOffset = RobotConstant.SWERVE_REAR_RIGHT_STEER_ENCODER_OFFSET;

    var swerveModules = new WheelData[] {
      swerveModuleFL,
      swerveModuleFR,
      swerveModuleRL,
      swerveModuleRR
    };

    var swerve = new Swerve(swerveModules, new ADIS16448_IMU_Gyro());

    swerve.SetPIDGain(0, 0.5, 0, 0);
    swerve.SetPIDGain(1, 0.5, 0, 0);
    swerve.SetPIDGain(2, 0.5, 0, 0);
    swerve.SetPIDGain(3, 0.5, 0, 0);

    swerve.SetCurrentMode(DrivingMode.World);
    swerve.SetHeadingOffset(Math.PI / 2);

    AddComponent("Swerve", swerve);
    
    // Manipulator
    var manipulatorData = new ManipulatorData();

    // Rotating Base
    var rotBase = new ManipulatorSegmentData();
    rotBase.Axis = new Vector3d(0,0, 1);
    rotBase.Length = 0;
    rotBase.Motors = new MotorControllerGroup()
            .AddPositiveController(null);
    rotBase.Encoder = new DutyCycleAbsoluteEncoder(5);

    // First Segment
    var segm0 = new ManipulatorSegmentData();
    segm0.Axis = new Vector3d(0,1, 0);
    segm0.Length = 0;
    segm0.Motors = new MotorControllerGroup()
            .AddPositiveController(null)
            .AddNegativeController(null);
    segm0.Encoder = new DutyCycleAbsoluteEncoder(0);

    // Second Segment
    var segm1 = new ManipulatorSegmentData();
    segm1.Axis = new Vector3d(0,1, 1);
    segm1.Length = 0;
    segm1.Motors = new MotorControllerGroup()
            .AddPositiveController(null);
    segm1.Encoder = new DutyCycleAbsoluteEncoder(1);

    // Third Segment
    var segm2 = new ManipulatorSegmentData();
    segm2.Axis = new Vector3d(0,1, 1);
    segm2.Length = 0;
    segm2.Motors = new MotorControllerGroup()
            .AddPositiveController(null);
    segm2.Encoder = new DutyCycleAbsoluteEncoder(2);

    manipulatorData.AddSegment(rotBase);
    manipulatorData.AddSegment(segm0);
    manipulatorData.AddSegment(segm1);
    manipulatorData.AddSegment(segm2);

    var manipulator = new Manipulator(manipulatorData);
    AddComponent("Manipulator", manipulator);
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