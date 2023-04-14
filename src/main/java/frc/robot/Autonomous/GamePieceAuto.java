package frc.robot.Autonomous;

import frc.robot.Game.GamePiece;
import frc.robot.Game.GridLevel;
import frc.robot.Library.FRC_3117_Tools.Component.FunctionScheduler;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Robot;
import frc.robot.SubSystems.Feeder;
import frc.robot.SubSystems.Manipulator;

public class GamePieceAuto extends AutonomousBase {
    public enum Strafe {
        None,
        Left,
        Right
    }

    public GamePieceAuto(String name, GamePiece piece, GridLevel level, Strafe endStrafe) {
        super(name);

        _piece = piece;
        _level = level;
        _strafe = endStrafe;

        _scheduler = new FunctionScheduler().
                AddFunction(this::FlipRobot).
                AddRunFor(this::SafetyForward, 1).
                AddFunction(this::SetManipulatorPose).
                AddWait(3).
                AddRunFor(this::BackwardIntoGrid, 2.5).
                AddRunFor(this::EjectObject, 1).
                AddFunction(this::Stowed).
                AddRunFor(this::CrossLine, 5).
                AddFunction(this::ResetFlip);
    }

    private Swerve _swerve;
    private Manipulator _manipulator;
    private Feeder _feeder;

    private GamePiece _piece;
    private GridLevel _level;
    private Strafe _strafe;

    private FunctionScheduler _scheduler;

    @Override
    public void StartAuto() {
        _swerve = Robot.Instance.GetComponent("SwerveDrive");
        _manipulator = Robot.Instance.GetComponent("Manipulator");
        _feeder = Robot.Instance.GetComponent("Feeder");

        _scheduler.Start();
    }

    @Override
    public void AutoLoop() {
        _scheduler.DoComponent();
    }

    @Override
    public void AutoEnd() {

    }

    private String GetPoseName() {
        var name = "";
        switch (_level) {
            case High:
                name = _piece == GamePiece.Cone ? "cone-high" : "cube-high";
                break;

            case Mid:
                name = _piece == GamePiece.Cone ? "cone-mid" : "cube-mid";
                break;

            case Low:
                name = "ground-cube";
                break;

            default:
                return null;
        }

        return name;
    }
    private void OverrideSwerves(double horizontal, double vertical, double rotation) {
        _swerve.OverrideHorizontalAxis(horizontal);
        _swerve.OverrideVerticalAxis(vertical);
        _swerve.OverrideRotationAxis(rotation);
    }

    private void FlipRobot() {
        _swerve.ZeroIMU(Math.PI);
        //_swerve.ReverseControll();

        //_swerve.SetHeadingAsCurrent(0);
        //_swerve.AddHeadingOffset(Math.PI);
    }
    private void SafetyForward() {
        OverrideSwerves(0, -0.5, 0);
    }
    private void SetManipulatorPose() {
        _manipulator.SetPose(GetPoseName());
    }
    private void BackwardIntoGrid() {
        OverrideSwerves(0, 0.5, 0);
    }
    private void EjectObject() {
        _feeder.OverrideIntake();
    }
    private void Stowed() {
        _manipulator.SetStowed();
    }
    private void ResetFlip() {
        //_swerve.ReverseControll();

        //_swerve.SetHeadingAsCurrent(Math.PI);
        //_swerve.AddHeadingOffset(Math.PI);
    }
    private void CrossLine() {
        var horizontal = 0.;
        var vertical = -0.5;
        var rotation = 0.;

        switch (_strafe)
        {
            case None:
                horizontal = 0;
                break;

            case Left:
                horizontal = 0.1;
                break;

            case Right:
                horizontal = -0.1;
                break;
        }

        OverrideSwerves(horizontal, vertical, rotation);
    }

    public static GamePieceAuto[] GenerateAllAuto() {
        var arr = new GamePieceAuto[GamePiece.values().length * GridLevel.values().length * Strafe.values().length];

        var i = 0;
        for (var piece : GamePiece.values()) {
            for (var level : GridLevel.values()) {
                for (var strafe : Strafe.values()) {
                    var name = "";
                    switch (piece)
                    {
                        case Cone:
                            name += "cone-";
                            break;

                        case Cube:
                            name += "cube-";
                            break;
                    }

                    switch (level)
                    {
                        case Low:
                            name += "low-";
                            break;

                        case Mid:
                            name += "mid-";
                            break;

                        case High:
                            name += "high-";
                            break;
                    }

                    switch (strafe)
                    {
                        case None:
                            name += "center";
                            break;

                        case Left:
                            name += "left";
                            break;

                        case Right:
                            name += "right";
                            break;
                    }

                    arr[i++] = new GamePieceAuto(name, piece, level, strafe);
                }
            }
        }

        return arr;
    }
}
