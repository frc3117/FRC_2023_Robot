package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifestDevices;
import frc.robot.Library.FRC_3117_Tools.Math.Mathf;
import frc.robot.Library.FRC_3117_Tools.Math.Timer;
import frc.robot.Robot;
import frc.robot.SubSystems.Data.FeederData;

@FromManifest(EntryName = "feeder", OnLoadMethod = "CreateFromManifest")
public class Feeder implements Component, Sendable {
    public Feeder(FeederData data) {
        Data = data;
    }
    public static void CreateFromManifest(String entryName) {
        if (!RobotManifest.ManifestJson.HasEntry(entryName))
            return;

        var manifestObject = RobotManifest.ManifestJson.GetSubObject(entryName);

        var feederData = new FeederData();
        feederData.Motor = RobotManifestDevices.GetMotorController(manifestObject.GetString("motor"));

        Robot.Instance.AddComponent("Feeder", new Feeder(feederData));
    }

    public FeederData Data;

    private boolean _holdingObject = true;

    private boolean _intakePressedLast = false;
    private boolean _overrideIntake = false;

    private boolean _holdPressedLast = false;
    private boolean _overrideHold = false;

    @Override
    public void Awake() {

    }

    @Override
    public void Init() {
        _intakePressedLast = false;
        _overrideIntake = false;

        _holdPressedLast = false;
        _overrideHold = false;
    }

    @Override
    public void Disabled() {

    }

    // eq = Ampl * sin(2 * pi * Freq * time)
    // if (eq < cuttoff) 0
    // else stay
    @Override
    public void DoComponent() {
        var motorOutput = 0.;

        if (_holdingObject) {
            if (IntakePressedUp())
                _holdingObject = false;
            else if (IntakePressed())
                motorOutput = -0.30;
            else
                motorOutput = 0.09;//HoldEquation(Data.HoldConeAmplitude, Data.HoldConeFreq, Data.HoldConeCutoff);
        }
        else
        {
            if (IntakePressed())
                motorOutput = 0.47;
            else if (Input.GetButton("Hold"))
                _holdingObject = true;
        }

        Data.Motor.Set(motorOutput);

        _intakePressedLast = IntakePressed();
        _overrideIntake = false;

        _holdPressedLast = HoldPressed();
        _overrideHold = false;
    }

    @Override
    public void Print() {

    }

    public boolean IntakePressed() {
        return Input.GetButton("Intake") || _overrideIntake;
    }
    public boolean IntakePressedUp() {
        return _intakePressedLast && !IntakePressed();
    }

    public boolean HoldPressed() {
        return Input.GetButton("Hold") || _overrideHold;
    }

    public void OverrideIntake() {
        _overrideIntake = true;
    }
    public void OverrideHold() {
        _overrideHold = true;
    }

    private double HoldEquation(double amplitude, double frequency, double cutoff) {
        var val = amplitude * Math.sin(Mathf.TAU * frequency * Timer.GetCurrentTime());
        return val < cutoff ? 0 : val;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("IsHoldingObject", () -> _holdingObject, null);

        builder.addDoubleProperty("ConeHold/Amplitude", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);
        builder.addDoubleProperty("ConeHold/Frequency", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);
        builder.addDoubleProperty("ConeHold/Cutoff", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);

        builder.addDoubleProperty("CubeHold/Amplitude", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
        builder.addDoubleProperty("CubeHold/Frequency", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
        builder.addDoubleProperty("CubeHold/Cutoff", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
    }
}
