package com.asseco.android.proj.projections;


import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * Implemetns the Oblique Stereographic Projection.
 */
public class ObliqueStereographicProjection extends MapProjection {
    private static double ITERATION_TOLERANCE = 1E-14;
    private static int MAXIMUM_ITERATIONS = 15;
    private static double EPSILON = 1E-6;
    private double globalScale;
    private double C, K, ratexp;
    private double phic0, cosc0, sinc0, R2;

    /**
     * Initializes the ObliqueStereographicProjection object with the specified parameters.
     *
     * @param parameters List of parameters to initialize the projection.The parameters this projection expects are listed below.ItemsDescriptionscentral_meridianThe longitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the longitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).latitude_of_originThe latitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the latitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).scale_factorThe factor by which the map grid is reduced or enlarged during the projection process, defined by its value at the natural origin.false_eastingsince the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Easting, FE, is the easting value assigned to the abscissa (east).false_northingsince the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Northing, FN, is the northing value assigned to the ordinate.
     * @throws Exception the exception
     */
    public ObliqueStereographicProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Initializes the ObliqueStereographicProjection object with the specified parameters.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param inverse    Inverse projectionThe parameters this projection expects are listed below.ItemsDescriptionscentral_meridianThe longitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the longitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).latitude_of_originThe latitude of the point from which the values of both the geographical coordinates on the ellipsoid and the grid coordinates on the projection are deemed to increment or decrement for computational purposes. Alternatively it may be considered as the latitude of the point which in the absence of application of false coordinates has grid coordinates of (0,0).scale_factorThe factor by which the map grid is reduced or enlarged during the projection process, defined by its value at the natural origin.false_eastingsince the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Easting, FE, is the easting value assigned to the abscissa (east).false_northingsince the natural origin may be at or near the centre of the projection and under normal coordinate circumstances would thus give rise to negative coordinates over parts of the mapped area, this origin is usually given false coordinates which are large enough to avoid this inconvenience. The False Northing, FN, is the northing value assigned to the ordinate.
     * @throws Exception the exception
     */
    public ObliqueStereographicProjection(List<ProjectionParameter> parameters, ObliqueStereographicProjection inverse) throws Exception {
        super(parameters, inverse);
        globalScale = scale_factor * this._semiMajor;
        double sphi = Math.sin(lat_origin);
        double cphi = Math.cos(lat_origin);
        cphi *= cphi;
        R2 = 2.0 * Math.sqrt(1 - _es) / (1 - _es * sphi * sphi);
        C = Math.sqrt(1.0 + _es * cphi * cphi / (1.0 - _es));
        phic0 = Math.asin(sphi / C);
        sinc0 = Math.sin(phic0);
        cosc0 = Math.cos(phic0);
        ratexp = 0.5 * C * _e;
        K = Math.tan(0.5 * phic0 + Math.PI / 4) / (Math.pow(Math.tan(0.5 * lat_origin + Math.PI / 4), C) * srat(_e * sphi, ratexp));
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    protected double[] metersToRadians(double[] p) throws Exception {
        double x = p[0] / this.globalScale;
        double y = p[1] / this.globalScale;
        double rho = Math.sqrt((x * x) + (y * y));
        if (Math.abs(rho) < EPSILON) {
            x = 0.0;
            y = phic0;
        } else {
            double ce = 2.0 * Math.atan2(rho, R2);
            double sinc = Math.sin(ce);
            double cosc = Math.cos(ce);
            x = Math.atan2(x * sinc, rho * cosc0 * cosc - y * sinc0 * sinc);
            y = (cosc * sinc0) + (y * sinc * cosc0 / rho);
            if (Math.abs(y) >= 1.0) {
                y = (y < 0.0) ? -Math.PI / 2.0 : Math.PI / 2.0;
            } else {
                y = Math.asin(y);
            }
        }
        x /= C;
        double num = Math.pow(Math.tan(0.5 * y + Math.PI / 4.0) / K, 1.0 / C);
        for (int i = MAXIMUM_ITERATIONS; ; ) {
            double phi = 2.0 * Math.atan(num * srat(_e * Math.sin(y), -0.5 * _e)) - Math.PI / 2.0;
            if (Math.abs(phi - y) < ITERATION_TOLERANCE) {
                break;
            }

            y = phi;
            if (--i < 0) {
                throw new Exception("Oblique Stereographics doesn't converge");
            }

        }
        x += central_meridian;
        if (p.length == 2)
            return new double[]{x, y};
        else
            return new double[]{x, y, p[2]};
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double x = lonlat[0] - this.central_meridian;
        double y = lonlat[1];
        y = 2.0 * Math.atan(K * Math.pow(Math.tan(0.5 * y + Math.PI / 4), C) * srat(_e * Math.sin(y), ratexp)) - Math.PI / 2;
        x *= C;
        double sinc = Math.sin(y);
        double cosc = Math.cos(y);
        double cosl = Math.cos(x);
        double k = R2 / (1.0 + sinc0 * sinc + cosc0 * cosc * cosl);
        x = k * cosc * Math.sin(x);
        y = k * (cosc0 * sinc - sinc0 * cosc * cosl);
        if (lonlat.length == 2)
            return new double[]{x * this.globalScale, y * this.globalScale};
        else
            return new double[]{x * this.globalScale, y * this.globalScale, lonlat[2]};
    }

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            _inverse = new ObliqueStereographicProjection(_Parameters.toProjectionParameter(), this);
        }

        return _inverse;
    }

    private double srat(double esinp, double exp) throws Exception {
        return Math.pow((1.0 - esinp) / (1.0 + esinp), exp);
    }

}


