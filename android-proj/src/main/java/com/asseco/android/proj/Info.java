package com.asseco.android.proj;

/**
 * The Info object defines the standard information
 * stored with spatial reference objects
 */
public abstract class Info implements IInfo {
    private String _Name = new String();
    private String _Authority = new String();
    private long _Code;
    private String _Alias = new String();
    private String _Abbreviation = new String();
    private String _Remarks = new String();

    /**
     * A base interface for metadata applicable to coordinate system objects.
     * The metadata items Abbreviation, Alias, Authority, AuthorityCode, Name and Remarks
     * were specified in the Simple Features interfaces, so they have been kept here.This specification does not dictate what the contents of these items
     * should be. However, the following guidelines are suggested:When
     * {@link ICoordinateSystemAuthorityFactory}
     * is used to create an object, the Authority
     * and 'AuthorityCode' values should be set to the authority name of the factory object, and the authority
     * code supplied by the client, respectively. The other values may or may not be set. (If the authority is
     * EPSG, the implementer may consider using the corresponding metadata values in the EPSG tables.)When
     * {@link CoordinateSystemFactory}
     * creates an object, the 'Name' should be set to the value
     * supplied by the client. All of the other metadata items should be left empty
     *
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param abbreviation Abbreviation
     * @param remarks      Provider-supplied remarks
     * @throws Exception the exception
     */
    public Info(String name, String authority, long code, String alias, String abbreviation, String remarks) throws Exception {
        _Name = name;
        _Authority = authority;
        _Code = code;
        _Alias = alias;
        _Abbreviation = abbreviation;
        _Remarks = remarks;
    }

    /**
     * Gets or sets the name of the object.
     */
    public String getName() throws Exception {
        return _Name;
    }

    /**
     * Sets name.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setName(String value) throws Exception {
        _Name = value;
    }

    /**
     * Gets or sets the authority name for this object, e.g., "EPSG",
     * is this is a standard object with an authority specific
     * identity code. Returns "CUSTOM" if this is a custom object.
     */
    public String getAuthority() throws Exception {
        return _Authority;
    }

    /**
     * Sets authority.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setAuthority(String value) throws Exception {
        _Authority = value;
    }

    /**
     * Gets or sets the authority specific identification code of the object
     */
    public long getAuthorityCode() throws Exception {
        return _Code;
    }

    /**
     * Sets authority code.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setAuthorityCode(long value) throws Exception {
        _Code = value;
    }

    /**
     * Gets or sets the alias of the object.
     */
    public String getAlias() throws Exception {
        return _Alias;
    }

    /**
     * Sets alias.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setAlias(String value) throws Exception {
        _Alias = value;
    }

    /**
     * Gets or sets the abbreviation of the object.
     */
    public String getAbbreviation() throws Exception {
        return _Abbreviation;
    }

    /**
     * Sets abbreviation.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setAbbreviation(String value) throws Exception {
        _Abbreviation = value;
    }

    /**
     * Gets or sets the provider-supplied remarks for the object.
     */
    public String getRemarks() throws Exception {
        return _Remarks;
    }

    /**
     * Sets remarks.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setRemarks(String value) throws Exception {
        _Remarks = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String toString() {
        try {
            return getWKT();
        } catch (RuntimeException __dummyCatchVar0) {
            throw __dummyCatchVar0;
        } catch (Exception __dummyCatchVar0) {
            throw new RuntimeException(__dummyCatchVar0);
        }

    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public abstract String getWKT() throws Exception;

    /**
     * Gets an XML representation of this object.
     */
    public abstract String getXML() throws Exception;

    /**
     * Returns an XML string of the info object
     *
     * @return the info xml
     * @throws Exception the exception
     */
    public String getInfoXml() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<CS_Info");
        if (getAuthorityCode() > 0)
            sb.append(String.format(" AuthorityCode=\"%s\"", getAuthorityCode()));

        if (getAbbreviation() != null && getAbbreviation() != "")
            sb.append(String.format(" Abbreviation=\"%s\"", getAbbreviation()));

        if (getAuthority() != null || getAuthority() != "")
            sb.append(String.format(" Authority=\"%s\"", getAuthority()));

        if (getName() != null && getName() != "")
            sb.append(String.format(" Name=\"%s\"", getName()));

        sb.append("/>");
        return sb.toString();
    }

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    public abstract boolean equalParams(Object obj) throws Exception;

}


