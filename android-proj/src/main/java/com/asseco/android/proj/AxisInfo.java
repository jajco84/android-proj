package com.asseco.android.proj;


/**
 * Details of axis. This is used to label axes, and indicate the orientation.
 */
public class AxisInfo {
    private String _Name;
    private AxisOrientationEnum _Orientation = AxisOrientationEnum.Other;

    /**
     * Initializes a new instance of an AxisInfo.
     *
     * @param name        Name of axis
     * @param orientation Axis orientation
     * @throws Exception the exception
     */
    public AxisInfo(String name, AxisOrientationEnum orientation) throws Exception {
        _Name = name;
        _Orientation = orientation;
    }

    /**
     * Human readable name for axis. Possible values are X, Y, Long, Lat or any other short string.
     *
     * @return the name
     * @throws Exception the exception
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
     * Gets enumerated value for orientation.
     *
     * @return the orientation
     * @throws Exception the exception
     */
    public AxisOrientationEnum getOrientation() throws Exception {
        return _Orientation;
    }

    /**
     * Sets orientation.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setOrientation(AxisOrientationEnum value) throws Exception {
        _Orientation = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     *
     * @return the wkt
     * @throws Exception the exception
     */
    public String getWKT() throws Exception {
        return String.format("AXIS[\"%s\", %s]", getName(), getOrientation().toString().toString().toUpperCase());
    }

    /**
     * Gets an XML representation of this object
     *
     * @return the xml
     * @throws Exception the exception
     */
    public String getXML() throws Exception {
        return String.format("<CS_AxisInfo Name=\"%s\" Orientation=\"%s\"/>", getName(), getOrientation().toString().toUpperCase());
    }

}


