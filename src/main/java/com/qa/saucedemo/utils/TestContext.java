package com.qa.saucedemo.utils;

import com.qa.saucedemo.pages.CartPage;
import com.qa.saucedemo.pages.CheckoutCompletePage;
import com.qa.saucedemo.pages.CheckoutInfoPage;
import com.qa.saucedemo.pages.CheckoutOverviewPage;
import com.qa.saucedemo.pages.InventoryPage;
import com.qa.saucedemo.pages.LoginPage;

/**
 * Holds the "current page" references for the running scenario so separate
 * step-definition classes (LoginSteps, InventorySteps, CartSteps,
 * CheckoutSteps) can pass state between them without Cucumber's dependency
 * injection container. Backed by ThreadLocal to stay parallel-safe.
 */
public final class TestContext {

    private static final ThreadLocal<TestContext> CONTEXT = ThreadLocal.withInitial(TestContext::new);

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutInfoPage checkoutInfoPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    private TestContext() {
    }

    public static TestContext get() {
        return CONTEXT.get();
    }

    public static void reset() {
        CONTEXT.remove();
    }

    public LoginPage getLoginPage() { return loginPage; }
    public void setLoginPage(LoginPage loginPage) { this.loginPage = loginPage; }

    public InventoryPage getInventoryPage() { return inventoryPage; }
    public void setInventoryPage(InventoryPage inventoryPage) { this.inventoryPage = inventoryPage; }

    public CartPage getCartPage() { return cartPage; }
    public void setCartPage(CartPage cartPage) { this.cartPage = cartPage; }

    public CheckoutInfoPage getCheckoutInfoPage() { return checkoutInfoPage; }
    public void setCheckoutInfoPage(CheckoutInfoPage checkoutInfoPage) { this.checkoutInfoPage = checkoutInfoPage; }

    public CheckoutOverviewPage getCheckoutOverviewPage() { return checkoutOverviewPage; }
    public void setCheckoutOverviewPage(CheckoutOverviewPage checkoutOverviewPage) { this.checkoutOverviewPage = checkoutOverviewPage; }

    public CheckoutCompletePage getCheckoutCompletePage() { return checkoutCompletePage; }
    public void setCheckoutCompletePage(CheckoutCompletePage checkoutCompletePage) { this.checkoutCompletePage = checkoutCompletePage; }
}
