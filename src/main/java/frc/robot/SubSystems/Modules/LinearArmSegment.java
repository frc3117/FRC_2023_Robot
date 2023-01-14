package frc.robot.SubSystems.Modules;

import frc.robot.SubSystems.Modules.Data.LinearArmSegmentData;
import frc.robot.SubSystems.Modules.Enum.ArmSegmentDoF;
import frc.robot.SubSystems.Modules.Interfaces.IArmSegment;

public class LinearArmSegment implements IArmSegment
{
    public LinearArmSegment(LinearArmSegmentData data)
    {
        Data = data;
    }

    public LinearArmSegmentData Data;

    @Override
    public ArmSegmentDoF GetDoF()
    {
        return Data.LinearDoF.ToTranslation();
    }

    @Override
    public double GetRawEncoderValue()
    {
        return Data.LinearEncoder.get();
    }
    @Override
    public double GetEncoderValue()
    {
        return GetRawEncoderValue();
    }
}
