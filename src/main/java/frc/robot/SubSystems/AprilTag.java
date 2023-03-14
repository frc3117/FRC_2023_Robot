package frc.robot.SubSystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;

import java.util.HashMap;

public class AprilTag implements Sendable
{
    public AprilTag(int id) {
        Id = id;
        SmartDashboard.putData("Apriltags/" + id, this);
    }

    public final static int[] DefaultTags = { 1, 2, 3, 4, 5, 6, 7, 8 };
    public static HashMap<Integer, AprilTag> AprilTags = new HashMap<>();

    public final double Id;
    public boolean Visible = false;
    public Vector3d EstimatedPosition = new Vector3d();
    public double EstimatedRotation = 0;

    public static void GenerateTags() {
        GenerateTags(DefaultTags);
    }
    public static void GenerateTags(int[] ids) {
        AprilTags.clear();

        for (var id : ids)
            AprilTags.put(id, new AprilTag(id));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("Visible", null, (val) -> Visible = val);

        builder.addDoubleProperty("Position/X", null, (val) -> EstimatedPosition.X = val);
        builder.addDoubleProperty("Position/Y", null, (val) -> EstimatedPosition.Y = val);
        builder.addDoubleProperty("Position/Z", null, (val) -> EstimatedPosition.Z = val);

        builder.addDoubleProperty("Rotation", null, (val) -> EstimatedRotation = val);
    }
}
