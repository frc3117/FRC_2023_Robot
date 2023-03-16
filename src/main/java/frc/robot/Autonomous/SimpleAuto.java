package frc.robot.Autonomous;

import frc.robot.Robot;
import frc.robot.Autonomous.AutonomousBase;
import frc.robot.Library.FRC_3117_Tools.Component.Swerve;
import frc.robot.Library.FRC_3117_Tools.Math.Timer;

public class SimpleAuto extends AutonomousBase
{
    public SimpleAuto(String name, double speed, double time)
    {
        _name = name;
        _speed = speed;
        _time = time;       
    }
    private Swerve _swerve;
    
    private String _name;
    private double _speed;
    private double _time;


    @Override
    public String GetName() 
    {
        return _name;
    }

    @Override
    public void StartAuto() 
    {
        _swerve = Robot.Instance.GetComponent("Swerve");


        
    }

    @Override
    public void AutoLoop() 
    {
        var OverrideValue = 0.;

        if (Timer.GetTimeSinceStart() <= _time)
        {
            OverrideValue = _speed;
        }
        else
        {
            OverrideValue = 0;
        }
        
        _swerve.OverrideVerticalAxis(OverrideValue); 
    }

    @Override
    public void AutoEnd() {
        // TODO Auto-generated method stub
        
    }
    
}
