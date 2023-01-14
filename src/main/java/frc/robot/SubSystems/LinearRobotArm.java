package frc.robot.SubSystems;

import frc.robot.Library.FRC_3117_Tools.Component.Data.InputManager;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.Library.FRC_3117_Tools.Math.Mathf;
import frc.robot.SubSystems.Data.LinearRobotArmData;

public class LinearRobotArm implements Component
{
    public LinearRobotArm(LinearRobotArmData data) 
    { 
        Data = data;
    }

    public LinearRobotArmData Data;

    @Override
    public void Awake() 
    {

    }

    @Override
    public void Init() 
    {
        //Data.VerticalTranslationEncoder.reset();
        Data.ArmRotationEncoder.reset();
        Data.WristRotationEncoder.reset();
    }

    @Override
    public void Disabled() 
    {

    }

    @Override
    public void DoComponent() 
    {
        var verticalTranslationAxis = InputManager.GetAxis("VerticalTranslation");
        var armRotationAxis = InputManager.GetAxis("ArmRotation");
        var wristRotationAxis = InputManager.GetAxis("WristRotation");
        
        Data.VerticalTranslationController.set(verticalTranslationAxis * 0.5);
        Data.ArmRotationController.set(armRotationAxis * 0.5);
        Data.WristRotationController.set(wristRotationAxis * 0.5);

        Print();
    }
    
    @Override
    public void Print()
    {
        System.out.println(String.format("Vertical Translation Distance: %.2f m", GetTranslationDistance()));
        System.out.println(String.format("Arm Rotation Angle: %.2f °", GetArmRotationAngle() * Mathf.kRAD2DEG));
        System.out.println(String.format("Wrist Rotation Angle: %.2f °", GetWristRotationAngle() * Mathf.kRAD2DEG));
        System.out.println("----------------------------------------------");
    }

    public double GetTranslationRaw()
    {
        return Data.VerticalTranslationEncoder.get();
    }
    public double GetTranslationDistance()
    {
        return GetTranslationRaw() * Data.VerticalTranslationHeight;
    }

    public double GetArmRotationRaw()
    {
        return Data.ArmRotationEncoder.get();
    }
    public double GetArmRotationAngle()
    {
        return GetArmRotationRaw() * 2 * Math.PI;
    }

    public double GetWristRotationRaw()
    {
        return Data.WristRotationEncoder.get();
    }
    public double GetWristRotationAngle()
    {
        return GetWristRotationRaw() * 2 * Math.PI;
    }
}
