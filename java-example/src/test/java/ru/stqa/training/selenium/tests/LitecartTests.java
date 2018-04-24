package ru.stqa.training.selenium.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.training.selenium.model.Country;
import ru.stqa.training.selenium.model.Zone;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LitecartTests extends TestBase {

    @Test
    public void testLitecartAdminLogin() {
        app.litecart().loginAdmin();
    }

    @Test
    public void testLeftNavigationMenu() {
        app.litecart().loginAdmin();
        app.litecart().clickMenuItems();

    }

    @Test
    public void testStickers() {
        app.litecart().openMainPage();
        app.litecart().checkStickers();
    }

    @Test
    public void testCountriesAndZonesSorting() {
        SoftAssert softAssert = new SoftAssert();

        app.litecart().loginAdmin();
        List<Country> countries = app.litecart().getCountries();

        boolean isSortedByName = countries.stream().sorted(Comparator.comparing(Country::getName)).collect(Collectors.toList()).equals(countries);
        softAssert.assertEquals(isSortedByName, true);

        countries.stream()
                .filter((c) -> c.getZonesCount() > 0)
                .forEach((c) -> {
                    boolean areZonesSortedByName = c.getZones().stream().sorted(Comparator.comparing(Zone::getName)).collect(Collectors.toList()).equals(c.getZones());
                    softAssert.assertEquals(areZonesSortedByName, true, c.getName());
                });
        softAssert.assertAll();
    }


    @Test
    public void testCountriesAndZonesSorting2() {
        SoftAssert softAssert = new SoftAssert();

        app.litecart().loginAdmin();
        List<Country> countries = app.litecart().getGeoZonesCountries();

        countries.stream()
                .forEach((c) -> {
                    boolean areZonesSortedByName = c.getZones().stream().sorted(Comparator.comparing(Zone::getName)).collect(Collectors.toList()).equals(c.getZones());
                    softAssert.assertEquals(areZonesSortedByName, true, c.getName());
                });
        softAssert.assertAll();
    }
}
