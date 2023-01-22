package frc.robot;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorController.MotorControllerType;
import frc.robot.Library.FRC_3117_Tools.Math.Vector2d;

public class RobotConstant 
{
    public static final MotorControllerType SWERVE_FRONT_LEFT_STEER_TYPE = MotorControllerType.SparkMax;
    public static final int SWERVE_FRONT_LEFT_STEER = 7;
    public static final int SWERVE_FRONT_LEFT_STEER_ENCODER = 3;
    public static final double SWERVE_FRONT_LEFT_STEER_ENCODER_OFFSET = 0.1456;
    public static final MotorControllerType SWERVE_FRONT_LEFT_DRIVE_TYPE = MotorControllerType.TalonFX;
    public static final int SWERVE_FRONT_LEFT_DRIVE = 8;
    public static final Vector2d SWERVE_FRONT_LEFT_POSITION = new Vector2d(-0.62320, 0.78206);

    public static final MotorControllerType SWERVE_FRONT_RIGHT_STEER_TYPE = MotorControllerType.SparkMax;
    public static final int SWERVE_FRONT_RIGHT_STEER = 1;
    public static final int SWERVE_FRONT_RIGHT_STEER_ENCODER = 0;
    public static final double SWERVE_FRONT_RIGHT_STEER_ENCODER_OFFSET = 0.4708;
    public static final MotorControllerType SWERVE_FRONT_RIGHT_DRIVE_TYPE = MotorControllerType.TalonFX;
    public static final int SWERVE_FRONT_RIGHT_DRIVE = 2;
    public static final Vector2d SWERVE_FRONT_RIGHT_POSITION = new Vector2d(0.62320, 0.78206);

    public static final MotorControllerType SWERVE_REAR_LEFT_STEER_TYPE = MotorControllerType.SparkMax;
    public static final int SWERVE_REAR_LEFT_STEER = 5;
    public static final int SWERVE_REAR_LEFT_STEER_ENCODER = 2;
    public static final double SWERVE_REAR_LEFT_STEER_ENCODER_OFFSET = 0.4294;
    public static final MotorControllerType SWERVE_REAR_LEFT_DRIVE_TYPE = MotorControllerType.TalonFX;
    public static final int SWERVE_REAR_LEFT_DRIVE = 6;
    public static final Vector2d SWERVE_REAR_LEFT_POSITION = new Vector2d(-0.62320, -0.78206);

    public static final MotorControllerType SWERVE_REAR_RIGHT_STEER_TYPE = MotorControllerType.SparkMax;
    public static final int SWERVE_REAR_RIGHT_STEER = 3;
    public static final int SWERVE_REAR_RIGHT_STEER_ENCODER = 1;
    public static final double SWERVE_REAR_RIGHT_STEER_ENCODER_OFFSET = 0.2515;
    public static final MotorControllerType SWERVE_REAR_RIGHT_DRIVE_TYPE = MotorControllerType.TalonFX;
    public static final int SWERVE_REAR_RIGHT_DRIVE = 4;
    public static final Vector2d SWERVE_REAR_RIGHT_POSITION = new Vector2d(0.62320, -0.78206);

    public static final String SWERVE_TRANSLATON_X_AXIS = "Horizontal";
    public static final String SWERVE_TRANSLATION_Y_AXIS = "Vertical";
    public static final String SWERVE_ROTATION_AXIS = "Rotation";
}
