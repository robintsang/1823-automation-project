package hk1823.automation.Utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MakeComplaintsTests {
    static Playwright playwright;
    static BrowserType browserType;
    static BrowserContext browserContext;
    static Page page;

    @BeforeAll
    public static void setUp() {
        String userDataDir = "./user-data/firefox";
        Path path = FileSystems.getDefault().getPath(userDataDir);
        playwright = Playwright.create();
        browserType = playwright.chromium();
        browserContext = browserType.launchPersistentContext(
            path,
            new BrowserType.LaunchPersistentContextOptions().setHeadless(false)
        );
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void chatRoomInteractionTest() throws InterruptedException {
        page.navigate("https://www.1823.gov.hk/tc");

        for (int i = 0; i < 4; i++) {
            page.keyboard().press("PageDown", new Keyboard.PressOptions().setDelay(1000));
        }
        Thread.sleep(1000);

        Locator chatRoomEntranceImage = page.locator("//img[@alt='我係1823智能助理一一，你可以問我有關政府服務嘅問題。']");
        chatRoomEntranceImage.click();

        FrameLocator chatRoomTextInputFrame = page.frameLocator("//iframe[@id='ml-webchat-iframe']");
        Locator chatRoomTextInput = chatRoomTextInputFrame.locator("//input[contains(@placeholder,'問下 1823 智能助理「一一」')]");
        chatRoomTextInput.click();

        chatRoomTextInput.fill("派錢未？");
        Thread.sleep(2000);

        Locator sendButton = chatRoomTextInputFrame.locator("//img[@title='傳送信息']");
        sendButton.click();

        Thread.sleep(5000);

        chatRoomTextInput.fill("我要續車牌");
        sendButton.click();
        Thread.sleep(8000);

        // Example assertion: check if the chat input is still visible
        assert(chatRoomTextInput.isVisible());
    }
}
