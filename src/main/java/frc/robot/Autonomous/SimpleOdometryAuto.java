package frc.robot.Autonomous;

import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Robot;

public class SimpleOdometryAuto extends AutonomousBase {
    public SimpleOdometryAuto(String name) {
        super(name);
    }

    private Swerve _swerve;

    @Override
    public void StartAuto() {
        _swerve = Robot.Instance.GetComponent("Swerve");
    }

    @Override
    public void AutoLoop() {
        var overrideValue = 0;

        _swerve.OverrideVerticalAxis(overrideValue);
    }

    @Override
    public void AutoEnd() {

    }
}
