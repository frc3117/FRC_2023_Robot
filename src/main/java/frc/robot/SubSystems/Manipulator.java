package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Component.CAN.MultiDigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Tupple.Pair;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifestDevices;
import frc.robot.Library.FRC_3117_Tools.Math.*;
import frc.robot.Library.FRC_3117_Tools.Physics.Controll.Interface.ControllerBase;
import frc.robot.Library.FRC_3117_Tools.Physics.Kinematics.DHParameters;
import frc.robot.Library.FRC_3117_Tools.Physics.Pose3d;
import frc.robot.SubSystems.Data.ManipulatorData;
import frc.robot.SubSystems.Data.ManipulatorSegmentData;

import java.util.ArrayList;
import java.util.Arrays;

@FromManifest(EntryName = "manipulator")
public class Manipulator implements Component, Sendable {
    public Manipulator(ManipulatorData data) {
        Data = data;

        _dhParams = Data.GetDHParameters();
        _jointsAngles = new double[data.Segments.size()];
        _segmentsEndEffectorPos = new Vector3d[data.Segments.size()];

        UpdatePositions();

        SmartDashboard.putNumber("MotorOutput", 0.1);
        SmartDashboard.putData("Arm", this);
    }

    public static Pair<String, Component> CreateFromManifest(String name) {
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
                segment.MinLimitSwitch = limitSwitches.GetDigitalInput(segmentManifestObject.GetInt("minLimitSwitch"), true);
            if (segmentManifestObject.HasEntry("maxLimitSwitch"))
                segment.MaxLimitSwitch = limitSwitches.GetDigitalInput(segmentManifestObject.GetInt("maxLimitSwitch"), true);

            segment.Controller = ControllerBase.Controllers.get(segmentManifestObject.GetString("controller"));
            segment.KeepAngle = segmentManifestObject.GetBoolean("keepAngle");

            segments[i] = segment;
        }

        var manipulator = new ManipulatorData();
        manipulator.LimitSwitches = limitSwitches;
        manipulator.Segments = new ArrayList<>(Arrays.asList(segments));

        return new Pair<>("Manipulator", new Manipulator(manipulator));
    }
    public ManipulatorData Data;


    private DHParameters[] _dhParams;
    private Matrix4X4[] _dhMatrices;

    private double[] _jointsAngles;
    private Vector3d[] _segmentsEndEffectorPos;

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

        // If the manipulator have been killed. Set the motor value to zero and return
        if (_killed)
        {
            Data.Segments.forEach(seg -> seg.Motors.Set(0));
            return;
        }

        var motorsLimits = new Range[Data.Segments.size()];
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

            motorsLimits[i] = limit;
        }

        var val = 0;
        if (Input.GetButton("UpSegment"))
            val++;
        if (Input.GetButton("DownSegment"))
            val--;

        var output0 = 0.;
        var output1 = 0.;
        var output2 = 0.;
        var output3 = 0.;

        var force = val * SmartDashboard.getNumber("MotorOutput", 0.1);
        if (Input.GetButton("SelectFirst"))
            output1 = force;
        else if (Input.GetButton("SelectSecond"))
            output2 = force;
        else if (Input.GetButton("SelectThird"))
            output3 = force;

        // Apply the values to the motors
        Data.Segments.get(0).Motors.Set(motorsLimits[0].Clamp(output0));
        Data.Segments.get(1).Motors.Set(motorsLimits[1].Clamp(output1));
        Data.Segments.get(2).Motors.Set(motorsLimits[2].Clamp(output2));
        Data.Segments.get(3).Motors.Set(motorsLimits[3].Clamp(output3));
    }

    @Override
    public void Print() {

    }

    public void Kill()
    {
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

            builder.addDoubleProperty(keyBase + "/EncoderAngleRaw", () -> Data.Segments.get(index).Encoder.GetRawValue(), null);
            builder.addDoubleProperty(keyBase + "/EncoderAngle", () -> GetSegmentEncoderAngle(index), null);
            builder.addDoubleProperty(keyBase + "/LocalAngle", () -> GetSegmentLocalAngle(index), null);
            builder.addDoubleProperty(keyBase + "/WorldAngle", () -> GetSegmentWorldAngle(index), null);

            if (Data.Segments.get(i).MinLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MinLimitSwitch", () -> Data.Segments.get(index).MinLimitSwitch.GetValue(), null);
            if (Data.Segments.get(i).MaxLimitSwitch != null)
                builder.addBooleanProperty(keyBase + "/MaxLimitSwitch", () -> Data.Segments.get(index).MaxLimitSwitch.GetValue(), null);

            builder.addDoubleProperty(keyBase + "/Position/X", () -> _segmentsEndEffectorPos[index].X, null);
            builder.addDoubleProperty(keyBase + "/Position/Y", () -> _segmentsEndEffectorPos[index].Y, null);
            builder.addDoubleProperty(keyBase + "/Position/Z", () -> _segmentsEndEffectorPos[index].Z, null);
        }
    }
}
