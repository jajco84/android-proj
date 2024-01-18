package com.asseco.android.proj.projections;

import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * Implements the Albers projection.
 * Implements the Albers projection. The Albers projection is most commonly
 * used to project the United States of America. It gives the northern
 * border with Canada a curved appearance.The Albers Equal Area
 * projection has the property that the area bounded
 * by any pair of parallels and meridians is exactly reproduced between the
 * image of those parallels and meridians in the projected domain, that is,
 * the projection preserves the correct area of the earth though distorts
 * direction, distance and shape somewhat.
 */
public class AlbersProjection extends MapProjection {
    private double _c;
    private double _ro0;
    private double _n;

    /**
     * Creates an instance of an Albers projection object.
     *
     * @param parameters List of parameters to initialize the projection.The parameters this projection expects are listed below.ItemsDescriptionslatitude_of_false_originThe latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.longitude_of_false_originThe longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.latitude_of_1st_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.latitude_of_2nd_standard_parallelFor a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.easting_at_false_originThe easting value assigned to the false origin.northing_at_false_originThe northing value assigned to the false origin.
     * @throws Exception the exception
     */
    public AlbersProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Creates an instance of an Albers projection object.
     * The parameters this projection expects are listed below.ItemsDescriptionslatitude_of_centerThe latitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.longitude_of_centerThe longitude of the point which is not the natural origin and at which grid coordinate values false easting and false northing are defined.standard_parallel_1For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is nearest the pole.  Scale is true along this parallel.standard_parallel_2For a conic projection with two standard parallels, this is the latitude of intersection of the cone with the ellipsoid that is furthest from the pole.  Scale is true along this parallel.false_eastingThe easting value assigned to the false origin.false_northingThe northing value assigned to the false origin.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param inverse    Indicates whether the projection forward (meters to degrees or degrees to meters).
     * @throws Exception the exception
     */
    protected AlbersProjection(List<ProjectionParameter> parameters, AlbersProjection inverse) throws Exception {
        super(parameters, inverse);
        setName("Albers_Conic_Equal_Area");
        double lat0 = lat_origin;
        double lat1 = degrees2Radians(_Parameters.getParameterValue("standard_parallel_1"));
        double lat2 = degrees2Radians(_Parameters.getParameterValue("standard_parallel_2"));
        if (Math.abs(lat1 + lat2) < 0.000000001)
            throw new IllegalArgumentException("Equal latitudes for standard parallels on opposite sides of Equator.");

        double alpha1 = alpha(lat1);
        double alpha2 = alpha(lat2);
        double m1 = Math.cos(lat1) / Math.sqrt(1 - _es * Math.pow(Math.sin(lat1), 2));
        double m2 = Math.cos(lat2) / Math.sqrt(1 - _es * Math.pow(Math.sin(lat2), 2));
        _n = (Math.pow(m1, 2) - Math.pow(m2, 2)) / (alpha2 - alpha1);
        _c = Math.pow(m1, 2) + (_n * alpha1);
        _ro0 = ro(alpha(lat0));
    }

    /*
                double sin_p0 = Math.sin(lat0);
    			double cos_p0 = Math.cos(lat0);
    			double q0 = qsfnz(e, sin_p0, cos_p0);
    			double sin_p1 = Math.sin(lat1);
    			double cos_p1 = Math.cos(lat1);
    			double m1 = msfnz(e,sin_p1,cos_p1);
    			double q1 = qsfnz(e,sin_p1,cos_p1);
    			double sin_p2 = Math.sin(lat2);
    			double cos_p2 = Math.cos(lat2);
    			double m2 = msfnz(e,sin_p2,cos_p2);
    			double q2 = qsfnz(e,sin_p2,cos_p2);
    			if (Math.abs(lat1 - lat2) > EPSLN)
    				ns0 = (m1 * m1 - m2 * m2)/ (q2 - q1);
    			else
    				ns0 = sin_p1;
    			C = m1 * m1 + ns0 * q1;
    			rh = this._semiMajor * Math.sqrt(C - ns0 * q0)/ns0;
    			*/

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double dLongitude = lonlat[0];
        double dLatitude = lonlat[1];
        double a = alpha(dLatitude);
        double ro = ro(a);
        double theta = _n * (dLongitude - central_meridian);
        /*_falseEasting +*/
        dLongitude = ro * Math.sin(theta);
        /*_falseNorthing +*/
        dLatitude = _ro0 - (ro * Math.cos(theta));


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
        /* _metersPerUnit - _falseEasting*/
        double theta = Math.atan((p[0]) / (_ro0 - (p[1])));
        /* _metersPerUnit - _falseNorthing*/
        /* _metersPerUnit - _falseEasting*/
        double ro = Math.sqrt(Math.pow(p[0], 2) + Math.pow(_ro0 - (p[1]), 2));
        /* * _metersPerUnit - _falseNorthing*/
        double q = (_c - Math.pow(ro, 2) * Math.pow(_n, 2) / Math.pow(this._semiMajor, 2)) / _n;
        double b = Math.sin(q / (1 - ((1 - _es) / (2 * _e)) * Math.log((1 - _e) / (1 + _e))));
        double lat = Math.asin(q * 0.5);
        double preLat = Double.MAX_VALUE;
        int iterationCounter = 0;
        while (Math.abs(lat - preLat) > 0.000001) {
            preLat = lat;
            double sin = Math.sin(lat);
            double e2sin2 = _es * Math.pow(sin, 2);
            lat += (Math.pow(1 - e2sin2, 2) / (2 * Math.cos(lat))) * ((q / (1 - _es)) - sin / (1 - e2sin2) + 1 / (2 * _e) * Math.log((1 - _e * sin) / (1 + _e * sin)));
            iterationCounter++;
            if (iterationCounter > 25)
                throw new IllegalArgumentException("Transformation failed to converge in Albers backwards transformation");

        }
        double lon = central_meridian + (theta / _n);
        if (p.length == 2)
            return new double[]{lon, lat};

        return new double[]{lon, lat, p[2]};
    }

    /*Radians2Degrees(lon), Radians2Degrees(lat)*/
    /*Radians2Degrees(lon), Radians2Degrees(lat)*/

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new AlbersProjection(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

    //private double ToAuthalic(double lat)
    //{
    //    return Math.atan(Q(lat) / Q(Math.PI * 0.5));
    //}
    //private double Q(double angle)
    //{
    //    double sin = Math.sin(angle);
    //    double esin = e * sin;
    //    return Math.abs(sin / (1 - Math.pow(esin, 2)) - 0.5 * e) * Math.Log((1 - esin) / (1 + esin)));
    //}
    private double alpha(double lat) throws Exception {
        double sin = Math.sin(lat);
        double sinsq = Math.pow(sin, 2);
        return (1 - _es) * (((sin / (1 - _es * sinsq)) - 1 / (2 * _e) * Math.log((1 - _e * sin) / (1 + _e * sin))));
    }

    private double ro(double a) throws Exception {
        return _semiMajor * Math.sqrt((_c - _n * a)) / _n;
    }

}


