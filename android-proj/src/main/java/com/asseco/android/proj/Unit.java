package com.asseco.android.proj;


/**
 * Class for defining units
 */
public class Unit extends Info implements IUnit {
    private double _ConversionFactor;

    /**
     * Initializes a new unit
     *
     * @param conversionFactor Conversion factor to base unit
     * @param name             Name of unit
     * @param authority        Authority name
     * @param authorityCode    Authority-specific identification code.
     * @param alias            Alias
     * @param abbreviation     Abbreviation
     * @param remarks          Provider-supplied remarks
     * @throws Exception the exception
     */
    public Unit(double conversionFactor, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) throws Exception {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _ConversionFactor = conversionFactor;
    }

    /**
     * Initializes a new unit
     *
     * @param name             Name of unit
     * @param conversionFactor Conversion factor to base unit
     * @throws Exception the exception
     */
    public Unit(String name, double conversionFactor) throws Exception {
        this(conversionFactor, name, "", -1, "", "", "");
    }

    /**
     * Gets or sets the number of units per base-unit.
     *
     * @return the conversion factor
     * @throws Exception the exception
     */
    public double getConversionFactor() throws Exception {
        return _ConversionFactor;
    }

    /**
     * Sets conversion factor.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setConversionFactor(double value) throws Exception {
        _ConversionFactor = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UNIT[\"%s\", %f", getName(), _ConversionFactor));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object [NOT IMPLEMENTED].
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException();
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
        if (!(obj instanceof Unit))
            return false;

        Unit u = (Unit) obj;
        return u.getConversionFactor() == this.getConversionFactor();
    }

}


