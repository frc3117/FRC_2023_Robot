package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;

import java.util.HashMap;

@FromManifest(EntryName = "poses", OnLoadMethod = "OnManifestLoad")
public class ManipulatorPose {
    public ManipulatorPose(ManipulatorPoseMode mode, double... jointsAngles) {
        Mode = mode;
        JointsAngles = jointsAngles;
    }

    public static HashMap<String, ManipulatorPose> Poses = new HashMap<>();

    public ManipulatorPoseMode Mode;
    public double[] JointsAngles;

    public static void OnManifestLoad(String entryName) {
        if (!RobotManifest.ManifestJson.HasEntry(entryName))
            return;

        var posesManifestEntry = RobotManifest.ManifestJson.GetSubObject(entryName);
        for (var poseEntry : posesManifestEntry.GetSubObjects().entrySet())
        {
            var mode = ManipulatorPoseMode.valueOf(poseEntry.getValue().GetString("mode"));
            var jointsAngles = poseEntry.getValue().GetDoubleArray("angles");

            Poses.put(poseEntry.getKey(), new ManipulatorPose(mode, jointsAngles));
        }
    }

    public void ConvertToLocal() {
        if (Mode == ManipulatorPoseMode.Local)
            return;
    }
    public void ConvertToWorld() {
        if (Mode == ManipulatorPoseMode.World)
            return;
    }

    public enum ManipulatorPoseMode {
        Local,
        World
    }
}
