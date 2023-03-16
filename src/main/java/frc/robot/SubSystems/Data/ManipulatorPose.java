package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Interface.FromManifest;
import frc.robot.Library.FRC_3117_Tools.Manifest.RobotManifest;

import java.util.HashMap;

@FromManifest(EntryName = "poses", OnLoadMethod = "OnManifestLoad")
public class ManipulatorPose {
    public ManipulatorPose(ManipulatorPoseAngle... angles) {
        Angles = angles;
    }

    public static HashMap<String, ManipulatorPose> Poses = new HashMap<>();

    public ManipulatorPoseAngle[] Angles;

    public static void OnManifestLoad(String entryName) {
        if (!RobotManifest.ManifestJson.HasEntry(entryName))
            return;

        var posesManifestEntry = RobotManifest.ManifestJson.GetSubObject(entryName);
        for (var poseEntry : posesManifestEntry.GetSubObjects().entrySet())
        {
            var angles = ManipulatorPoseAngle.ParseArray(poseEntry.getValue().GetStringArray("angles"));
            Poses.put(poseEntry.getKey(), new ManipulatorPose(angles));
        }
    }
}
