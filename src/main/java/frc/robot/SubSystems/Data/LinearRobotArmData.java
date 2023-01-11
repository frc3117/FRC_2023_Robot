package frc.robot.SubSystems.Data;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class LinearRobotArmData 
{
    public MotorController VerticalTranslationController;
    public MotorController ArmRotationController;
    public MotorController WristRotationController;

    public AnalogEncoder VerticalTranslationEncoder;
    public AnalogEncoder ArmRotationEncoder;
    public AnalogEncoder WristRotationEncoder;
}
