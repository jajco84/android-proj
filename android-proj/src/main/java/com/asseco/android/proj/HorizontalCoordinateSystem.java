package com.asseco.android.proj;

import java.util.List;

/**
 * A 2D coordinate system suitable for positions on the Earth's surface.
 */
public abstract class HorizontalCoordinateSystem extends CoordinateSystem implements IHorizontalCoordinateSystem {
    private IHorizontalDatum _HorizontalDatum;

    /**
     * Creates an instance of HorizontalCoordinateSystem
     *
     * @param datum        Horizontal datum
     * @param AxisInfo     Axis information
     * @param name         Name
     * @param authority    Authority name
     * @param code         Authority-specific identification code.
     * @param alias        Alias
     * @param remarks      Provider-supplied remarks
     * @param abbreviation Abbreviation
     * @throws Exception the exception
     */
    public HorizontalCoordinateSystem(IHorizontalDatum datum, List<AxisInfo> AxisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _HorizontalDatum = datum;
        if (AxisInfo.size() != 2)
            throw new IllegalArgumentException("Axis info should contain two axes for horizontal coordinate systems");

        super.setAxisInfo(AxisInfo);
    }

    /**
     * Gets or sets the HorizontalDatum.
     */
    public IHorizontalDatum getHorizontalDatum() throws Exception {
        return _HorizontalDatum;
    }

    public void setHorizontalDatum(IHorizontalDatum value) throws Exception {
        _HorizontalDatum = value;
    }

}


