import { defineConfig, devices } from '@playwright/test';
export default defineConfig({
  testDir: './tests', fullyParallel: true, workers: 2, retries: 0,
  timeout: 30000, forbidOnly: !!process.env.CI,
  reporter: [['list'], ['html', {open: 'never'}], ['junit', {outputFile: 'test-results/junit.xml'}]],
  use: {baseURL: 'https://www.saucedemo.com', trace: 'retain-on-failure', screenshot: 'only-on-failure'},
  projects: [{name: 'chromium', use: {...devices['Desktop Chrome']}}],
  
});
