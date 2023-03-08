package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Component.CAN.DigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Physics.Controll.Interface.ControllerBase;
import frc.robot.Library.FRC_3117_Tools.Physics.Kinematics.DHParameters;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.Interface.AbsoluteEncoder;

public class ManipulatorSegmentData
{
    public MotorControllerGroup Motors;
    public AbsoluteEncoder Encoder;
    public DHParameters DH;

    public DigitalInputCAN MinLimitSwitch;
    public DigitalInputCAN MaxLimitSwitch;

    public ControllerBase Controller;

    public boolean KeepAngle;
}
