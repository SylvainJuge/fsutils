package com.github.sylvainjuge.fsutils;


// TODO : find appropriate name like ByteUnits
public enum SizeUnit {

    BYTES("b"),
    KILOBYTES("Kb"),
    MEGABYTES("Mb"),
    GIGABYTES("Gb"),
    TERABYTES("Tb"),
    PETABYTES("Pb");

    private final String abbrev;

    SizeUnit(String abbrev){
        this.abbrev = abbrev;
    }

    public static final class Converter {
        private final SizeUnit from;
        private final SizeUnit to;

        private Converter(SizeUnit from, SizeUnit to) {
            this.from = from;
            this.to = to;
        }

        public double convert(long s) {
            SizeUnit targetUnit = to;
            if (targetUnit == null) {
                targetUnit = from.prettyUnit(s);
            }
            int diff = targetUnit.ordinal() - from.ordinal();
            double factor = Math.pow(1024, Math.abs(diff));
            return diff <= 0 ? s * factor : s / factor;
        }

        public Converter reverse() {
            return new Converter(to, from);
        }

        public SizeUnit targetUnit() {
            return to;
        }

    }

    public Converter to(SizeUnit targetUnit) {
        return new Converter(this, targetUnit);
    }

    public Converter toPretty(long quantity) {
        return new Converter(this, prettyUnit(quantity));
    }

    public SizeUnit prettyUnit(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }
        if (quantity == 0) {
            return BYTES;
        }
        if (this == PETABYTES) {
            return PETABYTES;
        }
        long readableQuantity = quantity;
        int result = this.ordinal();
        SizeUnit[] values = values();
        while (result < values.length && readableQuantity >= 1024) {
            readableQuantity /= 1024;
            result++;
        }

        return values[result];
    }

}
