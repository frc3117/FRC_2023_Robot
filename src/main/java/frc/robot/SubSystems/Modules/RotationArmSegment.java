package frc.robot.SubSystems.Modules;

import frc.robot.SubSystems.Modules.Data.RotationArmSegmentData;
import frc.robot.SubSystems.Modules.Enum.ArmSegmentDoF;
import frc.robot.SubSystems.Modules.Interfaces.IArmSegment;

public class RotationArmSegment implements IArmSegment
{
    public RotationArmSegment(RotationArmSegmentData data)
    {
        Data = data;
    }

    public RotationArmSegmentData Data;

    @Override
    public ArmSegmentDoF GetDoF()
    {
        return Data.RotationDof.ToRotation();
    }

    @Override
    public double GetRawEncoderValue()
    {
        return Data.RotationEncoder.get();
    }
    @Override
    public double GetEncoderValue()
    {
        return GetRawEncoderValue();
    }
}
