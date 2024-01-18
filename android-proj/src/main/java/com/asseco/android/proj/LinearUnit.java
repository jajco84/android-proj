package com.asseco.android.proj;

/**
 * Definition of linear units.
 */
public class LinearUnit extends Info implements ILinearUnit {
    private double _MetersPerUnit;

    /**
     * Creates an instance of a linear unit
     *
     * @param metersPerUnit Number of meters per                      {@link #LinearUnit}
     * @param name          Name
     * @param authority     Authority name
     * @param authorityCode Authority-specific identification code.
     * @param alias         Alias
     * @param abbreviation  Abbreviation
     * @param remarks       Provider-supplied remarks
     * @throws Exception the exception
     */
    public LinearUnit(double metersPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) throws Exception {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _MetersPerUnit = metersPerUnit;
    }

    /**
     * Returns the meters linear unit.
     * Also known as International metre. SI standard unit.
     *
     * @return the metre
     * @throws Exception the exception
     */
    public static ILinearUnit getMetre() throws Exception {
        return new LinearUnit(1.0, "metre", "EPSG", 9001, "m", "", "Also known as International metre. SI standard unit.");
    }

    /**
     * Returns the foot linear unit (1ft = 0.3048m).
     *
     * @return the foot
     * @throws Exception the exception
     */
    public static ILinearUnit getFoot() throws Exception {
        return new LinearUnit(0.3048, "foot", "EPSG", 9002, "ft", "", "");
    }

    /**
     * Returns the US Survey foot linear unit (1ftUS = 0.304800609601219m).
     *
     * @return the us survey foot
     * @throws Exception the exception
     */
    public static ILinearUnit getUSSurveyFoot() throws Exception {
        return new LinearUnit(0.304800609601219, "US survey foot", "EPSG", 9003, "American foot", "ftUS", "Used in USA.");
    }

    /**
     * Returns the Nautical Mile linear unit (1NM = 1852m).
     *
     * @return the nautical mile
     * @throws Exception the exception
     */
    public static ILinearUnit getNauticalMile() throws Exception {
        return new LinearUnit(1852, "nautical mile", "EPSG", 9030, "NM", "", "");
    }

    /**
     * Returns Clarke's foot.
     *
     * Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.
     * Used in older Australian, southern African and British West Indian mapping.
     *
     * @return the clarkes foot
     * @throws Exception the exception
     */
    public static ILinearUnit getClarkesFoot() throws Exception {
        return new LinearUnit(0.3047972654, "Clarke's foot", "EPSG", 9005, "Clarke's foot", "", "Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre. Used in older Australian, southern African & British West Indian mapping.");
    }

    /**
     * Gets or sets the number of meters per
     * {@link #LinearUnit}
     * .
     */
    public double getMetersPerUnit() throws Exception {
        return _MetersPerUnit;
    }

    public void setMetersPerUnit(double value) throws Exception {
        _MetersPerUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UNIT[\"%s\", %f", getName(), getMetersPerUnit()));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        return String.format("<CS_LinearUnit MetersPerUnit=\"%f\">%s</CS_LinearUnit>", getMetersPerUnit(), getInfoXml());
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
        if (!(obj instanceof LinearUnit))
            return false;

        LinearUnit lu = (LinearUnit)obj;
        return lu.getMetersPerUnit() == this.getMetersPerUnit();
    }

}


