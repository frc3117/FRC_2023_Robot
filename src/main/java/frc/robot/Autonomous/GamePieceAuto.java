package frc.robot.Autonomous;

import frc.robot.Game.GamePiece;
import frc.robot.Game.GridLevel;
import frc.robot.Library.FRC_3117_Tools.Component.FunctionScheduler;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Math.Mathf;
import frc.robot.Robot;
import frc.robot.SubSystems.Feeder;
import frc.robot.SubSystems.Manipulator;

public class GamePieceAuto extends AutonomousBase {
    public GamePieceAuto(String name, GamePiece piece, GridLevel level) {
        super(name);

        _piece = piece;
        _level = level;

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
        switch (_level) {
            case High:
                return _piece == GamePiece.Cone ? "cone-high" : "cube-high";

            case Mid:
                return _piece == GamePiece.Cone ? "cone-mid" : "cube-mid";

            case Low:
                return "ground-cube";

            default:
                return null;
        }
    }
    private void OverrideSwerves(double horizontal, double vertical, double rotation) {
        _swerve.OverrideHorizontalAxis(horizontal);
        _swerve.OverrideVerticalAxis(vertical);
        _swerve.OverrideRotationAxis(rotation);
    }

    private void FlipRobot() {
        //_swerve.SetHeadingAsCurrent(0);
        _swerve.AddHeadingOffset(Math.PI);
    }
    private void SafetyForward() {
        OverrideSwerves(0, 0.5, 0);
    }
    private void SetManipulatorPose() {
        _manipulator.SetPose(GetPoseName());
    }
    private void BackwardIntoGrid() {
        OverrideSwerves(0, -0.5, 0);
    }
    private void EjectObject() {
        _feeder.OverrideIntake();
    }
    private void Stowed() {
        _manipulator.SetStowed();
    }
    private void ResetFlip() {
        //_swerve.SetHeadingAsCurrent(Math.PI);
        //_swerve.AddHeadingOffset(Math.PI);
    }
    private void CrossLine() {
        OverrideSwerves(0, 0.5, 0);
    }
}
