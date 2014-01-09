package com.github.sylvainjuge.fsutils;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static com.github.sylvainjuge.fsutils.SizeUnit.*;

@Test(enabled = false)
public class SizeUnitTest {

    @Test
    public void convertIdentity() {
        for (SizeUnit unit : values()) {
            assertThat(unit.to(unit).convert(42)).isEqualTo(42);
        }
    }

    @Test
    public void convertToUpperUnit1024Factor() {
        int factor = 1024;
        SizeUnit[] units = values();
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
        for (SizeUnit u : SizeUnit.values()) {
            assertThat(u.prettyUnit(0)).isEqualTo(BYTES);
        }
    }

    @Test
    public void petabytesReadableUnitIsPetabytes() {
        assertThat(PETABYTES.prettyUnit(1)).isEqualTo(PETABYTES);
    }

    @Test
    public void generalPrettyUnits() {

        SizeUnit[] units = SizeUnit.values();
        for (int i = 0; i < units.length - 1; i++) {
            SizeUnit current = units[i];
            SizeUnit next = units[i + 1];

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
