package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Tupple.Pair;
import frc.robot.Library.FRC_3117_Tools.Component.Data.WheelData;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifestDevices;
import frc.robot.Library.FRC_3117_Tools.Math.Matrix4X4;
import frc.robot.Library.FRC_3117_Tools.Math.Quaternion;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.Library.FRC_3117_Tools.Physics.Kinematics.DHParameters;
import frc.robot.Library.FRC_3117_Tools.Physics.TransformationMatrix;
import frc.robot.SubSystems.Data.ManipulatorData;
import frc.robot.SubSystems.Data.ManipulatorSegmentData;

import java.util.ArrayList;
import java.util.Arrays;

@FromManifest(EntryName = "manipulator")
public class Manipulator implements Component, Sendable
{
    public Manipulator(ManipulatorData data)
    {
        Data = data;
        _segmentsEndEffectorPos = new Vector3d[data.Segments.size()];
        for (var i = 0; i < _segmentsEndEffectorPos.length; i++)
        {
            _segmentsEndEffectorPos[i] = Vector3d.Zero();
        }

        SmartDashboard.putData("Arm", this);
    }

    public static Pair<String, Component> CreateFromManifest(String name) {
        var manifestObject = RobotManifest.ManifestJson.GetSubObject(name);

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

            segments[i] = segment;
        }

        var manipulator = new ManipulatorData();
        manipulator.Segments = new ArrayList<>(Arrays.asList(segments));

        return new Pair<>("Manipulator", new Manipulator(manipulator));
    }

    public ManipulatorData Data;

    private Vector3d[] _segmentsEndEffectorPos;

    private Vector3d _endEffectorTargetPos;

    @Override
    public void Awake() 
    {

    }

    @Override
    public void Init() 
    {

    }

    @Override
    public void Disabled() 
    {

    }

    @Override
    public void DoComponent() 
    {
        var params = new DHParameters[Data.Segments.size()];
        for (var i = 0; i < params.length; i++)
        {
            var dh = Data.Segments.get(i).DH;
            dh.Theta = GetSegmentLocalAngle(i);

            params[i] = dh;
        }

        var matrices = DHParameters.GetMatrices(params);
        var mat = new Matrix4X4(new TransformationMatrix(Vector3d.Zero(), Quaternion.Identity()));
        for (var i = 0; i < matrices.length; i++)
        {
            mat = mat.mult(matrices[i]);
            _segmentsEndEffectorPos[i] = mat.GetPosition();
        }
    }

    @Override
    public void Print() 
    {

    }

    public double GetSegmentEncoderAngle(int index)
    {
        return Data.Segments.get(index).Encoder.GetAngle();
    }
    public double GetSegmentLocalAngle(int index)
    {
        return GetSegmentEncoderAngle(index);
    }
    public double GetSegmentWorldAngle(int index)
    {
        return GetSegmentEncoderAngle(index);
    }

    public Vector3d GetEndEffectorTargetPos()
    {
        return _endEffectorTargetPos.Copy();
    }
    public void SetEndEffectorTargetPos(Vector3d target)
    {
        _endEffectorTargetPos = target;
    }
    
    public Vector3d GetEndEffectorPos()
    {
        return _segmentsEndEffectorPos[_segmentsEndEffectorPos.length - 1];
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addIntegerProperty("SegmentCount", () -> Data.Segments.size(), null);

        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var keyBase = "Segment_" + i;
            var index = i;

            builder.addDoubleProperty(keyBase + "/EncoderAngle", () -> GetSegmentEncoderAngle(index), null);
            builder.addDoubleProperty(keyBase + "/LocalAngle", () -> GetSegmentLocalAngle(index), null);
            builder.addDoubleProperty(keyBase + "/WorldAngle", () -> GetSegmentWorldAngle(index), null);

            builder.addDoubleProperty(keyBase + "/Position/X", () -> _segmentsEndEffectorPos[index].X, null);
            builder.addDoubleProperty(keyBase + "/Position/Y", () -> _segmentsEndEffectorPos[index].Y, null);
            builder.addDoubleProperty(keyBase + "/Position/Z", () -> _segmentsEndEffectorPos[index].Z, null);
        }
    }
}
