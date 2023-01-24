package frc.robot.RobotConstant;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorController;
import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorController.MotorControllerType;

public class MotorControllerConstant 
{
    public MotorControllerConstant() {}
    public MotorControllerConstant(MotorControllerType type, int id, boolean inverted)
    {
        this(type, id, inverted, true);
    }
    public MotorControllerConstant(MotorControllerType type, int id, boolean inverted, boolean brushless)
    {
        Type = type;
        Id = id;
        Inverted = inverted;
        Brushless = brushless;
    }

    public MotorControllerType Type;
    public int Id;
    public boolean Inverted;
    public boolean Brushless;

    public MotorController ToMotorController()
    {
        var controller = new MotorController(Type, Id, Brushless);
        controller.SetInverted(Inverted);

        return controller;
    }
}
