package com.github.sylvainjuge.fsutils;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static com.github.sylvainjuge.fsutils.ByteUnit.*;

@Test(enabled = false)
public class ByteUnitTest {

    @Test
    public void convertIdentity() {
        for (ByteUnit unit : values()) {
            assertThat(unit.to(unit).convert(42)).isEqualTo(42);
        }
    }

    @Test
    public void convertToUpperUnit1024Factor() {
        int factor = 1024;
        ByteUnit[] units = values();
        for (int i = 0; i < units.length - 1; i++) {
            Converter converter = units[i].to(units[i + 1]);

            assertThat(converter.convert(factor - 1)).isEqualTo((factor - 1d) / factor);
            assertThat(converter.convert(factor)).isEqualTo(1);
            assertThat(converter.convert(factor + 1)).isEqualTo((factor + 1d) / factor);

            Converter reverseConverter = converter.reverse();
            assertThat(reverseConverter.convert(1)).isEqualTo(factor);
        }
    }

    @Test
    public void kilobyteToBytes() {
        assertThat(KILOBYTES.to(BYTES).convert(1)).isEqualTo(1024);
        assertThat(KILOBYTES.to(BYTES).convert(2)).isEqualTo(2048);
    }

    @Test
    public void bytesToKilobytes() {
        assertThat(BYTES.to(KILOBYTES).convert(1024)).isEqualTo(1);
    }

    @Test
    public void zeroReadableUnitIsBytes() {
        for (ByteUnit u : ByteUnit.values()) {
            assertThat(u.prettyUnit(0)).isEqualTo(BYTES);
        }
    }

    @Test
    public void petabytesReadableUnitIsPetabytes() {
        assertThat(PETABYTES.prettyUnit(1)).isEqualTo(PETABYTES);
    }

    @Test
    public void generalPrettyUnits() {

        ByteUnit[] units = ByteUnit.values();
        for (int i = 0; i < units.length - 1; i++) {
            ByteUnit current = units[i];
            ByteUnit next = units[i + 1];

            assertThat(current.prettyUnit(1023)).isEqualTo(current);
            assertThat(current.prettyUnit(1024)).isEqualTo(next);

        }
    }

    @Test
    public void readableUnitsSamples(){

        assertThat(BYTES.toPretty(0).targetUnit()).isEqualTo(BYTES);
        assertThat(BYTES.toPretty(1).targetUnit()).isEqualTo(BYTES);
        assertThat(BYTES.toPretty(1024).targetUnit()).isEqualTo(KILOBYTES);
        assertThat(BYTES.toPretty(1024*1024).targetUnit()).isEqualTo(MEGABYTES);
        assertThat(BYTES.toPretty(1024*1024*1024).targetUnit()).isEqualTo(GIGABYTES);
        // we can't test conversion from bytes to bigger units because of long overflow

        assertThat(MEGABYTES.toPretty(1024).targetUnit()).isEqualTo(GIGABYTES);
        assertThat(MEGABYTES.toPretty(1024*1024).targetUnit()).isEqualTo(TERABYTES);
        assertThat(MEGABYTES.toPretty(1024*1024*1024).targetUnit()).isEqualTo(PETABYTES);


    }

}
