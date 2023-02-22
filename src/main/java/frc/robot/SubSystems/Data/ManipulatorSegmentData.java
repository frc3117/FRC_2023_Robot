package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.Interface.AbsoluteEncoder;

public class ManipulatorSegmentData
{
    public Vector3d Axis;
    public double Length;
    public MotorControllerGroup Motors;
    public AbsoluteEncoder Encoder;
}
