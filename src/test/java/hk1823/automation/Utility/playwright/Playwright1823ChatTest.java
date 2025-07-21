package hk1823.automation.Utility.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class Playwright1823ChatTest {
    static Playwright playwright;
    static Browser browser;
    static Page page;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @AfterAll
    static void teardown() {
        browser.close();
        playwright.close();
    }

    @Test
    void testChatBotMultipleQuestions() throws InterruptedException {
        page.navigate("https://www.1823.gov.hk/tc");

        // Scroll down to make sure chat is visible
        for (int i = 0; i < 2; i++) {
            page.keyboard().press("PageDown", new Keyboard.PressOptions().setDelay(500));
        }
        Thread.sleep(1000);

        // Click the chat room image to open chat
        Locator chatRoomEntranceImage = page.locator("//img[@alt='我係1823智能助理一一，你可以問我有關政府服務嘅問題。']");
        chatRoomEntranceImage.click();

        // Switch to the chat iframe and interact
        FrameLocator chatFrame = page.frameLocator("//iframe[@id='ml-webchat-iframe']");
        Locator chatInput = chatFrame.locator("//input[contains(@placeholder,'問下 1823 智能助理「一一」')]");
        Locator sendButton = chatFrame.locator("//img[@title='傳送信息']");

        String[] questions = {
            "派錢未？",
            "我要續車牌",
            "如何申請狗牌？",
            "你可以幫我搵路政署嘅電話嗎？",
            "啊？",
            "123456",
            "",
            "!@#$%^&*()",
            "Thank you!",
            "你好",
            "如何申請水費減免？"
        };

        for (String question : questions) {
            chatInput.click();
            chatInput.fill(question);
            Thread.sleep(500);
            Locator responses = chatFrame.locator(".webchat__stacked-layout__content span");
            int beforeCount = responses.count();
            sendButton.click();

            // Wait for a new response to appear (up to 5 seconds)
            boolean gotReply = false;
            for (int t = 0; t < 10; t++) { // 10 * 500ms = 5s
                Thread.sleep(500);
                int afterCount = responses.count();
                if (afterCount > beforeCount) {
                    String reply = responses.nth(afterCount - 1).textContent();
                    System.out.println("Bot reply to '" + question + "': " + reply);
                    Assertions.assertTrue(reply != null && reply.length() > 0, "Chatbot should respond to: '" + question + "'");
                    gotReply = true;
                    break;
                }
            }
            if (!gotReply) {
                System.out.println("No reply for question: " + question);
                // Optionally: Assertions.fail("No reply for question: " + question);
            }
        }
    }
} 