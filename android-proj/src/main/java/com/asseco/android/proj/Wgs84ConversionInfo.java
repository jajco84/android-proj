package com.asseco.android.proj;


/**
 * Parameters for a geographic transformation into WGS84. The Bursa Wolf parameters should be applied
 * to geocentric coordinates, where the X axis points towards the Greenwich Prime Meridian, the Y axis
 * points East, and the Z axis points North.
 * These parameters can be used to approximate a transformation from the horizontal datum to the
 * WGS84 datum using a Bursa Wolf transformation. However, it must be remembered that this transformation
 * is only an approximation. For a given horizontal datum, different Bursa Wolf transformations can be
 * used to minimize the errors over different regions.If the DATUM clause contains a TOWGS84 clause, then this should be its “preferred” transformation,
 * which will often be the transformation which gives a broad approximation over the whole area of interest
 * (e.g. the area of interest in the containing geographic coordinate system).Sometimes, only the first three or six parameters are defined. In this case the remaining
 * parameters must be zero. If only three parameters are defined, then they can still be plugged into the
 * Bursa Wolf formulas, or you can take a short cut. The Bursa Wolf transformation works on geocentric
 * coordinates, so you cannot apply it onto geographic coordinates directly. If there are only three
 * parameters then you can use the Molodenski or abridged Molodenski formulas.If a datums ToWgs84Parameters parameter values are zero, then the receiving
 * application can assume that the writing application believed that the datum is approximately equal to
 * WGS84.
 */
public class Wgs84ConversionInfo {
    private static final double SEC_TO_RAD = 4.84813681109535993589914102357e-6;
    /**
     * Bursa Wolf shift in meters.
     */
    public double Dx;
    /**
     * Bursa Wolf shift in meters.
     */
    public double Dy;
    /**
     * Bursa Wolf shift in meters.
     */
    public double Dz;
    /**
     * Bursa Wolf rotation in arc seconds.
     */
    public double Ex;
    /**
     * Bursa Wolf rotation in arc seconds.
     */
    public double Ey;
    /**
     * Bursa Wolf rotation in arc seconds.
     */
    public double Ez;
    /**
     * Bursa Wolf scaling in parts per million.
     */
    public double Ppm;
    /**
     * Human readable text describing intended region of transformation.
     */
    public String AreaOfUse = new String();

    /**
     * Initializes an instance of Wgs84ConversionInfo with default parameters (all values = 0)
     *
     * @throws Exception the exception
     */
    public Wgs84ConversionInfo() throws Exception {
        this(0, 0, 0, 0, 0, 0, 0, "");
    }

    /**
     * Initializes an instance of Wgs84ConversionInfo
     *
     * @param dx  Bursa Wolf shift in meters.
     * @param dy  Bursa Wolf shift in meters.
     * @param dz  Bursa Wolf shift in meters.
     * @param ex  Bursa Wolf rotation in arc seconds.
     * @param ey  Bursa Wolf rotation in arc seconds.
     * @param ez  Bursa Wolf rotation in arc seconds.
     * @param ppm Bursa Wolf scaling in parts per million.
     * @throws Exception the exception
     */
    public Wgs84ConversionInfo(double dx, double dy, double dz, double ex, double ey, double ez, double ppm) throws Exception {
        this(dx, dy, dz, ex, ey, ez, ppm, "");
    }

    /**
     * Initializes an instance of Wgs84ConversionInfo
     *
     * @param dx        Bursa Wolf shift in meters.
     * @param dy        Bursa Wolf shift in meters.
     * @param dz        Bursa Wolf shift in meters.
     * @param ex        Bursa Wolf rotation in arc seconds.
     * @param ey        Bursa Wolf rotation in arc seconds.
     * @param ez        Bursa Wolf rotation in arc seconds.
     * @param ppm       Bursa Wolf scaling in parts per million.
     * @param areaOfUse Area of use for this transformation
     * @throws Exception the exception
     */
    public Wgs84ConversionInfo(double dx, double dy, double dz, double ex, double ey, double ez, double ppm, String areaOfUse) throws Exception {
        Dx = dx;
        Dy = dy;
        Dz = dz;
        Ex = ex;
        Ey = ey;
        Ez = ez;
        Ppm = ppm;
        AreaOfUse = areaOfUse;
    }

    /**
     * Affine Bursa-Wolf matrix transformation
     * Transformation of coordinates from one geographic coordinate system into another
     * (also colloquially known as a "datum transformation") is usually carried out as an
     * implicit concatenation of three transformations:[geographical to geocentric - geocentric to geocentric - geocentric to geographic
     * The middle part of the concatenated transformation, from geocentric to geocentric, is usually
     * described as a simplified 7-parameter Helmert transformation, expressed in matrix form with 7
     * parameters, in what is known as the "Bursa-Wolf" formula:
     * {@code
     * S = 1 + Ppm/1000000
     * [ Xt ]    [     S   -Ez*S   +Ey*S   Dx ]  [ Xs ]
     * [ Yt ]  = [ +Ez*S       S   -Ex*S   Dy ]  [ Ys ]
     * [ Zt ]    [ -Ey*S   +Ex*S       S   Dz ]  [ Zs ]
     * [ 1  ]    [     0       0       0    1 ]  [ 1  ]
     * }*
     *
     * The parameters are commonly referred to defining the transformation "from source coordinate system
     * to target coordinate system", whereby (XS, YS, ZS) are the coordinates of the point in the source
     * geocentric coordinate system and (XT, YT, ZT) are the coordinates of the point in the target
     * geocentric coordinate system. But that does not define the parameters uniquely; neither is the
     * definition of the parameters implied in the formula, as is often believed. However, the
     * following definition, which is consistent with the "Position Vector Transformation" convention,
     * is common EP survey practice:
     * (dX, dY, dZ): Translation vector, to be added to the point's position vector in the source
     * coordinate system in order to transform from source system to target system; also: the coordinates
     * of the origin of source coordinate system in the target coordinate system (RX, RY, RZ): Rotations to be applied to the point's vector. The sign convention is such that
     * a positive rotation about an axis is defined as a clockwise rotation of the position vector when
     * viewed from the origin of the Cartesian coordinate system in the positive direction of that axis;
     * e.g. a positive rotation about the Z-axis only from source system to target system will result in a
     * larger longitude value for the point in the target system. Although rotation angles may be quoted in
     * any angular unit of measure, the formula as given here requires the angles to be provided in radians.: The scale correction to be made to the position vector in the source coordinate system in order
     * to obtain the correct scale in the target coordinate system. M = (1 + dS*10-6), whereby dS is the scale
     * correction expressed in parts per million.
     *
     * for an explanation of the Bursa-Wolf transformation
     *
     * @return double [ ]
     * @throws Exception the exception
     */
    public double[] getAffineTransform() throws Exception {
        double RS = 1 + Ppm * 0.000001;
        return new double[]{RS, Ex * SEC_TO_RAD * RS, Ey * SEC_TO_RAD * RS, Ez * SEC_TO_RAD * RS, Dx, Dy, Dz};
    }

    /*return new double[3,4] {
                    { RS,				-Ez*SEC_TO_RAD*RS,	+Ey*SEC_TO_RAD*RS,	Dx} ,
                    { Ez*SEC_TO_RAD*RS,	RS,					-Ex*SEC_TO_RAD*RS,	Dy} ,
                    { -Ey*SEC_TO_RAD*RS,Ex*SEC_TO_RAD*RS,	RS,					Dz}
                };*/

    /**
     * Returns the Well Known Text (WKT) for this object.
     * The WKT format of this object is:
     * {@code TOWGS84[dx, dy, dz, ex, ey, ez, ppm]}
     *
     * @return WKT representaion
     */
    public String getWKT() {
        return String.format("TOWGS84[%f, %f, %f, %f, %f, %f, %f]", Dx, Dy, Dz, Ex, Ey, Ez, Ppm);
    }

    /**
     * Gets an XML representation of this object
     *
     * @return the xml
     */
    public String getXML() {
        return String.format("<CS_WGS84ConversionInfo Dx=\"%f\" Dy=\"%f\" Dz=\"%f\" Ex=\"%f\" Ey=\"%f\" Ez=\"%f\" Ppm=\"%f\" />", Dx, Dy, Dz, Ex, Ey, Ez, Ppm);
    }

    /**
     * Returns the Well Known Text (WKT) for this object.
     * The WKT format of this object is:
     * {@code TOWGS84[dx, dy, dz, ex, ey, ez, ppm]}
     *
     * @return WKT representaion
     */
    public String toString() {
        return getWKT();
    }

    /**
     * Returns true of all 7 parameter values are 0.0
     *
     * @return has zero values only
     */
    public boolean getHasZeroValuesOnly() {
        return !(Dx != 0 || Dy != 0 || Dz != 0 || Ex != 0 || Ey != 0 || Ez != 0 || Ppm != 0);
    }


    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj the obj
     * @return True if equal
     */
    public boolean equals(Wgs84ConversionInfo obj) {
        if (obj == null)
            return false;

        return obj.Dx == this.Dx && obj.Dy == this.Dy && obj.Dz == this.Dz && obj.Ex == this.Ex && obj.Ey == this.Ey && obj.Ez == this.Ez && obj.Ppm == this.Ppm;
    }

}


