package com.asseco.android.proj;


/**
 * A meridian used to take longitude measurements from.
 */
public class PrimeMeridian extends Info implements IPrimeMeridian {
    private double _Longitude;
    private IAngularUnit _AngularUnit;

    /**
     * Initializes a new instance of a prime meridian
     *
     * @param longitude     Longitude of prime meridian
     * @param angularUnit   Angular unit
     * @param name          Name
     * @param authority     Authority name
     * @param authorityCode Authority-specific identification code.
     * @param alias         Alias
     * @param abbreviation  Abbreviation
     * @param remarks       Provider-supplied remarks
     * @throws Exception the exception
     */
    public PrimeMeridian(double longitude, IAngularUnit angularUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) throws Exception {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _Longitude = longitude;
        _AngularUnit = angularUnit;
    }

    /**
     * Greenwich prime meridian
     *
     * @return the greenwich
     * @throws Exception the exception
     */
    public static PrimeMeridian getGreenwich() throws Exception {
        return new PrimeMeridian(0.0, AngularUnit.getDegrees(), "Greenwich", "EPSG", 8901, "", "", "");
    }

    /**
     * Lisbon prime meridian
     *
     * @return the lisbon
     * @throws Exception the exception
     */
    public static PrimeMeridian getLisbon() throws Exception {
        return new PrimeMeridian(-9.0754862, AngularUnit.getDegrees(), "Lisbon", "EPSG", 8902, "", "", "");
    }

    /**
     * Paris prime meridian.
     * Value adopted by IGN (Paris) in 1936. Equivalent to 2 deg 20min 14.025sec. Preferred by EPSG to earlier value of 2deg 20min 13.95sec (2.596898 grads) used by RGS London.
     *
     * @return the paris
     * @throws Exception the exception
     */
    public static PrimeMeridian getParis() throws Exception {
        return new PrimeMeridian(2.5969213, AngularUnit.getDegrees(), "Paris", "EPSG", 8903, "", "", "Value adopted by IGN (Paris) in 1936. Equivalent to 2 deg 20min 14.025sec. Preferred by EPSG to earlier value of 2deg 20min 13.95sec (2.596898 grads) used by RGS London.");
    }

    /**
     * Bogota prime meridian
     *
     * @return the bogota
     * @throws Exception the exception
     */
    public static PrimeMeridian getBogota() throws Exception {
        return new PrimeMeridian(-74.04513, AngularUnit.getDegrees(), "Bogota", "EPSG", 8904, "", "", "");
    }

    /**
     * Madrid prime meridian
     *
     * @return the madrid
     * @throws Exception the exception
     */
    public static PrimeMeridian getMadrid() throws Exception {
        return new PrimeMeridian(-3.411658, AngularUnit.getDegrees(), "Madrid", "EPSG", 8905, "", "", "");
    }

    /**
     * Rome prime meridian
     *
     * @return the rome
     * @throws Exception the exception
     */
    public static PrimeMeridian getRome() throws Exception {
        return new PrimeMeridian(12.27084, AngularUnit.getDegrees(), "Rome", "EPSG", 8906, "", "", "");
    }

    /**
     * Bern prime meridian.
     * 1895 value. Newer value of 7 deg 26 min 22.335 sec E determined in 1938.
     *
     * @return the bern
     * @throws Exception the exception
     */
    public static PrimeMeridian getBern() throws Exception {
        return new PrimeMeridian(7.26225, AngularUnit.getDegrees(), "Bern", "EPSG", 8907, "", "", "1895 value. Newer value of 7 deg 26 min 22.335 sec E determined in 1938.");
    }

    /**
     * Jakarta prime meridian
     *
     * @return the jakarta
     * @throws Exception the exception
     */
    public static PrimeMeridian getJakarta() throws Exception {
        return new PrimeMeridian(106.482779, AngularUnit.getDegrees(), "Jakarta", "EPSG", 8908, "", "", "");
    }

    /**
     * Ferro prime meridian.
     * Used in Austria and former Czechoslovakia.
     *
     * @return the ferro
     * @throws Exception the exception
     */
    public static PrimeMeridian getFerro() throws Exception {
        return new PrimeMeridian(-17.66666666666667, AngularUnit.getDegrees(), "Ferro", "EPSG", 8909, "", "", "Used in Austria and former Czechoslovakia.");
    }

    /**
     * Brussels prime meridian
     *
     * @return the brussels
     * @throws Exception the exception
     */
    public static PrimeMeridian getBrussels() throws Exception {
        return new PrimeMeridian(4.220471, AngularUnit.getDegrees(), "Brussels", "EPSG", 8910, "", "", "");
    }

    /**
     * Stockholm prime meridian
     *
     * @return the stockholm
     * @throws Exception the exception
     */
    public static PrimeMeridian getStockholm() throws Exception {
        return new PrimeMeridian(18.03298, AngularUnit.getDegrees(), "Stockholm", "EPSG", 8911, "", "", "");
    }

    /**
     * Athens prime meridian.
     * Used in Greece for older mapping based on Hatt projection.
     *
     * @return the athens
     * @throws Exception the exception
     */
    public static PrimeMeridian getAthens() throws Exception {
        return new PrimeMeridian(23.4258815, AngularUnit.getDegrees(), "Athens", "EPSG", 8912, "", "", "Used in Greece for older mapping based on Hatt projection.");
    }

    /**
     * Oslo prime meridian.
     * Formerly known as Kristiania or Christiania.
     *
     * @return the oslo
     * @throws Exception the exception
     */
    public static PrimeMeridian getOslo() throws Exception {
        return new PrimeMeridian(10.43225, AngularUnit.getDegrees(), "Oslo", "EPSG", 8913, "", "", "Formerly known as Kristiania or Christiania.");
    }

    /**
     * Gets or sets the longitude of the prime meridian (relative to the Greenwich prime meridian).
     */
    public double getLongitude() throws Exception {
        return _Longitude;
    }

    public void setLongitude(double value) throws Exception {
        _Longitude = value;
    }

    /**
     * Gets or sets the AngularUnits.
     */
    public IAngularUnit getangularUnit() throws Exception {
        return _AngularUnit;
    }

    public void setangularUnit(IAngularUnit value) throws Exception {
        _AngularUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PRIMEM[\"%s\", %f", getName(), getLongitude()));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        return String.format("<CS_PrimeMeridian Longitude=\"%f\" >%s%s</CS_PrimeMeridian>", getLongitude(), getInfoXml(), getangularUnit().getXML());
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    public boolean equalParams(Object obj) throws Exception {
        if (!(obj instanceof PrimeMeridian))
            return false;

        PrimeMeridian prime = obj instanceof PrimeMeridian ? (PrimeMeridian) obj : (PrimeMeridian) null;
        if (prime != null) {
            return prime.getangularUnit().equalParams(this.getangularUnit()) && prime.getLongitude() == this.getLongitude();
        }
        return false;
    }

}


