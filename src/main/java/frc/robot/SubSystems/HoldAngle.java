package frc.robot.SubSystems;

import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Math.Mathf;
import frc.robot.Library.FRC_3117_Tools.Math.Range;
import frc.robot.Library.FRC_3117_Tools.Physics.Control.AntiWindupPID;
import frc.robot.Robot;

public class HoldAngle implements Component {

    public HoldAngle() {
        _swerve = Robot.Instance.GetComponent("SwerveDrive");

        _pid = new AntiWindupPID("hold-angler-pid");
        _pid.SetRange(-1, 1);
        _pid.SetResetOnFlip(true);
    }
    private Swerve _swerve;

    private Range _range = new Range(-0.15, 0.15);

    private AntiWindupPID _pid;

    private boolean _isHolding = false;
    private double _targetAngle;

    @Override
    public void Awake() {

    }

    @Override
    public void Init() {
        _pid.Reset();
    }

    @Override
    public void Disabled() {

    }

    @Override
    public void DoComponent() {
        if (_isHolding) {
            var heading = _swerve.GetHeading();
            _swerve.OverrideRotationAxis(_range.Clamp(_pid.EvaluateError(Mathf.DeltaAngle(_targetAngle, heading))));
        }
    }

    @Override
    public void Print() {

    }

    public void StartHold() {
        _pid.Reset();
        _isHolding = true;
    }
    public void StopHold() {
        _isHolding = false;
    }

    public void SetHeadingTarget(double targetAngle) {
        _targetAngle = targetAngle;
    }
}
