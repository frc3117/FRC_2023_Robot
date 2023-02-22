package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.SubSystems.Data.ManipulatorData;

public class Manipulator implements Component, Sendable
{
    public Manipulator(ManipulatorData data)
    {
        Data = data;

        SmartDashboard.putData("Arm", this);
    }

    public ManipulatorData Data;

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

    }

    @Override
    public void Print() 
    {

    }

    public double GetSegmentLocalAngle(int index)
    {
        return 0;
    }
    public double GetSegmentWorldAngle(int index)
    {
        return 0;
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
        return Vector3d.Zero();
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addDoubleProperty("SegmentCount", () -> Data.Segments.size(), null);

        for (var i = 0; i < Data.Segments.size(); i++)
        {
            var keyBase = "Segment_" + i;
            var index = i;

            builder.addDoubleProperty(keyBase + "/LocalAngle", () -> GetSegmentLocalAngle(index), null);
            builder.addDoubleProperty(keyBase + "/WorldAngle", () -> GetSegmentWorldAngle(index), null);
        }
    }
}
