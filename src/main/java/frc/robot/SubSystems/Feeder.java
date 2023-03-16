package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Component.Data.InputManager;
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

    private boolean _holdingCone;
    private boolean _holdingCube;

    @Override
    public void Awake() {

    }

    @Override
    public void Init() {

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
            if (InputManager.GetButtonUp("Intake"))
                _holdingObject = false;
            else if (Input.GetButton("Intake"))
                motorOutput = -0.25;
            else
                motorOutput = 0.1;//HoldEquation(Data.HoldConeAmplitude, Data.HoldConeFreq, Data.HoldConeCutoff);
        }
        else
        {
            if (Input.GetButton("Intake"))
                motorOutput = 0.50;
            else if (Input.GetButton("Hold"))
                _holdingObject = true;
        }

        /*if (Input.GetButton("Intake"))
            motorOutput = 0.5;
        else if (_holdingCone)
            motorOutput = HoldEquation(Data.HoldConeAmplitude, Data.HoldConeFreq, Data.HoldConeCutoff);
        else if (_holdingCube)
            motorOutput = HoldEquation(Data.HoldCubeAmplitude, Data.HoldCubeFreq, Data.HoldCubeCutoff);*/

        Data.Motor.Set(motorOutput);
    }

    @Override
    public void Print() {

    }

    private double HoldEquation(double amplitude, double frequency, double cutoff) {
        var val = amplitude * Math.sin(Mathf.TAU * frequency * Timer.GetCurrentTime());
        return val < cutoff ? 0 : val;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("ConeHold/Amplitude", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);
        builder.addDoubleProperty("ConeHold/Frequency", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);
        builder.addDoubleProperty("ConeHold/Cutoff", () -> Data.HoldConeAmplitude, (val) -> Data.HoldConeAmplitude = val);

        builder.addDoubleProperty("CubeHold/Amplitude", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
        builder.addDoubleProperty("CubeHold/Frequency", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
        builder.addDoubleProperty("CubeHold/Cutoff", () -> Data.HoldCubeAmplitude, (val) -> Data.HoldCubeAmplitude = val);
    }
}
