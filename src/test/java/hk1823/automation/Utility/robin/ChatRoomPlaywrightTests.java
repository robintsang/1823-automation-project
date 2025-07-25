package hk1823.automation.Utility.robin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ChatRoomPlaywrightTests {
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
            // 設定瀏覽器視窗大小為 1920x1280 (Set browser viewport to 1920x1280)
            new BrowserType.LaunchPersistentContextOptions().setHeadless(false).setViewportSize(1920, 1280)
        );
        page = browserContext.newPage();
        // 自動將 Chromium 視窗移到螢幕中央 (Center Chromium window on screen)
        try {
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - 1920) / 2;
            int y = (screenSize.height - 1280) / 2;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac")) {
                // macOS: AppleScript
                String script = String.format(
                    "osascript -e 'tell application \"Google Chrome\" to set the bounds of the first window to {%d, %d, %d, %d}'",
                    x, y, x + 1920, y + 1280
                );
                Runtime.getRuntime().exec(script);
            } else if (os.contains("win")) {
                // Windows: PowerShell
                String ps = String.format(
                    "$hwnd = (Get-Process chrome | Where-Object {{$_.MainWindowTitle}} | Select-Object -First 1).MainWindowHandle; " +
                    "Add-Type -TypeDefinition '[DllImport(\"user32.dll\")]public static extern bool MoveWindow(IntPtr hWnd, int X, int Y, int nWidth, int nHeight, bool bRepaint);' -Name Win32 -Namespace Native; " +
                    "[Native.Win32]::MoveWindow($hwnd, %d, %d, %d, %d, $true);",
                    x, y, 1920, 1280
                );
                String[] cmd = {"powershell", "-Command", ps};
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception e) {
            System.out.println("[警告] 無法自動置中 Chromium 視窗: " + e.getMessage());
        }
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
