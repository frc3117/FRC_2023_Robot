package frc.robot.SubSystems.Modules.Interfaces;

import frc.robot.SubSystems.Modules.Enum.ArmSegmentDoF;

public interface IArmSegment 
{
    public ArmSegmentDoF GetDoF();

    public double GetRawEncoderValue();
    public double GetEncoderValue();
}
