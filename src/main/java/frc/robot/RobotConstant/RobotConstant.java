package frc.robot.RobotConstant;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorController.MotorControllerType;
import frc.robot.Library.FRC_3117_Tools.Math.Vector2d;

public class RobotConstant 
{
    // Swerve Front Right
    public static final MotorControllerConstant SWERVE_FRONT_RIGHT_STEER_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.SparkMax,
        1,
        false
    );
    public static final MotorControllerConstant SWERVE_FRONT_RIGHT_DRIVE_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.TalonFX,
        2,
        true
    );
    public static final int SWERVE_FRONT_RIGHT_STEER_ENCODER = 0;
    public static final double SWERVE_FRONT_RIGHT_STEER_ENCODER_OFFSET = 0.2360;
    public static final Vector2d SWERVE_FRONT_RIGHT_POSITION = new Vector2d(-0.78206, -0.62320);

    // Swerve Rear Right
    public static final MotorControllerConstant SWERVE_REAR_RIGHT_STEER_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.SparkMax,
        3,
        false
    );
    public static final MotorControllerConstant SWERVE_REAR_RIGHT_DRIVE_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.TalonFX,
        4,
        true
    );
    public static final int SWERVE_REAR_RIGHT_STEER_ENCODER = 1;
    public static final double SWERVE_REAR_RIGHT_STEER_ENCODER_OFFSET = 0.2512;
    public static final Vector2d SWERVE_REAR_RIGHT_POSITION = new Vector2d(0.78206, -0.62320);

    // Swerve Rear Left
    public static final MotorControllerConstant SWERVE_REAR_LEFT_STEER_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.SparkMax,
        5,
        false
    );
    public static final MotorControllerConstant SWERVE_REAR_LEFT_DRIVE_CONTROLLER =
    new MotorControllerConstant(
        MotorControllerType.TalonFX,
        6,
        false
    );
    public static final int SWERVE_REAR_LEFT_STEER_ENCODER = 2;
    public static final double SWERVE_REAR_LEFT_STEER_ENCODER_OFFSET = 0.4285;
    public static final Vector2d SWERVE_REAR_LEFT_POSITION = new Vector2d(0.78206, 0.62320);

    // Swerve Front Left
    public static final MotorControllerConstant SWERVE_FRONT_LEFT_STEER_CONTROLLER = 
    new MotorControllerConstant(
        MotorControllerType.SparkMax,
        7,
        false
    );
    public static final MotorControllerConstant SWERVE_FRONT_LEFT_DRIVE_CONTROLLER = 
    new MotorControllerConstant(
        MotorControllerType.TalonFX,
        8,
        false
    );
    public static final int SWERVE_FRONT_LEFT_STEER_ENCODER = 3;
    public static final double SWERVE_FRONT_LEFT_STEER_ENCODER_OFFSET = 0.1467;
    public static final Vector2d SWERVE_FRONT_LEFT_POSITION = new Vector2d(-0.78206, 0.62320);

    // Swerve Input
    public static final String SWERVE_TRANSLATON_X_AXIS = "Horizontal";
    public static final String SWERVE_TRANSLATION_Y_AXIS = "Vertical";
    public static final String SWERVE_ROTATION_AXIS = "Rotation";
}
