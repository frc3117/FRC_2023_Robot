package frc.robot.SubSystems.Modules.Enum;

public enum DoF 
{
    None,
    X,
    Y,
    Z;

    public ArmSegmentDoF ToRotation()
    {
        switch(this)
        {
            case X:
                return ArmSegmentDoF.X_Rotation;

            case Y:
                return ArmSegmentDoF.Y_Rotation;

            case Z:
                return ArmSegmentDoF.Z_Rotation;

            case None:
            default:
                return ArmSegmentDoF.None;
        }
    }
    public ArmSegmentDoF ToTranslation()
    {
        switch(this)
        {
            case X:
                return ArmSegmentDoF.X_Translation;

            case Y:
                return ArmSegmentDoF.Y_Translation;

            case Z:
                return ArmSegmentDoF.Z_Translation;

            case None:
            default:
                return ArmSegmentDoF.None;
        }
    }
}
