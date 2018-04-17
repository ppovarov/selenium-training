package ru.stqa.training.selenium.tests;

import org.testng.annotations.Test;

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

}
