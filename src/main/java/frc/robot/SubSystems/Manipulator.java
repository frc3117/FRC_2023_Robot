package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Component.CAN.MultiDigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
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
        _segmentsEndEffectorPos = new Vector3d[data.Segments.size()];
        _motorsLimits = new Range[data.Segments.size()];

        for (var i = 0; i < data.Segments.size(); i++)
        {
            _motorsLimits[i] = new Range(0, 0);
        }

        UpdatePositions();

        SmartDashboard.putNumber("MotorOutput", 0.1);
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

    public HashMap<String, ManipulatorPose> Poses = new HashMap<>();

    private DHParameters[] _dhParams;
    private Matrix4X4[] _dhMatrices;

    private double[] _jointsAngles;
    private Vector3d[] _segmentsEndEffectorPos;
    private Range[] _motorsLimits;

    private Vector3d _endEffectorTargetPos;

    private boolean _killed = false;

    @Override
    public void Awake() {

    }
    @Override
    public void Init() {

    }

    @Override
    public void Disabled() {

    }

    @Override
    public void DoComponent() {
        // Do a forward kinematic loop to update the positions
        UpdatePositions();

        if (InputManager.GetButtonDown("SaveButton"))
            Print();

        //if (Timer.GetFrameCount() % 100 == 0)
            //System.out.println(DHParameters.GetJacobianMatrix(new Pose3d(Vector3d.Zero(), Quaternion.Identity()), _jointsAngles, _dhParams));

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
                    //Kill();
                }
                catch (Exception e) {}
                return;
            }

            // Apply the output limit if a limit switch is clicked
            var limit = new Range(-1, 1);
            if (seg.MinLimitSwitch != null && seg.MinLimitSwitch.GetValue())
                limit.Min = 0;
            if (seg.MaxLimitSwitch != null && seg.MaxLimitSwitch.GetValue())
                limit.Max = 0;

            // Apply the output limit if a limit switch for the next segment is clicked and it keep its angle
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
        }

        var motorOutput = SmartDashboard.getNumber("MotorOutput", 0.1);

        var rotatingBaseOutput = motorOutput * Input.GetAxis("RotatingBase");
        var segment0Output = motorOutput * Input.GetAxis("Segment0");
        var segment1Output = motorOutput * Input.GetAxis("Segment1");
        var segment2Output = motorOutput * Input.GetAxis("Segment2");
        var twistOutput = motorOutput * Input.GetAxis("Twist");

        // Apply the values to the motors
        Data.Segments.get(0).Motors.Set(_motorsLimits[0].Clamp(rotatingBaseOutput));
        Data.Segments.get(1).Motors.Set(_motorsLimits[1].Clamp(segment0Output));
        Data.Segments.get(2).Motors.Set(_motorsLimits[2].Clamp(segment1Output));
        Data.Segments.get(3).Motors.Set(_motorsLimits[3].Clamp(segment2Output));
        Data.Segments.get(4).Motors.Set(_motorsLimits[4].Clamp(twistOutput));
    }

    @Override
    public void Print() {
        System.out.println("------------------------------");
        for (var i = 0; i < Data.Segments.size(); i++) {
            System.out.println(Data.Segments.get(i).Encoder.GetRawAngle());
        }
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
        return Data.Segments.get(index).Encoder.GetAngle();
    }
    public double GetSegmentLocalAngle(int index) {
        return GetSegmentEncoderAngle(index);
    }
    public double GetSegmentWorldAngle(int index) {
        return GetSegmentEncoderAngle(index);
    }

    private void UpdatePositions() {
        for (var i = 0; i < Data.Segments.size(); i++)
            _jointsAngles[i] = GetSegmentLocalAngle(i);

        _segmentsEndEffectorPos = DHParameters.ForwardKinematics(new Pose3d(), _jointsAngles, _dhParams);
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

            if (Data.Segments.get(i).MinLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MinLimitSwitch", () -> Data.Segments.get(index).MinLimitSwitch.GetValue(), null);
            if (Data.Segments.get(i).MaxLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MaxLimitSwitch", () -> Data.Segments.get(index).MaxLimitSwitch.GetValue(), null);

            builder.addDoubleProperty(keyBase + "/MotorMin", () -> _motorsLimits[index].Min, null);
            builder.addDoubleProperty(keyBase + "/MotorMax", () -> _motorsLimits[index].Max, null);

            builder.addDoubleProperty(keyBase + "/Position/X", () -> _segmentsEndEffectorPos[index].X, null);
            builder.addDoubleProperty(keyBase + "/Position/Y", () -> _segmentsEndEffectorPos[index].Y, null);
            builder.addDoubleProperty(keyBase + "/Position/Z", () -> _segmentsEndEffectorPos[index].Z, null);
        }
    }
}
