package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autonomous.AutonomousBase;
import frc.robot.Autonomous.SimpleAuto;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorController;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.SubSystems.AprilTag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Robot extends RobotBase
{
  //public MotorController ClawMotor;

  public SendableChooser<AutonomousBase> SelectedAutonomous = new SendableChooser<>();
  public HashMap<String, AutonomousBase> AutonomousModes = new HashMap<>();

  @Override
  public void robotInit()
  {
    AprilTag.GenerateTags();
    super.robotInit();

    try {
      for (var cam : UsbCamera.enumerateUsbCameras())
      {
        System.out.println(cam.name);
        CameraServer.startAutomaticCapture(cam.dev);
      }

    }
    catch (Exception e) {}

    for (var mode : AutonomousModes.entrySet()) {
      SelectedAutonomous.addOption(mode.getKey(), mode.getValue());
      System.out.println(mode.getKey());
    }

    SmartDashboard.putData("Auto Selector", SelectedAutonomous);
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

    SelectedAutonomous.getSelected().StartAuto();
  }

  @Override
  public void autonomousPeriodic()
  {
    super.autonomousPeriodic();

    SelectedAutonomous.getSelected().AutoLoop();
  }

  @Override
  public void autonomousExit()
  {
    SelectedAutonomous.getSelected().AutoEnd();
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

    //ClawMotor.Set(Input.GetAxis("Claw") * 0.5);
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

    AddAutonomous(new SimpleAuto("cross-line", .5, 3));

    //ClawMotor = new MotorController(MotorController.MotorControllerType.SparkMax, 15, true);
    //ClawMotor.SetBrake(true);
  }

  @Override
  public void CreateInput()
  {
    //Input.CreateAxis("Claw", 0, Input.XboxAxis.RIGHTY, false);
    //Input.SetAxisDeadzone("Claw", 0.15);

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

  public void AddAutonomous(AutonomousBase auto) {
    AutonomousModes.put(auto.GetName(), auto);
  }
}