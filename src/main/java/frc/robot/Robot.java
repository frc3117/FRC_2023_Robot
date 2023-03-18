package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autonomous.AutonomousBase;
import frc.robot.Autonomous.EmptyAuto;
import frc.robot.Autonomous.GamePieceAuto;
import frc.robot.Autonomous.SimpleAuto;
import frc.robot.Game.GamePiece;
import frc.robot.Game.GridLevel;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.RobotBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.SubSystems.AprilTag;

import java.util.HashMap;

public class Robot extends RobotBase
{
  //public MotorController ClawMotor;

  public Swerve Swerve;

  public SendableChooser<AutonomousBase> SelectedAutonomous = new SendableChooser<>();
  public HashMap<String, AutonomousBase> AutonomousModes = new HashMap<>();

  @Override
  public void robotInit()
  {
    AprilTag.GenerateTags();
    super.robotInit();

    //var cam = CameraServer.startAutomaticCapture();
    //cam.setVideoMode(new VideoMode(VideoMode.PixelFormat.kMJPEG, 640, 360, 30));

    SetDefaultAuto(new EmptyAuto("Nothing"));
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
    if (Input.GetButton("SwerveSlow"))
      Swerve.SetSpeed(0.5);
    else
      Swerve.SetSpeed(1);

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
    Swerve = GetComponent("SwerveDrive");

    Swerve.SetPIDGain(0, 0.5, 0, 0);
    Swerve.SetPIDGain(1, 0.5, 0, 0);
    Swerve.SetPIDGain(2, 0.5, 0, 0);
    Swerve.SetPIDGain(3, 0.5, 0, 0);

    Swerve.SetHeadingOffset(Math.PI / 2);

    AddAutonomous(new SimpleAuto("cross-line", .5, 5));
    AddAutonomous(new SimpleAuto("cross-line-balance", .5, 7));

    AddAutonomous(new GamePieceAuto("cube-low-cross", GamePiece.Cube, GridLevel.Low));
    AddAutonomous(new GamePieceAuto("cube-mid-cross", GamePiece.Cube, GridLevel.Mid));
    AddAutonomous(new GamePieceAuto("cube-high-cross", GamePiece.Cube, GridLevel.High));

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

  public void SetDefaultAuto(AutonomousBase auto) {
    SelectedAutonomous.setDefaultOption(auto.GetName(), auto);
    AutonomousModes.put(auto.GetName(), auto);
  }
  public void AddAutonomous(AutonomousBase auto) {
    SelectedAutonomous.addOption(auto.GetName(), auto);
    AutonomousModes.put(auto.GetName(), auto);
  }
}