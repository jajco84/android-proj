package com.asseco.android.proj;

/**
 * A set of quantities from which other quantities are calculated.
 *
 * For the OGC abstract model, it can be defined as a set of real points on the earth
 * that have coordinates. EG. A datum can be thought of as a set of parameters
 * defining completely the origin and orientation of a coordinate system with respect
 * to the earth. A textual description and/or a set of parameters describing the
 * relationship of a coordinate system to some predefined physical locations (such
 * as center of mass) and physical directions (such as axis of spin). The definition
 * of the datum may also include the temporal behavior (such as the rate of change of
 * the orientation of the coordinate axes).
 */
public abstract class Datum extends Info implements IDatum {
    private DatumType _DatumType;

    /**
     * Initializes a new instance of a Datum object
     *
     * @param type         Datum type
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param remarks      Provider-supplied remarks
     * @param abbreviation Abbreviation
     * @throws Exception the exception
     */
    public Datum(DatumType type, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _DatumType = type;
    }

    /**
     * Gets or sets the type of the datum as an enumerated code.
     */
    public DatumType getDatumType() throws Exception {
        return _DatumType;
    }

    public void setDatumType(DatumType value) throws Exception {
        _DatumType = value;
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
        if (!(obj instanceof Datum))
            return false;

        return (obj instanceof Datum ? ((Datum) obj).getDatumType() == this.getDatumType() : false);
    }

}


