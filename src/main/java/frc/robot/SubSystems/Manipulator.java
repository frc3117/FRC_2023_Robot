package frc.robot.SubSystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Component.CAN.MultiDigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Component.Data.InputManager;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifestDevices;
import frc.robot.Library.FRC_3117_Tools.Math.*;
import frc.robot.Library.FRC_3117_Tools.Physics.Control.Interface.ControllerBase;
import frc.robot.Library.FRC_3117_Tools.Physics.Kinematics.DHParameters;
import frc.robot.Library.FRC_3117_Tools.Physics.Pose3d;
import frc.robot.Robot;
import frc.robot.SubSystems.Data.ManipulatorData;
import frc.robot.SubSystems.Data.ManipulatorPose;
import frc.robot.SubSystems.Data.ManipulatorPoseAngle;
import frc.robot.SubSystems.Data.ManipulatorSegmentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@FromManifest(EntryName = "manipulator", OnLoadMethod = "CreateFromManifest")
public class Manipulator implements Component, Sendable {
    public Manipulator(ManipulatorData data) {
        Data = data;

        _dhParams = Data.GetDHParameters();
        _jointsAngles = new double[data.Segments.size()];
        _joinsWorldAngles = new double[data.Segments.size()];
        _jointTargetAnglesMode = new int[data.Segments.size()];
        _segmentsEndEffectorPos = new Vector3d[data.Segments.size()];
        _motorsLimits = new Range[data.Segments.size()];
        _testPoseString = new String[data.Segments.size()];

        Arrays.fill(_jointTargetAnglesMode, 2);
        Arrays.fill(_testPoseString, "*");

        for (var i = 0; i < data.Segments.size(); i++)
            _motorsLimits[i] = new Range(0, 0);

        UpdatePositions();
        _jointsTargetAngles = _joinsWorldAngles.clone();

        _input = new Joystick(1);

        SmartDashboard.putData("Arm", this);
    }

    public static void CreateFromManifest(String name) {
        if (!RobotManifest.ManifestJson.HasEntry(name))
            return;

        var manifestObject = RobotManifest.ManifestJson.GetSubObject(name);

        var limitSwitches = new MultiDigitalInputCAN(manifestObject.GetInt("limitSwitchesID"));

        var segmentsManifestObject = manifestObject.GetSubObjectArray("segments");
        var segments = new ManipulatorSegmentData[segmentsManifestObject.length];
        for (var i = 0; i < segments.length; i++)
        {
            var segment = new ManipulatorSegmentData();
            var segmentManifestObject = segmentsManifestObject[i];

            segment.Motors = new MotorControllerGroup();
            for (var positive : RobotManifestDevices.GetMotorControllerArray(segmentManifestObject.GetStringArray("positiveMotors")))
                segment.Motors.AddPositiveController(positive);
            for (var negative : RobotManifestDevices.GetMotorControllerArray(segmentManifestObject.GetStringArray("negativeMotors")))
                segment.Motors.AddNegativeController(negative);

            segment.Encoder = RobotManifestDevices.GetAbsoluteEncoder(segmentManifestObject.GetString("encoder"));

            var paramManifestObject = segmentManifestObject.GetSubObject("DH");
            segment.DH = new DHParameters(
                    paramManifestObject.GetDouble("theta"),
                    paramManifestObject.GetDouble("alpha"),
                    paramManifestObject.GetDouble("r"),
                    paramManifestObject.GetDouble("d")
            );

            if (segmentManifestObject.HasEntry("minLimitSwitch"))
                segment.MinLimitSwitch = limitSwitches.GetDigitalInput(segmentManifestObject.GetInt("minLimitSwitch"), false);
            if (segmentManifestObject.HasEntry("maxLimitSwitch"))
                segment.MaxLimitSwitch = limitSwitches.GetDigitalInput(segmentManifestObject.GetInt("maxLimitSwitch"), false);

            segment.Controller = ControllerBase.Controllers.get(segmentManifestObject.GetString("controller"));
            segment.KeepAngle = segmentManifestObject.GetBoolean("keepAngle");

            segments[i] = segment;
        }

        var manipulator = new ManipulatorData();
        manipulator.LimitSwitches = limitSwitches;
        manipulator.Segments = new ArrayList<>(Arrays.asList(segments));

        Robot.Instance.AddComponent("Manipulator", new Manipulator(manipulator));
    }
    public ManipulatorData Data;

    private static double[] _anglesOffset = {
            2.42,
            2.75 - (Math.PI * 0.5),
            3.33,// - Math.PI,
            3.45,// - Math.PI,
            0
    };

    public HashMap<String, ManipulatorPose> Poses = new HashMap<>();

    private Joystick _input;
    private boolean _wasStowedPressed = false;

    private DHParameters[] _dhParams;
    private Matrix4X4[] _dhMatrices;

    private double[] _jointsAngles;
    private double[] _joinsWorldAngles;

    private double[] _jointsTargetAngles;
    private int[] _jointTargetAnglesMode;

    private Vector3d[] _segmentsEndEffectorPos;
    private Range[] _motorsLimits;

    private Vector3d _endEffectorTargetPos;

    private String[] _testPoseString;
    private String _poseName = "";

    private boolean _isStowed = true;
    private boolean _killed = false;

    @Override
    public void Awake() {

    }
    @Override
    public void Init() {
        UpdatePositions();
        _jointsTargetAngles = _joinsWorldAngles.clone();
    }

    @Override
    public void Disabled() {

    }

    @Override
    public void DoComponent() {
        // Do a forward kinematic loop to update the positions
        UpdatePositions();

        if (ToggleStowedPressed())
            ToggleStowed();
        else if (HumanPlayerShelfPressed()) {
            SetPose("human-player-shelf");
            SetStowedState(false);
        }
        if (InputManager.GetButtonDown("CubeHigh")) {
            SetPose("cube-high");
            SetStowedState(false);
        }
        else if (InputManager.GetButtonDown("CubeMid")) {
            SetPose("cube-mid");
            SetStowedState(false);
        }
        else if (InputManager.GetButtonDown("CubeLow")) {
            SetPose("ground-cube");
            SetStowedState(false);
        }
        else if (InputManager.GetButtonDown("ConeHigh")) {
            SetPose("cone-high");
            SetStowedState(false);
        }
        else if (InputManager.GetButtonDown("ConeMid")) {
            SetPose("cone-mid");
            SetStowedState(false);
        }
        else if (InputManager.GetButtonDown("ConeLow")) {
            SetPose("ground-cube");
            SetStowedState(false);
        }

        if (InputManager.GetButtonDown("PrintPose"))
            Print();

        /*if (InputManager.GetButtonDown("ResetPose"))
            SetPose("up");
        if (InputManager.GetButtonDown("TestPose"))
            SetTestPose();
        if (InputManager.GetButtonDown("SetPose"))
            SetPose(_poseName);*/

        // If the manipulator have been killed. Set the motor value to zero and return
        if (_killed)
        {
            Data.Segments.forEach(seg -> seg.Motors.Set(0));
            return;
        }

        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var seg = Data.Segments.get(i);
            if (!seg.Encoder.IsConnected())
            {
                try {
                    Kill();
                }
                catch (Exception e) {}
                return;
            }

            var limVal = 0.;
            switch (i) {
                case 0:
                case 4:
                    limVal = 0;
                    break;

                case 1:
                    limVal = 0.15;
                    break;

                case 2:
                case 3:
                    limVal = 0.3;
                    break;

            }

            // Apply the output limit if a limit switch is clicked
            var limit = new Range(-limVal, limVal);
            if (seg.MinLimitSwitch != null && seg.MinLimitSwitch.GetValue())
                limit.Min = 0;
            if (seg.MaxLimitSwitch != null && seg.MaxLimitSwitch.GetValue())
                limit.Max = 0;

            // Apply the output limit if a limit switch for the next segment is clicked, and it keeps its angle
            if (i != Data.Segments.size() - 1)
            {
                var nextSeg = Data.Segments.get(i + 1);
                if (nextSeg.KeepAngle)
                {
                    if (nextSeg.MinLimitSwitch != null && nextSeg.MinLimitSwitch.GetValue())
                        limit.Max = 0;
                    if (nextSeg.MaxLimitSwitch != null && nextSeg.MaxLimitSwitch.GetValue())
                        limit.Min = 0;
                }
            }

            _motorsLimits[i] = limit;

            var current = 0.;
            switch (_jointTargetAnglesMode[i])
            {
                case 0: // Local
                    current = GetSegmentLocalAngle(i);
                    break;

                case 1: // World
                    current = GetSegmentWorldAngle(i);
                    break;

                case 2: // Any
                    seg.Motors.Set(0);
                    continue;
            }

            //_jointTargetAngles[i] = Math.PI + (Math.sin((Math.PI * 0.25 * Timer.GetCurrentTime()) + (i-1) * (Math.PI / 2)) * 1);

            seg.Controller.Setpoint = _jointsTargetAngles[i];
            seg.Motors.Set(limit.Clamp(seg.Controller.Evaluate(current)));
        }

        // Apply the values to the motors
        Data.Segments.get(0).Motors.Set(0);
        Data.Segments.get(4).Motors.Set(0);
    }

    @Override
    public void Print() {
        System.out.println("------------------------------");
        for (var i = 0; i < Data.Segments.size(); i++) {
            var angles = "Local: ( " + (GetSegmentLocalAngle(i) * Mathf.kRAD2DEG) + " ) World: ( " + (GetSegmentWorldAngle(i) * Mathf.kRAD2DEG) + " )";
            System.out.println(angles);
        }
    }

    public void SetStowed() {
        SetStowedState(true);

        SetPose("stowed");
    }
    public void SetGround() {
        SetStowedState(false);

        SetPose("ground-cube");
    }
    public void ToggleStowed() {
        _isStowed = !_isStowed;

        if (_isStowed)
            SetStowed();
        else
            SetGround();
    }
    public void SetStowedState(boolean state) {
        _isStowed = state;
    }


    public void Kill() {
        _killed = true;

        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var seg = Data.Segments.get(i);
            seg.Motors.Set(0);
        }

        System.out.println("Manipulator Killed.");
    }

    public double GetSegmentEncoderAngle(int index) {
        return Data.Segments.get(index).Encoder.GetAngle() - _anglesOffset[index];
    }
    public double GetSegmentLocalAngle(int index) {
        return _jointsAngles[index];
    }
    public double GetSegmentWorldAngle(int index) {
        return _joinsWorldAngles[index];
    }

    private void UpdatePositions() {
        for (var i = 0; i < Data.Segments.size(); i++)
            _jointsAngles[i] = GetSegmentEncoderAngle(i);

        var localSum = 0.;
        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var localAngle = _jointsAngles[i];

            if (i == 0 || i == Data.Segments.size() - 1)
            {
                _joinsWorldAngles[i] = localAngle;
                continue;
            }

            _joinsWorldAngles[i] = (1.5 * Math.PI) - localAngle - localSum;
            localSum += localAngle;
        }

        _segmentsEndEffectorPos = DHParameters.ForwardKinematics(new Pose3d(), _jointsAngles, _dhParams);
    }

    public void SetTestPose() {
        try {
            SetPose(new ManipulatorPose(ManipulatorPoseAngle.ParseArray(_testPoseString)));
        }
        catch (Exception e) {
            System.out.println("Error in the test pose values.");
        }
    }

    public void SetPose(String poseName) {
        SetPose(ManipulatorPose.Poses.get(poseName));
    }
    public void SetPose(ManipulatorPose pose) {
        for (var i = 0; i < pose.Angles.length; i++) {
            var angle = pose.Angles[i];

            var mode = 0;
            var target = 0.;
            switch (angle.Mode)
            {
                case Any:
                    mode = 2;
                    target = 0;
                    break;

                case HoldLocal:
                    mode = 0;
                    target = GetSegmentLocalAngle(i);
                    break;

                case HoldWorld:
                    mode = 1;
                    target = GetSegmentWorldAngle(i);
                    break;

                case Follow:
                    mode = 2;
                    target = 0;
                    break;

                case TargetLocal:
                    mode = 0;
                    target = angle.Angle;
                    break;

                case TargetWorld:
                    mode = 1;
                    target = angle.Angle;
                    break;
            }

            _jointTargetAnglesMode[i] = mode;
            _jointsTargetAngles[i] = target;

            Data.Segments.get(i).Controller.Reset();
        }
    }

    public Vector3d GetEndEffectorTargetPos() {
        return _endEffectorTargetPos.Copy();
    }
    public void SetEndEffectorTargetPos(Vector3d target) {
        _endEffectorTargetPos = target;
    }
    
    public Vector3d GetEndEffectorPos() {
        return _segmentsEndEffectorPos[_segmentsEndEffectorPos.length - 1];
    }

    private boolean ToggleStowedPressed() {
        var pov = _input.getPOV();

        if (pov == 0)
        {
            if (_wasStowedPressed)
                return false;

            _wasStowedPressed = true;
            return true;
        }

        if(_wasStowedPressed)
            _wasStowedPressed = false;

        return false;
    }
    private boolean HumanPlayerShelfPressed() {
        return InputManager.GetButtonDown("HumanPlayer-0") ||
                InputManager.GetButtonDown("HumanPlayer-1") ||
                InputManager.GetButtonDown("HumanPlayer-2") ||
                InputManager.GetButtonDown("HumanPlayer-3");
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addIntegerProperty("SegmentCount", () -> Data.Segments.size(), null);
        builder.addBooleanProperty("Killed", () -> _killed, null);

        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var keyBase = "Segment_" + i;
            var index = i;

            builder.addBooleanProperty(keyBase + "/EncoderConnected", () -> Data.Segments.get(index).Encoder.IsConnected(), null);
            builder.addDoubleProperty(keyBase + "/EncoderAngleRaw", () -> Data.Segments.get(index).Encoder.GetRawValue(), null);
            builder.addDoubleProperty(keyBase + "/EncoderAngle", () -> GetSegmentEncoderAngle(index), null);
            builder.addDoubleProperty(keyBase + "/LocalAngle", () -> GetSegmentLocalAngle(index), null);
            builder.addDoubleProperty(keyBase + "/WorldAngle", () -> GetSegmentWorldAngle(index), null);

            builder.addDoubleProperty(keyBase + "/TargetAngle", () -> _jointsTargetAngles[index], (val) -> _jointsTargetAngles[index] = val);

            if (Data.Segments.get(i).MinLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MinLimitSwitch", () -> Data.Segments.get(index).MinLimitSwitch.GetValue(), null);
            if (Data.Segments.get(i).MaxLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MaxLimitSwitch", () -> Data.Segments.get(index).MaxLimitSwitch.GetValue(), null);

            builder.addDoubleProperty(keyBase + "/MotorMin", () -> _motorsLimits[index].Min, null);
            builder.addDoubleProperty(keyBase + "/MotorMax", () -> _motorsLimits[index].Max, null);

            builder.addDoubleProperty(keyBase + "/Position/X", () -> _segmentsEndEffectorPos[index].X, null);
            builder.addDoubleProperty(keyBase + "/Position/Y", () -> _segmentsEndEffectorPos[index].Y, null);
            builder.addDoubleProperty(keyBase + "/Position/Z", () -> _segmentsEndEffectorPos[index].Z, null);

            builder.addStringProperty("TestPose/Angle" + i, () -> _testPoseString[index], (val) -> _testPoseString[index] = val);
        }

        builder.addStringProperty("PoseName", () -> _poseName, (val) -> _poseName = val);
    }
}
