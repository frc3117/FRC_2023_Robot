package frc.robot;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.DutyCycleAbsoluteEncoder;
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
    Swerve swerve = GetComponent("SwerveDrive");

    swerve.SetPIDGain(0, 0.5, 0, 0);
    swerve.SetPIDGain(1, 0.5, 0, 0);
    swerve.SetPIDGain(2, 0.5, 0, 0);
    swerve.SetPIDGain(3, 0.5, 0, 0);

    swerve.SetHeadingOffset(Math.PI / 2);
    
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
    /*
    //Swerve
    Input.CreateAxis("Horizontal", 0, XboxAxis.LEFTX, false);
    Input.CreateAxis("Vertical", 0, XboxAxis.LEFTY, true);
    Input.CreateAxis("Rotation", 0, XboxAxis.LEFT_TRIGGER, false);

    Input.SetAxisNegative("Rotation", 0, XboxAxis.RIGHT_TRIGGER, false);

    Input.SetAxisDeadzone("Horizontal", 0.15);
    Input.SetAxisDeadzone("Vertical", 0.15);
    Input.SetAxisDeadzone("Rotation", 0.15);*/
  }
}