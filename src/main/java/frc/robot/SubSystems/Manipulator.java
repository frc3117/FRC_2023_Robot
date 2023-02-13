package frc.robot.SubSystems;

import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.SubSystems.Data.ManipulatorData;

public class Manipulator implements Component
{
    public Manipulator(ManipulatorData data)
    {
        Data = data;
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
}
