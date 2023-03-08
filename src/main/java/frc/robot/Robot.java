package frc.robot;

import frc.robot.Library.FRC_3117_Tools.Component.CAN.MultiDigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;

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