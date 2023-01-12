package frc.robot.SubSystems;

import javax.xml.crypto.Data;

import edu.wpi.first.wpilibj.RobotController;
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
        

        Data.VerticalTranslationController.set(verticalTranslationAxis * 0.5);
        Data.ArmRotationController.set(armRotationAxis * 0.5);
        Data.WristRotationController.set(wristRotationAxis * 0.5);
        
        var verticalTranslationDistance = (Data.VerticalTranslationEncoder.get() / RobotController.getVoltage5V());
        var armRotationAngle = (Data.ArmRotationEncoder.get() / RobotController.getVoltage5V()) * 2 * Math.PI;
        var wristRotationAngle = (Data.WristRotationEncoder.get() / RobotController.getVoltage5V()) * 2 * Math.PI;
       
        System.out.println(armRotationAngle);
    }
    
}
