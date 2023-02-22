package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Component.Data.MotorControllerGroup;
import frc.robot.Library.FRC_3117_Tools.Math.Vector3d;
import frc.robot.Library.FRC_3117_Tools.Wrapper.Encoder.Interface.AbsoluteEncoder;

import java.util.ArrayList;
import java.util.List;

public class ManipulatorData
{
    public void AddSegment(ManipulatorSegmentData segment)
    {
        Segments.add(segment);
    }

    public List<ManipulatorSegmentData> Segments = new ArrayList<>();
}
