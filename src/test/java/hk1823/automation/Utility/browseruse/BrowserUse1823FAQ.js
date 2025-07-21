// You need to install puppeteer: npm install puppeteer
const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ headless: true });
  const page = await browser.newPage();
  await page.goto('https://www.1823.gov.hk/tc');

  // Example: Extract FAQ questions (adjust selector as needed)
  const faqs = await page.$$eval('.faq-question', nodes => nodes.map(n => n.innerText));
  console.log('FAQ Questions:', faqs);

  await browser.close();
})(); 