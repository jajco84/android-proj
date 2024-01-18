package com.asseco.android.proj.projections;

import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * Implemetns the Krovak Projection.
 * The normal case of the Lambert Conformal conic is for the axis of the cone
 * to be coincident with the minor axis of the ellipsoid, that is the axis of the cone
 * is normal to the ellipsoid at a pole. For the Oblique Conformal Conic the axis
 * of the cone is normal to the ellipsoid at a defined location and its extension
 * cuts the minor axis at a defined angle. This projection is used in the Czech Republic
 * and Slovakia under the name "Krovak" projection.
 */
public class KrovakProjection extends MapProjection {
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MaximumIterations = 15;
    /**
     * When to stop the iteration.
     */
    private static final double IterationTolerance = 1E-11;
    /**
     * Useful constant - 45° in radians.
     */
    private static final double S45 = 0.785398163397448;
    /**
     * Azimuth of the centre line passing through the centre of the projection.
     * This is equals to the co-latitude of the cone axis at point of intersection
     * with the ellipsoid.
     */
    private double _azimuth;
    /**
     * Latitude of pseudo standard parallel.
     */
    private double _pseudoStandardParallel;
    /**
     * Useful variables calculated from parameters defined by user.
     */
    private double _sinAzim, _cosAzim, _n, _tanS2, _alfa, _hae, _k1, _ka, _ro0, _rop;

    /**
     * Creates an instance of an LambertConformalConic2SPProjection projection object.
     * The parameters this projection expects are listed below.ItemsDescriptionslatitude_of_false_originThe latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.longitude_of_false_originThe longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.latitude_of_1st_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.latitude_of_2nd_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.easting_at_false_originThe easting value assigned to the false origin.northing_at_false_originThe northing value assigned to the false origin.
     *
     * @param parameters List of parameters to initialize the projection.
     * @throws Exception the exception
     */
    public KrovakProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Creates an instance of an Albers projection object.
     * The parameters this projection expects are listed below.ParameterDescriptionlatitude_of_originThe latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.central_meridianThe longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.standard_parallel_1For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.standard_parallel_2For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.false_eastingThe easting value assigned to the false origin.false_northingThe northing value assigned to the false origin.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param inverse    Indicates whether the projection forward (meters to degrees or degrees to meters).
     * @throws Exception the exception
     */
    protected KrovakProjection(List<ProjectionParameter> parameters, KrovakProjection inverse) throws Exception {
        super(parameters, inverse);
        setName("Krovak");
        setAuthority("EPSG");
        setAuthorityCode(9819);
        //PROJCS["S-JTSK (Ferro) / Krovak",
        //GEOGCS["S-JTSK (Ferro)",
        //    DATUM["D_S_JTSK_Ferro",
        //        SPHEROID["Bessel 1841",6377397.155,299.1528128]],
        //    PRIMEM["Ferro",-17.66666666666667],
        //    UNIT["degree",0.0174532925199433]],
        //PROJECTION["Krovak"],
        //PARAMETER["latitude_of_center",49.5],
        //PARAMETER["longitude_of_center",42.5],
        //PARAMETER["azimuth",30.28813972222222],
        //PARAMETER["pseudo_standard_parallel_1",78.5],
        //PARAMETER["scale_factor",0.9999],
        //PARAMETER["false_easting",0],
        //PARAMETER["false_northing",0],
        //UNIT["metre",1]]
        //Check for missing parameters
        _azimuth = degrees2Radians(_Parameters.getParameterValue("azimuth"));
        _pseudoStandardParallel = degrees2Radians(_Parameters.getParameterValue("pseudo_standard_parallel_1"));
        // Calculates useful constants.
        _sinAzim = Math.sin(_azimuth);
        _cosAzim = Math.cos(_azimuth);
        _n = Math.sin(_pseudoStandardParallel);
        _tanS2 = Math.tan(_pseudoStandardParallel / 2 + S45);
        double sinLat = Math.sin(lat_origin);
        double cosLat = Math.cos(lat_origin);
        double cosL2 = cosLat * cosLat;
        _alfa = Math.sqrt(1 + ((_es * (cosL2 * cosL2)) / (1 - _es)));
        // parameter B
        _hae = _alfa * _e / 2;
        double u0 = Math.asin(sinLat / _alfa);
        double esl = _e * sinLat;
        double g = Math.pow((1 - esl) / (1 + esl), (_alfa * _e) / 2);
        _k1 = Math.pow(Math.tan(lat_origin / 2 + S45), _alfa) * g / Math.tan(u0 / 2 + S45);
        _ka = Math.pow(1 / _k1, -1 / _alfa);
        double radius = Math.sqrt(1 - _es) / (1 - (_es * (sinLat * sinLat)));
        _ro0 = scale_factor * radius / Math.tan(_pseudoStandardParallel);
        _rop = _ro0 * Math.pow(_tanS2, _n);
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double lambda = lonlat[0] - central_meridian;
        double phi = lonlat[1];
        double esp = _e * Math.sin(phi);
        double gfi = Math.pow(((1.0 - esp) / (1.0 + esp)), _hae);
        double u = 2 * (Math.atan(Math.pow(Math.tan(phi / 2 + S45), _alfa) / _k1 * gfi) - S45);
        double deltav = -lambda * _alfa;
        double cosU = Math.cos(u);
        double s = Math.asin((_cosAzim * Math.sin(u)) + (_sinAzim * cosU * Math.cos(deltav)));
        double d = Math.asin(cosU * Math.sin(deltav) / Math.cos(s));
        double eps = _n * d;
        double ro = _rop / Math.pow(Math.tan(s / 2 + S45), _n);
        /* x and y are reverted  */
        double y = -(ro * Math.cos(eps)) * _semiMajor;
        double x = -(ro * Math.sin(eps)) * _semiMajor;
        return new double[]{x, y};
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    protected double[] metersToRadians(double[] p) throws Exception {
        double x = p[0] / _semiMajor;
        double y = p[1] / _semiMajor;
        // x -> southing, y -> westing
        double ro = Math.sqrt(x * x + y * y);
        double eps = Math.atan2(-x, -y);
        double d = eps / _n;
        double s = 2 * (Math.atan(Math.pow(_ro0 / ro, 1 / _n) * _tanS2) - S45);
        double cs = Math.cos(s);
        double u = Math.asin((_cosAzim * Math.sin(s)) - (_sinAzim * cs * Math.cos(d)));
        double kau = _ka * Math.pow(Math.tan((u / 2.0) + S45), 1 / _alfa);
        double deltav = Math.asin((cs * Math.sin(d)) / Math.cos(u));
        double lambda = -deltav / _alfa;
        double phi = 0d;
        for (int i = MaximumIterations; i > 0; i--) {
            // iteration calculation
            double fi1 = phi;
            double esf = _e * Math.sin(fi1);
            phi = 2.0 * (Math.atan(kau * Math.pow((1.0 + esf) / (1.0 - esf), _e / 2.0)) - S45);
            if (Math.abs(fi1 - phi) <= IterationTolerance) {
                break;
            }


        }
        return new double[]{lambda + central_meridian, phi};
    }

    //throw new ProjectionException(Errors.format(ErrorKeys.NO_CONVERGENCE));

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            _inverse = new KrovakProjection(_Parameters.toProjectionParameter(), this);
        }

        return _inverse;
    }

}


