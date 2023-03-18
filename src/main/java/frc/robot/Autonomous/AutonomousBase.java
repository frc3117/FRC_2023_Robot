package frc.robot.Autonomous;

public abstract class AutonomousBase {
    public AutonomousBase(String name) {
        _name = name;
    }

    protected String _name;

    public String GetName()
    {
        return _name;
    }
    
    public abstract void StartAuto();
    public abstract void AutoLoop();
    public abstract void AutoEnd();
}
