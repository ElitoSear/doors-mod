package elito.doors.utils;

import net.minecraft.util.math.Vec3d;

public class Interpolation {

    public static Vec3d linear(Vec3d origin, Vec3d destination, double step) {
        return origin.add(destination.subtract(origin).multiply(step));
    }

    public static Vec3d cubic(Vec3d origin, Vec3d destination, Vec3d reference, double step) {
        return Interpolation.linear(
                Interpolation.linear(origin, reference, step),
                Interpolation.linear(reference, destination, step),
                step);
    }

}
