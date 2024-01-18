package com.asseco.android.proj.projections;

import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * Implemetns the Lambert Conformal Conic 2SP Projection.
 * The Lambert Conformal Conic projection is a standard projection for presenting maps
 * of land areas whose East-West extent is large compared with their North-South extent.
 * This projection is "conformal" in the sense that lines of latitude and longitude,
 * which are perpendicular to one another on the earth's surface, are also perpendicular
 * to one another in the projected domain.
 */
public class LambertConformalConic2SP extends MapProjection {
    //double _falseEasting;
    //double _falseNorthing;
    //private double es=0;              /* eccentricity squared         */
    //private double e=0;               /* eccentricity                 */
    //private double center_lon=0;      /* center longituted            */
    //private double center_lat=0;      /* cetner latitude              */
    private double ns = 0;
    /* ratio of angle between meridian*/
    private double f0 = 0;
    /* flattening of ellipsoid      */
    private double rh = 0;
    /* height above ellipsoid       */

    /**
     * Creates an instance of an LambertConformalConic2SPProjection projection object.
     * The parameters this projection expects are listed below.ItemsDescriptionslatitude_of_false_originThe latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.longitude_of_false_originThe longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.latitude_of_1st_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.latitude_of_2nd_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.easting_at_false_originThe easting value assigned to the false origin.northing_at_false_originThe northing value assigned to the false origin.
     *
     * @param parameters List of parameters to initialize the projection.
     * @throws Exception the exception
     */
    public LambertConformalConic2SP(List<ProjectionParameter> parameters) throws Exception {
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
    protected LambertConformalConic2SP(List<ProjectionParameter> parameters, LambertConformalConic2SP inverse) throws Exception {
        super(parameters, inverse);
        setName("Lambert_Conformal_Conic_2SP");
        setAuthority("EPSG");
        setAuthorityCode(9802);
        //Check for missing parameters
        double lat1 = degrees2Radians(_Parameters.getParameterValue("standard_parallel_1"));
        double lat2 = degrees2Radians(_Parameters.getParameterValue("standard_parallel_2"));
        double sin_po;
        /* sin value                            */
        double cos_po;
        /* cos value                            */
        double con;
        /* temporary variable                   */
        double ms1;
        /* small m 1                            */
        double ms2;
        /* small m 2                            */
        double ts0;
        /* small t 0                            */
        double ts1;
        /* small t 1                            */
        double ts2;
        /* small t 2                            */
        /* Standard Parallels cannot be equal and on opposite sides of the equator
                    ------------------------------------------------------------------------*/
        if (Math.abs(lat1 + lat2) < EPSLN) {
            throw new IllegalArgumentException("Equal latitudes for St. Parallels on opposite sides of equator.");
        }

        //Debug.Assert(true,"LambertConformalConic:LambertConformalConic() - Equal Latitiudes for St. Parallels on opposite sides of equator");

        sin_po = Math.sin(lat1);
        cos_po = Math.cos(lat1);
        con = sin_po;
        ms1 = msfnz(_e, sin_po, cos_po);
        ts1 = tsfnz(_e, lat1, sin_po);
        sin_po = Math.sin(lat2);
        cos_po = Math.cos(lat2);
        ms2 = msfnz(_e, sin_po, cos_po);
        ts2 = tsfnz(_e, lat2, sin_po);
        sin_po = Math.sin(lat_origin);
        ts0 = tsfnz(_e, lat_origin, sin_po);
        if (Math.abs(lat1 - lat2) > EPSLN)
            ns = Math.log(ms1 / ms2) / Math.log(ts1 / ts2);
        else
            ns = con;
        f0 = ms1 / (ns * Math.pow(ts1, ns));
        rh = _semiMajor * f0 * Math.pow(ts0, ns);
    }

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double dLongitude = lonlat[0];
        double dLatitude = lonlat[1];
        double con;
        /* temporary angle variable             */
        double rh1;
        /* height above ellipsoid               */
        double sinphi;
        /* sin value                            */
        double theta;
        /* angle                                */
        double ts;
        /* small value t                        */
        con = Math.abs(Math.abs(dLatitude) - HALF_PI);
        if (con > EPSLN) {
            sinphi = Math.sin(dLatitude);
            ts = tsfnz(_e, dLatitude, sinphi);
            rh1 = _semiMajor * f0 * Math.pow(ts, ns);
        } else {
            con = dLatitude * ns;
            if (con <= 0)
                throw new IllegalArgumentException();

            rh1 = 0;
        }
        theta = ns * adjust_lon(dLongitude - central_meridian);
        dLongitude = rh1 * Math.sin(theta);
        dLatitude = rh - rh1 * Math.cos(theta);

        return lonlat.length == 2
                ? new double[]{dLongitude, dLatitude}
                : new double[]{dLongitude, dLatitude, lonlat[2]};
    }

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    protected double[] metersToRadians(double[] p) throws Exception {
        double dLongitude = Double.NaN;
        double dLatitude = Double.NaN;
        double rh1;
        /* height above ellipsoid	*/
        double con;
        /* sign variable		*/
        double ts;
        /* small t			*/
        double theta;
        /* angle			*/
        double dX = p[0];
        double dY = rh - p[1];
        if (ns > 0) {
            rh1 = Math.sqrt(dX * dX + dY * dY);
            con = 1.0;
        } else {
            rh1 = -Math.sqrt(dX * dX + dY * dY);
            con = -1.0;
        }
        theta = 0.0;
        if (rh1 != 0)
            theta = Math.atan2((con * dX), (con * dY));

        if ((rh1 != 0) || (ns > 0.0)) {
            con = 1.0 / ns;
            ts = Math.pow((rh1 / (_semiMajor * f0)), con);
            dLatitude = phi2z(_e, ts);
        } else
            dLatitude = -HALF_PI;
        dLongitude = adjust_lon(theta / ns + central_meridian);

        return p.length == 2
                ? new double[]{dLongitude, dLatitude}
                : new double[]{dLongitude, dLatitude, p[2]};
    }

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            _inverse = new LambertConformalConic2SP(_Parameters.toProjectionParameter(), this);
        }

        return _inverse;
    }

}


