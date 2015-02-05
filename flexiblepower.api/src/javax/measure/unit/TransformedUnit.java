/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.unit;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;

/**
 * <p>
 * This class represents the units derived from other units using {@link UnitConverter converters}.
 * </p>
 * 
 * <p>
 * Examples of transformed units:[code] CELSIUS = KELVIN.add(273.15); FOOT = METER.multiply(0.3048); MILLISECOND =
 * MILLI(SECOND); [/code]
 * </p>
 * 
 * <p>
 * Transformed units have no label. But like any other units, they may have labels attached to them:[code]
 * UnitFormat.getStandardInstance().label(FOOT, "ft"); [/code] or aliases: [code]
 * UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimeter");
 * UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimetre"); [/code]
 * </p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 * @see Unit#plus(double)
 * @see Unit#times(double)
 * @see Unit#transform(UnitConverter)
 * @see UnitFormat
 */
public final class TransformedUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the parent unit (not a transformed unit).
     */
    private final Unit<Q> parentUnit;

    /**
     * Holds the converter to the parent unit.
     */
    private final UnitConverter toParentUnit;

    /**
     * Creates a transformed unit from the specified parent unit.
     * 
     * @param parentUnit
     *            the untransformed unit from which this unit is derived.
     * @param toParentUnit
     *            the converter to the parent units.
     * @throws IllegalArgumentException
     *             if <code>toParentUnit == 
     *         {@link UnitConverter#IDENTITY UnitConverter.IDENTITY}</code>
     */
    TransformedUnit(Unit<Q> parentUnit, UnitConverter toParentUnit) {
        if (toParentUnit == UnitConverter.IDENTITY) {
            throw new IllegalArgumentException("Identity not allowed");
        }
        this.parentUnit = parentUnit;
        this.toParentUnit = toParentUnit;
    }

    /**
     * Returns the parent unit for this unit. The parent unit is the untransformed unit from which this unit is derived.
     * 
     * @return the untransformed unit from which this unit is derived.
     */
    public Unit<Q> getParentUnit() {
        return this.parentUnit;
    }

    /**
     * Returns the converter to the parent unit.
     * 
     * @return the converter to the parent unit.
     */
    public UnitConverter toParentUnit() {
        return this.toParentUnit;
    }

    /**
     * Indicates if this transformed unit is considered equals to the specified object (both are transformed units with
     * equal parent unit and equal converter to parent unit).
     * 
     * @param that
     *            the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code> are considered equals; <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof TransformedUnit)) {
            return false;
        }
        TransformedUnit<?> thatUnit = (TransformedUnit<?>) that;
        return this.parentUnit.equals(thatUnit.parentUnit) && this.toParentUnit.equals(thatUnit.toParentUnit);
    }

    // Implements abstract method.
    @Override
    public int hashCode() {
        return this.parentUnit.hashCode() + this.toParentUnit.hashCode();
    }

    // Implements abstract method.
    @Override
    public Unit<? super Q> getStandardUnit() {
        return this.parentUnit.getStandardUnit();
    }

    // Implements abstract method.
    @Override
    public UnitConverter toStandardUnit() {
        return this.parentUnit.toStandardUnit().concatenate(this.toParentUnit);
    }

    private static final long serialVersionUID = 1L;

}
