package academy.teenfuture.crse.utility;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class BaseFormTest {
    private static final String[] TEST_SUBJECTS = {
        "Clean-up of Refuses or Streets",
        "Water Dripping outside Building Units", 
        "Slope"
    };
    private static final String BASE_URL = "https://www.1823.gov.hk/en/form/complain";
    private static final int NAV_TIMEOUT = 5000;
    private static final int ACTION_TIMEOUT = 5000;
    private static final int SLOW_MO = 300;
    private static final String REQUIRED_FIELD_ERROR = "This field is required.";
    private static final String TEST_INPUT_TEXT = "Test input for other option";
    private static final String TEXT_LIMIT_ERROR = "Character size limit exceeded.";
    private static final String FILE_SIZE_LIMIT_ERROR = "The file cannot be uploaded because the total file size has exceeded the maximum size limit of 25MB.";
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    private static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // ===== Setup & Teardown =====
    @BeforeAll
    static void launchBrowser() {
        System.out.println("Starting test suite - launching browser");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setChannel("chrome")
            .setHeadless(false)
            .setSlowMo(SLOW_MO)
            .setArgs(Arrays.asList("--disable-extensions", "--no-sandbox")));
    }

    @AfterAll
    static void closeBrowser() {
        System.out.println("Completing test suite - closing browser");
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    // ===== Test Methods =====
    @Test
    void testSubjectCardOpensAndSelectRadioButtons() {
        System.out.println("Starting main test: Form navigation and validation");
        
        for (String testSubject : TEST_SUBJECTS) {
            System.out.println("\nTesting subject: " + testSubject);
            try {
                openPage();
                testSubjectCard(testSubject);
                verifyFormName(testSubject);
                navigateThroughAllSteps();
                System.out.println("Completed testing for: " + testSubject);
            } catch (Exception e) {
                System.err.println("Failed testing for: " + testSubject);
                captureFailure(testSubject);
                throw e;
            } finally {
                closePage();
            }
        }
        
        System.out.println("\nMain test completed successfully");
    }

    // ===== Helper Methods =====
    private void openPage() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(SCREEN_WIDTH, SCREEN_HEIGHT));
        page = context.newPage();
        page.setDefaultNavigationTimeout(NAV_TIMEOUT);
        page.setDefaultTimeout(ACTION_TIMEOUT);
        Response response = page.navigate(BASE_URL);
        assertNavigationSuccess(response);
        System.out.println("Opened base URL: " + BASE_URL);
    }

    private void closePage() {
        if (page != null) page.close();
        if (context != null) context.close();
        System.out.println("Closed page and context");
    }

    private void testSubjectCard(String testSubject) {
        Locator card = page.locator(
            "figure.placeholder.placeholder--complain-type:has(img[alt='" + testSubject + "'])");
        Response response = page.waitForNavigation(() -> card.click());
        assertNavigationSuccess(response);
        System.out.println("Selected subject card: " + testSubject);
    }

    private void verifyFormName(String expectedSubject) {
        Locator formNameElement = page.locator("p.form-theme__name");
        String actualFormName = formNameElement.textContent().trim();
        assertThat("Form name mismatch", actualFormName, equalTo(expectedSubject));
        System.out.println("Verified form name: " + actualFormName);
    }

    private void navigateThroughAllSteps() {
        boolean reachedResubmit = false;
        
        while (!reachedResubmit) {
            try {
                Locator stepText = page.locator("div.form-group__label.form-group__label--resubmit");
                stepText.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(2000));
                System.out.println("Reached resubmit step");
                testTheRest();
                reachedResubmit = true;
            } catch (Exception e) {
                testRadioButtons();
            }
        }
    }

    private void testTheRest() {
        Locator noRadio = page.locator("label.radio-label:has-text('No')");
        String noRadioText = noRadio.textContent().trim();
        noRadio.click();
        System.out.println("Select radio option: " + noRadioText);
        
        testTextInputLengthValidation();
        testMap();
        testUpload();
        testFillInPersonalInfo();
    }

    private void testRadioButtons() {
        if (isQuestionRequired()) {
            System.out.println("Testing required field validation");
            testRequiredFieldValidation();
        }
        testRegularRadioOptions();
        testOtherRadioOptions();
        clickNextButton();
    }

    private boolean isQuestionRequired() {
        return page.locator("p.form-notice:has-text('Required')").count() > 0;
    }

    private void testRequiredFieldValidation() {
        clickNextButton();
        checkErrorMessage(REQUIRED_FIELD_ERROR);
    }

    private void checkErrorMessage(String message) {
        Locator errorMessages = page.getByText(message);
        if (errorMessages.count() > 0) {
            System.out.println("Verified expected error: " + message);
        } else {
            System.out.println("Expected error not found: " + message);
        }
    }

    private void testRegularRadioOptions() {
        Locator regularOptions = page.locator(".radio-option:not(.radio-option-other)");
        int optionCount = regularOptions.count();
        if (optionCount > 0) {
            for (int i = 0; i < optionCount; i++) {
                Locator option = regularOptions.nth(i);
                String optionText = option.locator("span").first().textContent().trim();
                // Print the radio option text
                System.out.println("Selected radio option: " + optionText);
                page.locator("//span[normalize-space()='" + optionText + "']").click();
                assertThat("Radio not checked: " + optionText,
                    option.locator("input[type='radio']").isChecked(), is(true));
            }
        }
    }

    private void testOtherRadioOptions() {
        Locator otherOptions = page.locator(".radio-option-other");
        int optionCount = otherOptions.count();
        if (optionCount > 0) {
            for (int i = 0; i < optionCount; i++) {
                Locator option = otherOptions.nth(i);
                String optionText = option.locator("span").first().textContent().trim();
                // Print the radio option text
                System.out.println("Selected radio option: " + optionText);
                page.locator("//span[normalize-space()='" + optionText + "']").click();
                assertThat("Other radio not checked: " + optionText,
                    option.locator("input[type='radio']").isChecked(), is(true));
                testOtherTextInput(option);
            }
        }
    }

    private void testOtherTextInput(Locator option) {
        Locator textInput = option.locator(".other-input");
        if (textInput.count() > 0) {
            assertThat("Text input disabled", textInput.isEnabled(), is(true));
            clickNextButton();
            checkErrorMessage(REQUIRED_FIELD_ERROR);
            textInput.fill(TEST_INPUT_TEXT);
            assertThat("Input value mismatch", textInput.inputValue(), equalTo(TEST_INPUT_TEXT));
            System.out.println("Entered text in 'other' input field");
        }
    }

    private void clickNextButton() {
        Locator nextButton = page.locator("button:has-text('Next')");
        if (nextButton.count() > 0 && nextButton.isEnabled()) {
            String buttonText = nextButton.textContent().trim();
            nextButton.click(new Locator.ClickOptions().setTimeout(ACTION_TIMEOUT));
            System.out.println("Clicked button: " + buttonText);
        }
    }

    private void testTextInputLengthValidation() {
        Locator textArea = page.locator("textarea[id='Supplementary Information']");
        if (textArea.count() > 0) {           
            System.out.println("Testing text input length validation");
            textArea.fill(generateText(1500));
            assertThat("Unexpected error for 1500 chars",
                page.locator("textarea + .error-message, textarea ~ .error-message").count(), is(0));
            
            textArea.fill(generateText(1501));
            clickNextButton();
            checkErrorMessage(TEXT_LIMIT_ERROR);
            textArea.fill("");
        }
    }

    private String generateText(int length) {
        return new String(new char[length]).replace('\0', 'a');
    }

    private void testMap() {
        Locator addressInput = page.getByPlaceholder("Enter location");
        if (addressInput.count() > 0) {
            System.out.println("Testing map location input");
            addressInput.fill("26 Nathan Road");
            Locator addressSuggest = page.getByText("26 Nathan Road");
            addressSuggest.click();
        }
    }

    private void testUpload() {
        try {
            System.out.println("Testing file upload validation");
            FileChooser fileChooser = page.waitForFileChooser(() -> {
                page.getByText("Select or drag and drop files to here").click();
            });
            
            List<Path> filesList = new ArrayList<>();
            String basePath = "testPhoto";
            for (int i = 1; i <= 10; i++) {
                filesList.add(Paths.get(basePath + i + ".jpg"));
            }
            
            fileChooser.setFiles(filesList.toArray(new Path[0]));
            page.waitForTimeout(10000);
            checkErrorMessage(FILE_SIZE_LIMIT_ERROR);
            page.locator(".upload-list__clear.js-clear").click();
            clickNextButton();
        } catch (Exception e) {
            System.out.println("File upload test encountered issue: " + e.getMessage());
        }
    }

    private void testFillInPersonalInfo() {
        System.out.println("=== Starting Personal Information Tests ===");
        
        // Test Case 1: Click 'No' on first radio button
        System.out.println("\n--- Test Case 1: Click 'No' on first option ---");
        Locator no_1 = page.locator("(//span[contains(text(),'No')])[1]");
        String no1Text = no_1.textContent().trim();
        no_1.click();
        System.out.println("Selected option: '" + no1Text + "'");
        
        // Verify all other radio buttons and checkboxes are disabled
        verifyElementsDisabled();
        
        // Test Case 2: Click 'Yes' on both initial options
        System.out.println("\n--- Test Case 2: Click 'Yes' on both initial options ---");
        Locator yes_1 = page.locator("(//span[contains(text(),'Yes')])[1]");
        String yes1Text = yes_1.textContent().trim();
        yes_1.click();
        System.out.println("Selected first option: '" + yes1Text + "'");
        
        Locator yes_2 = page.locator("(//span[contains(text(),'Yes')])[2]");
        String yes2Text = yes_2.textContent().trim();
        yes_2.click();
        System.out.println("Selected second option: '" + yes2Text + "'");
        
        // Fill in personal information
        System.out.println("Filling in personal information");
        Locator nameInput = page.locator("#name");
        nameInput.fill("Tester Name");
        System.out.println("Entered name: " + nameInput.inputValue());
        
        Locator emailInput = page.locator("#email");
        emailInput.fill("test@example.com");
        System.out.println("Entered email: " + emailInput.inputValue());
        
        Locator phoneInput = page.locator("#phone");
        phoneInput.fill("12345678");
        System.out.println("Entered phone: " + phoneInput.inputValue());
        
        // Check all timeslot boxes
        System.out.println("Selecting all timeslots");
        List<Locator> timeSlots = page.locator("input[id^='time_']").all();
        for (int i = 0; i < timeSlots.size(); i++) {
            Locator timeSlotInput = timeSlots.get(i);
            Locator timeSlotLabel = page.locator("label[for='" + timeSlotInput.getAttribute("id") + "'] span");
            String timeSlotText = timeSlotLabel.textContent().trim();
            
            timeSlotLabel.click();
            assertThat("Timeslot " + (i+1) + " should be checked", 
                timeSlotInput.isChecked(), is(true));
            System.out.println("Selected timeslot: '" + timeSlotText + "'");
        }
        
        // Test final radio button combinations
        System.out.println("Testing final radio button combinations");
        
        // First combination: Yes, then No
        Locator yes_3 = page.locator("(//span[contains(text(),'Yes')])[3]");
        String yes3Text = yes_3.textContent().trim();
        yes_3.click();
        System.out.println("Selected third option: '" + yes3Text + "'");
        assertThat("Third yes should be checked", yes_3.locator("..").locator("input").isChecked(), is(true));
        
        Locator no_3 = page.locator("(//span[contains(text(),'No')])[3]");
        String no3Text = no_3.textContent().trim();
        no_3.click();
        System.out.println("Selected third option: '" + no3Text + "'");
        assertThat("Third no should be checked", no_3.locator("..").locator("input").isChecked(), is(true));
        
        clickNextButton();
        System.out.println("=== Completed Personal Information Tests ===");
    }
    
    private void verifyElementsDisabled() {
        // Check if second radio group is disabled
        List<Locator> secondRadioGroup = page.locator("(//input[@type='radio'])[position() >= 3 and position() <=4]").all();
        for (Locator radio : secondRadioGroup) {
            String id = radio.getAttribute("id");
            Locator label = page.locator("label[for='" + id + "'] span");
            String labelText = label.textContent().trim();
            
            assertThat("Radio button '" + labelText + "' should be disabled", 
                radio.isDisabled(), is(true));
            System.out.println("Verified disabled: Radio button '" + labelText + "'");
        }
        
        // Check if personal info fields are disabled
        List<Locator> inputFields = Arrays.asList(
            page.locator("#name"),
            page.locator("#email"),
            page.locator("#phone")
        );
        for (Locator field : inputFields) {
            String fieldName = field.getAttribute("id");
            assertThat("Field '" + fieldName + "' should be disabled", 
                field.isDisabled(), is(true));
            System.out.println("Verified disabled: Input field '" + fieldName + "'");
        }
        
        // Check if timeslots are disabled
        List<Locator> timeSlots = page.locator("input[id^='time_']").all();
        for (Locator slot : timeSlots) {
            String slotId = slot.getAttribute("id");
            assertThat("Timeslot '" + slotId + "' should be disabled", 
                slot.isDisabled(), is(true));
            System.out.println("Verified disabled: Timeslot '" + slotId + "'");
        }
        
        // Check if final radio group is disabled
        List<Locator> finalRadioGroup = page.locator("(//input[@type='radio'])[position() >= last()-1 and position() <= last()]").all();
        for (Locator radio : finalRadioGroup) {
            String id = radio.getAttribute("id");
            Locator label = page.locator("label[for='" + id + "'] span");
            String labelText = label.textContent().trim();
            
            assertThat("Final radio button '" + labelText + "' should be disabled", 
                radio.isDisabled(), is(true));
            System.out.println("Verified disabled: Final radio button '" + labelText + "'");
        }
    }

    private void captureFailure(String testSubject) {
        String screenshotName = "failure-" + testSubject.replaceAll("[^a-zA-Z0-9]", "-") + ".png";
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(screenshotName))
            .setFullPage(true));
        System.out.println("Saved failure screenshot: " + screenshotName);
    }

    private void assertNavigationSuccess(Response response) {
        assertThat("Navigation failed", response.ok(), is(true));
    }
}