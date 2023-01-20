package frc.robot.SubSystems;

import frc.robot.Library.FRC_3117_Tools.Component.Data.InputManager;
import frc.robot.Library.FRC_3117_Tools.Component.Data.Input.XboxButton;
import frc.robot.Library.FRC_3117_Tools.Interface.Component;
import frc.robot.SubSystems.Data.EncoderTestData;


public class EncoderTest implements Component
{
     public EncoderTest( EncoderTestData data) 
    { 
        Data = data;
    }
    
    public EncoderTestData Data;
    
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
        if(InputManager.GetButton("ShaftRotation"))
        {
            Data.ShaftRotationController.set(0.5);
        }
        else
        {
            Data.ShaftRotationController.set(0);
        }
        
    }

    @Override
    public void Print() {
        // TODO Auto-generated method stub
        
    }
    public double GetShaftRotationRaw()
    {
        return Data.EncoderTestPWM.get();
    }
    public double GetShaftRotationAngle()
    {
        return GetShaftRotationRaw() * 2 * Math.PI;
    }
}



