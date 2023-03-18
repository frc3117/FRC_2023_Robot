package frc.robot.SubSystems.Data;

public class ManipulatorPoseAngle {
    public ManipulatorPoseAngle(ManipulatorPoseAngleMode mode) {
        this(mode, 0);
    }
    public ManipulatorPoseAngle(ManipulatorPoseAngleMode mode, double angle) {
        Mode = mode;
        Angle = angle;
    }

    public ManipulatorPoseAngleMode Mode;
    public double Angle;

    public static ManipulatorPoseAngle Parse(String str) {
        var split = str.split("_", 2);

        ManipulatorPoseAngleMode mode;
        var angle = split.length == 1 ? 0. : Double.parseDouble(split[1]);
        switch (split[0]) {
            case "*":
                mode = ManipulatorPoseAngleMode.Any;
                break;

            case "=l":
                mode = ManipulatorPoseAngleMode.HoldLocal;
                break;

            case "=w":
                mode = ManipulatorPoseAngleMode.HoldWorld;
                break;

            case ">":
                mode = ManipulatorPoseAngleMode.Follow;
                break;

            case "@l":
                mode = ManipulatorPoseAngleMode.TargetLocal;
                break;

            case "@w":
            default:
                mode = ManipulatorPoseAngleMode.TargetWorld;
                break;

            case "dl":
                mode = ManipulatorPoseAngleMode.DeltaLocal;
                break;

            case "dw":
                mode = ManipulatorPoseAngleMode.DeltaWorld;
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

    public enum ManipulatorPoseAngleMode {
        Any,
        HoldLocal,
        HoldWorld,
        Follow,
        TargetLocal,
        TargetWorld,
        DeltaLocal,
        DeltaWorld
    }
}
