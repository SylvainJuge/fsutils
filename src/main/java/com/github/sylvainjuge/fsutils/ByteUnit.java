package com.github.sylvainjuge.fsutils;

public enum ByteUnit {


    BYTES("b"),
    KILOBYTES("Kb"),
    MEGABYTES("Mb"),
    GIGABYTES("Gb"),
    TERABYTES("Tb"),
    PETABYTES("Pb");

    private final String abbreviation;

    ByteUnit(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String abbreviation(){
        return abbreviation;
    }

    public static final class Converter {
        private final ByteUnit from;
        private final ByteUnit to;

        private Converter(ByteUnit from, ByteUnit to) {
            this.from = from;
            this.to = to;
        }

        public double convert(long s) {
            ByteUnit targetUnit = to;
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

        public ByteUnit targetUnit() {
            return to;
        }

    }

    public Converter to(ByteUnit targetUnit) {
        return new Converter(this, targetUnit);
    }

    public Converter toPretty(long quantity) {
        return new Converter(this, prettyUnit(quantity));
    }

    public ByteUnit prettyUnit(long quantity) {
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
        ByteUnit[] values = values();
        while (result < values.length && readableQuantity >= 1024) {
            readableQuantity /= 1024;
            result++;
        }

        return values[result];
    }

}
