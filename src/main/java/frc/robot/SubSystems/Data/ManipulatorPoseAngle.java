package frc.robot.SubSystems.Data;

public class ManipulatorPoseAngle {
    public ManipulatorPoseAngle(ManupulatorPoseAngleMode mode) {
        this(mode, 0);
    }
    public ManipulatorPoseAngle(ManupulatorPoseAngleMode mode, double angle) {
        Mode = mode;
        Angle = angle;
    }

    public ManupulatorPoseAngleMode Mode;
    public double Angle;

    public static ManipulatorPoseAngle Parse(String str) {
        var split = str.split("_", 2);

        ManupulatorPoseAngleMode mode;
        var angle = split.length == 1 ? 0. : Double.parseDouble(split[1]);
        switch (split[0]) {
            case "*":
                mode = ManupulatorPoseAngleMode.Any;
                break;

            case "=l":
                mode = ManupulatorPoseAngleMode.HoldLocal;
                break;

            case "=w":
                mode = ManupulatorPoseAngleMode.HoldWorld;
                break;

            case ">":
                mode = ManupulatorPoseAngleMode.Follow;
                break;

            case "@l":
                mode = ManupulatorPoseAngleMode.TargetLocal;
                break;

            case "@w":
            default:
                mode = ManupulatorPoseAngleMode.TargetWorld;
                break;
        }

        return new ManipulatorPoseAngle(mode, angle);
    }
    public static ManipulatorPoseAngle[] ParseArray(String... str) {
        var arr = new ManipulatorPoseAngle[str.length];
        for (var i = 0; i < str.length; i++)
        {
            arr[i] = Parse(str[i]);
        }

        return arr;
    }

    public enum ManupulatorPoseAngleMode {
        Any,
        HoldLocal,
        HoldWorld,
        Follow,
        TargetLocal,
        TargetWorld
    }
}
