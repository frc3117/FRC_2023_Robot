package frc.robot.SubSystems;

import frc.robot.Library.FRC_3117_Tools.Component.Data.InputManager;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.SubSystems.Data.LinearRobotArmData;

public class LinearRobotArm implements Component
{
    public LinearRobotArm(LinearRobotArmData data) 
    { 
        Data = data;
    }

    public LinearRobotArmData Data;

    @Override
    public void Awake() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Disabled() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void DoComponent() 
    {
        var verticalTranslationAxis = InputManager.GetAxis("VerticalTranslation");
        var armRotationAxis = InputManager.GetAxis("ArmRotation");
        var wristRotationAxis = InputManager.GetAxis("WristRotation");

        Data.VerticalTranslationController.set(0.2 * verticalTranslationAxis);
        Data.ArmRotationController.set(0.2 * armRotationAxis);
        Data.WristRotationController.set(0.2 * wristRotationAxis);
    
        System.out.println("Test");
    }
    
}
