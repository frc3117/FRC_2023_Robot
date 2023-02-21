package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.Interface.AbsoluteEncoder;

public class TestArm implements Component, Sendable
{
    public TestArm(AbsoluteEncoder... encoders)
    {
        Encoders = encoders;
        SmartDashboard.putData("Arm",this);
    }

    public  AbsoluteEncoder[] Encoders;

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

    @Override
    public void initSendable(SendableBuilder builder)
    {
        for (var i = 0; i < Encoders.length; i++)
        {
            var encoder = Encoders[i];
            builder.addDoubleProperty("Encoder_" + i, encoder::GetAngle, null);
        }
    }
}
