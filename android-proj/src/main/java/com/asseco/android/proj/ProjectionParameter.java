package com.asseco.android.proj;


/**
 * A named projection parameter value.
 *
 * The linear units of parameters' values match the linear units of the containing
 * projected coordinate system. The angular units of parameter values match the
 * angular units of the geographic coordinate system that the projected coordinate
 * system is based on. (Notice that this is different from
 * {@link Parameter}
 * ,
 * where the units are always meters and degrees.)
 */
public class ProjectionParameter {
    private String _Name = new String();
    private double _Value;

    /**
     * Initializes an instance of a ProjectionParameter
     *
     * @param name  Name of parameter
     * @param value Parameter value
     * @throws Exception the exception
     */
    public ProjectionParameter(String name, double value) throws Exception {
        _Name = name;
        _Value = value;
    }

    /**
     * Parameter name.
     *
     * @return the name
     */
    public String getName() {
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
     * Parameter value.
     * The linear units of a parameters' values match the linear units of the containing
     * projected coordinate system. The angular units of parameter values match the
     * angular units of the geographic coordinate system that the projected coordinate
     * system is based on.
     *
     * @return the value
     */
    public double getValue() {
        return _Value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setValue(double value) throws Exception {
        _Value = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     *
     * @return the wkt
     * @throws Exception the exception
     */
    public String getWKT() throws Exception {
        return String.format("PARAMETER[\"%s\", %f]", getName(), getValue());
    }

    /**
     * Gets an XML representation of this object
     *
     * @return the xml
     * @throws Exception the exception
     */
    public String getXML() throws Exception {
        return String.format("<CS_ProjectionParameter Name=\"%s\" Value=\"%f\"/>", getName(), getValue());
    }

    /**
     * Function to get a textual representation of this envelope
     *
     * @return A textual representation of this envelope
     */
    public String toString() {
        return String.format("ProjectionParameter '%1s': %f", getName(), getValue());
    }

}


