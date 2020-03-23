package zelix.cc.client.utils.Math;

public class FlatLocation {
    public double x,z;
    public FlatLocation (double x, double z)
    {
        this.x = x;
        this.z = z;
    }

    public void setNextPos(double forwardDist, float yaw, double forward, double strafe)
    {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        setX(this.x + (forward * forwardDist * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * forwardDist * Math.sin(Math.toRadians((double)(yaw + 90.0f)))));
        setZ(this.z + (forward * forwardDist * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * forwardDist * Math.cos(Math.toRadians((double)(yaw + 90.0f)))));
    }
    public FlatLocation getNextPos(double forwardDist, float yaw, double forward, double strafe)
    {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        setX(this.x + (forward * forwardDist * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * forwardDist * Math.sin(Math.toRadians((double)(yaw + 90.0f)))));
        setZ(this.z + (forward * forwardDist * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * forwardDist * Math.cos(Math.toRadians((double)(yaw + 90.0f)))));
        return this;
    }
    public void setX(double x)
    {
        this.x = x;
    }
    public void setZ(double z)
    {
        this.z = z;
    }
}
