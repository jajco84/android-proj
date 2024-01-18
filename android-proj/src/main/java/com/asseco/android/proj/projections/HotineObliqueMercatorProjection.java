package com.asseco.android.proj.projections;


import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * The type Hotine oblique mercator projection.
 */
public class HotineObliqueMercatorProjection extends MapProjection {
    private double _azimuth;
    private double _sinP20, _cosP20;
    private double _bl, _al;
    private double _d, _el;
    private double _singrid, _cosgrid;
    private double _singam, _cosgam;
    private double _sinaz, _cosaz;
    private double _u;


    /**
     * Instantiates a new Hotine oblique mercator projection.
     *
     * @param parameters the parameters
     * @throws Exception the exception
     */
    public HotineObliqueMercatorProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Instantiates a new Hotine oblique mercator projection.
     *
     * @param parameters the parameters
     * @param inverse    the inverse
     * @throws Exception the exception
     */
    public HotineObliqueMercatorProjection(List<ProjectionParameter> parameters, HotineObliqueMercatorProjection inverse) throws Exception {
        super(parameters, inverse);
        setAuthority("EPSG");
        setAuthorityCode(9812);
        setName("Hotine_Oblique_Mercator");
        _azimuth = degrees2Radians(_Parameters.getParameterValue("azimuth"));
        double rectifiedGridAngle = degrees2Radians(_Parameters.getParameterValue("rectified_grid_angle"));

        _sinP20 = Math.sin(lat_origin);
        _cosP20 = Math.cos(lat_origin);


        double con = 1.0 - _es * Math.pow(_sinP20, 2);
        double com = Math.sqrt(1.0 - _es);
        _bl = Math.sqrt(1.0 + _es * Math.pow(_cosP20, 4.0) / (1.0 - _es));
        _al = _semiMajor * _bl * scale_factor * com / con;
        double f;
        if (Math.abs(lat_origin) < EPSLN) {
            //ts = 1.0;
            _d = 1.0;
            _el = 1.0;
            f = 1.0;
        } else {
            double ts = tsfnz(_e, lat_origin, _sinP20);
            con = Math.sqrt(con);
            _d = _bl * com / (_cosP20 * con);
            if ((_d * _d - 1.0) > 0.0) {
                if (lat_origin >= 0.0)
                    f = _d + Math.sqrt(_d * _d - 1.0);
                else
                    f = _d - Math.sqrt(_d * _d - 1.0);
            } else
                f = _d;
            _el = f * Math.pow(ts, _bl);
        }
        double g = .5 * (f - 1.0 / f);
        double gama = asinz(Math.sin(_azimuth) / _d);
        setlon_origin(getlon_origin() - asinz(g * Math.tan(gama)) / _bl);
        con = Math.abs(lat_origin);
        if ((con > EPSLN) && (Math.abs(con - HALF_PI) > EPSLN)) {
            _singam = Math.sin(gama);
            _cosgam = Math.cos(gama);

            _sinaz = Math.sin(_azimuth);
            _cosaz = Math.cos(_azimuth);
            if (lat_origin >= 0)
                _u = (_al / _bl) * Math.atan(Math.sqrt(_d * _d - 1.0) / _cosaz);
            else
                _u = -(_al / _bl) * Math.atan(Math.sqrt(_d * _d - 1.0) / _cosaz);
        } else {
            throw new IllegalArgumentException("Input data error");
        }

        _singrid = Math.sin(rectifiedGridAngle);
        _cosgrid = Math.cos(rectifiedGridAngle);
    }

    private boolean getNaturalOriginOffsets() throws Exception {
        if (getAuthorityCode() == 9812)
            return false;

        if (getAuthorityCode() == 9815)
            return true;

        throw new IllegalArgumentException("AuthorityCode");
    }

    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            _inverse = new HotineObliqueMercatorProjection(_Parameters.toProjectionParameter(), this);
        }

        return _inverse;
    }

    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double lon = lonlat[0];
        double lat = lonlat[1];
        double us, ul;
        // Forward equations
        // -----------------
        double sin_phi = Math.sin(lat);
        double dlon = adjust_lon(lon - getlon_origin());
        double vl = Math.sin(_bl * dlon);
        if (Math.abs(Math.abs(lat) - HALF_PI) > EPSLN) {
            double ts1 = tsfnz(_e, lat, sin_phi);
            double q = _el / (Math.pow(ts1, _bl));
            double s = .5 * (q - 1.0 / q);
            double t = .5 * (q + 1.0 / q);
            ul = (s * _singam - vl * _cosgam) / t;
            double con = Math.cos(_bl * dlon);
            if (Math.abs(con) < .0000001) {
                us = _al * _bl * dlon;
            } else {
                us = _al * Math.atan((s * _cosgam + vl * _singam) / con) / _bl;
                if (con < 0)
                    us = us + PI * _al / _bl;

            }
        } else {
            if (lat >= 0)
                ul = _singam;
            else
                ul = -_singam;
            us = _al * lat / _bl;
        }
        if (Math.abs(Math.abs(ul) - 1.0) <= EPSLN) {
            throw new Exception("Point projects into infinity");
        }

        double vs = .5 * _al * Math.log((1.0 - ul) / (1.0 + ul)) / _bl;
        if (!getNaturalOriginOffsets())
            us = us - _u;

        double x = vs * _cosgrid + us * _singrid;
        double y = us * _cosgrid - vs * _singrid;

        return lonlat.length == 2
                ? new double[]{x, y} :
                new double[]{x, y, lonlat[2]};
    }

    protected double[] metersToRadians(double[] p) throws Exception {
        // Inverse equations
        // -----------------
        double x = p[0];
        double y = p[1];
        double vs = x * _cosgrid - y * _singrid;
        double us = y * _cosgrid + x * _singrid;
        if (!getNaturalOriginOffsets())
            us = us + _u;

        double q = Math.exp(-_bl * vs / _al);
        double s = .5 * (q - 1.0 / q);
        double t = .5 * (q + 1.0 / q);
        double vl = Math.sin(_bl * us / _al);
        double ul = (vl * _cosgam + s * _singam) / t;
        double lon, lat;
        if (Math.abs(Math.abs(ul) - 1.0) <= EPSLN) {
            lon = getlon_origin();
            lat = sign(ul) * HALF_PI;
        } else {
            double con = 1.0 / _bl;
            double ts1 = Math.pow((_el / Math.sqrt((1.0 + ul) / (1.0 - ul))), con);
            lat = phi2z(_e, ts1);
            con = Math.cos(_bl * us / _al);
            double theta = getlon_origin() - Math.atan2((s * _cosgam - vl * _singam), con) / _bl;
            lon = adjust_lon(theta);
        }
        return p.length == 2
                ? new double[]{lon, lat} : new double[]{lon, lat, p[2]};
    }

}


