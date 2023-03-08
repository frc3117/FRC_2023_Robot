package frc.robot.SubSystems.Data;

import frc.robot.Library.FRC_3117_Tools.Component.CAN.DigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Component.CAN.MultiDigitalInputCAN;
import frc.robot.Library.FRC_3117_Tools.Physics.Kinematics.DHParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManipulatorData {
    public MultiDigitalInputCAN LimitSwitches;
    public List<ManipulatorSegmentData> Segments = new ArrayList<>();

    public void AddSegment(ManipulatorSegmentData segment) {
        Segments.add(segment);
    }

    public DHParameters[] GetDHParameters() {
        var params = new DHParameters[Segments.size()];
        for (var i = 0; i < params.length; i++)
            params[i] = Segments.get(i).DH;

        return params;
    }
}
