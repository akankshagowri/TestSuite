import {test as base, expect, Page} from '@playwright/test';
const test = base.extend<{shop: Page}>({
  shop: async ({page}, use) => {
    await page.goto('/');
    await page.getByPlaceholder('Username').fill('standard_user');
    await page.getByPlaceholder('Password').fill('secret_sauce');
    await page.locator('[data-test="login-button"]').click();
    await expect(page).toHaveURL(/inventory.html/);
    await use(page);
  }
});
test('locked account is rejected', async ({page}) => {
  await page.goto('/');
  await page.getByPlaceholder('Username').fill('locked_out_user');
  await page.getByPlaceholder('Password').fill('secret_sauce');
  await page.locator('[data-test="login-button"]').click();
  await expect(page.locator('[data-test="error"]')).toContainText('locked out');
  await expect(page).not.toHaveURL(/inventory.html/);
});
test('fresh context starts with an empty cart', async ({shop}) => {
  await shop.locator('.shopping_cart_link').click();
  await expect(shop.locator('.cart_item')).toHaveCount(0);
});
test('add and remove a product', async ({shop}) => {
  await shop.locator('[data-test="add-to-cart-sauce-labs-backpack"]').click();
  await expect(shop.locator('.shopping_cart_badge')).toHaveText('1');
  await shop.locator('.shopping_cart_link').click();
  await expect(shop.locator('.inventory_item_name')).toHaveText('Sauce Labs Backpack');
  await shop.locator('[data-test="remove-sauce-labs-backpack"]').click();
  await expect(shop.locator('.cart_item')).toHaveCount(0);
});
test('sort prices from low to high', async ({shop}) => {
  await shop.locator('[data-test="product-sort-container"]').selectOption('lohi');
  const prices = (await shop.locator('.inventory_item_price').allTextContents()).map(s => Number(s.replace('$','')));
  expect(prices).toEqual([...prices].sort((a,b) => a-b));
});
for (const field of ['firstName', 'lastName', 'postalCode']) {
  test(`checkout requires ${field}`, async ({shop}) => {
    await shop.locator('[data-test="add-to-cart-sauce-labs-backpack"]').click();
    await shop.locator('.shopping_cart_link').click();
    await shop.locator('[data-test="checkout"]').click();
    for (const [name,value] of Object.entries({firstName:'QA',lastName:'Tester',postalCode:'SW1A 1AA'})) {
      if (name !== field) await shop.locator(`[data-test="${name}"]`).fill(value);
    }
    await shop.locator('[data-test="continue"]').click();
    await expect(shop.locator('[data-test="error"]')).toContainText('is required');
    await expect(shop).toHaveURL(/checkout-step-one.html/);
  });
}
test('complete purchase verifies item, total and confirmation', async ({shop}) => {
  await shop.locator('[data-test="add-to-cart-sauce-labs-backpack"]').click();
  await shop.locator('.shopping_cart_link').click();
  await shop.locator('[data-test="checkout"]').click();
  await shop.locator('[data-test="firstName"]').fill('QA');
  await shop.locator('[data-test="lastName"]').fill('Tester');
  await shop.locator('[data-test="postalCode"]').fill('SW1A 1AA');
  await shop.locator('[data-test="continue"]').click();
  await expect(shop.locator('.inventory_item_name')).toHaveText('Sauce Labs Backpack');
  await expect(shop.locator('.summary_total_label')).toHaveText('Total: $32.39');
  await shop.locator('[data-test="finish"]').click();
  await expect(shop.locator('.complete-header')).toHaveText('Thank you for your order!');
});
